package org.smartlearnai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smartlearnai.config.security.JwtIssuer;
import org.smartlearnai.config.security.UserPrincipal;
import org.smartlearnai.model.User;
import org.smartlearnai.model.auth.LoginResponse;
import org.smartlearnai.model.auth.RegisterRequest;
import org.smartlearnai.model.exeptions.EmailException;
import org.smartlearnai.model.exeptions.InvalidArgumentsException;
import org.smartlearnai.model.exeptions.UserNotFoundException;
import org.smartlearnai.repository.UserRepository;
import org.smartlearnai.service.impl.AuthServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtIssuer jwtIssuer;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void registerSuccess() {
        User testUser = new User(4L, "user@test.com", "encodedPassword", "user", 21);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("user@test.com")
                .password("rawPassword")
                .age(21)
                .build();

        User user = authService.register(registerRequest);

        assertNotNull(user, "User should not be null");
        assertEquals("user@test.com", user.getEmail(), "Email should match");
        assertEquals("user", user.getRole(), "Role should match");
        assertEquals("encodedPassword", user.getPassword(), "Password should be encoded");
        assertEquals(21, user.getAge(), "Age should match");

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("rawPassword");
    }

    @Test
    void invalidEmail() {
        RegisterRequest invalidRequest = RegisterRequest.builder()
                .email("11")
                .password("password")
                .age(21)
                .build();

        EmailException exception = assertThrows(EmailException.class,
                () -> authService.register(invalidRequest),
                "Should throw EmailException for invalid email");

        verifyNoInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void attemptLogin_Success_WithCredentialsAreValid() {
        // Arrange
        String email = "user@test.com";
        String password = "validPassword";
        User mockUser = new User(1L, email, "encodedPassword", "ROLE_USER", 25);
        UserPrincipal mockPrincipal = UserPrincipal.builder()
                .userId(mockUser.getId())
                .email(mockUser.getEmail())
                .password(mockUser.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(mockUser.getRole())))
                .build();
        List<String> roles = List.of("ROLE_USER");
        String mockToken = "mockJwtToken";

        // Mock authentication flow
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockPrincipal);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Mock user repository
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(mockUser));

        // Mock JWT issuer
        when(jwtIssuer.issue(anyLong(), anyString(), anyList())).thenReturn(mockToken);

        // Act
        LoginResponse response = authService.attemptLogin(email, password);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(mockToken, response.getToken(), "Token should match");

        // Verify interactions
        verify(authenticationManager).authenticate(
                argThat(token ->
                        email.equals(token.getPrincipal()) &&
                                password.equals(token.getCredentials())
                )
        );
        verify(userRepository).findUserByEmail(email);
        verify(jwtIssuer).issue(mockUser.getId(), email, roles);

        // Verify security context was set
        Authentication contextAuth = SecurityContextHolder.getContext().getAuthentication();
        assertEquals(authentication, contextAuth, "Security context should be set");
    }


    @Test
    void attemptLogin_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        String email = "nonexistent@test.com";
        String password = "anyPassword";

        // Mock user repository to return empty Optional
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> authService.attemptLogin(email, password),
                "Should throw UserNotFoundException when user doesn't exist");

        // Verify exception message
        assertEquals(email, exception.getEmail(), "Exception should contain the email that wasn't found");

        // Verify interactions
        verify(userRepository).findUserByEmail(email);
        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtIssuer);

        // Verify security context wasn't modified
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Security context should remain null");
    }

    @ParameterizedTest
    @MethodSource("invalidCredentialsProvider")
    void attemptLogin_ShouldThrowInvalidArgumentsException_WhenCredentialsAreInvalid(
            String email, String password, String scenario) {
        // Act & Assert
        assertThrows(InvalidArgumentsException.class,
                () -> authService.attemptLogin(email, password),
                "Should throw InvalidArgumentsException for scenario: " + scenario);

        // Verify no interactions with any mocks
        verifyNoInteractions(userRepository);
        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtIssuer);

        // Verify security context wasn't modified
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Security context should remain null for scenario: " + scenario);
    }

    private static Stream<Arguments> invalidCredentialsProvider() {
        return Stream.of(
                Arguments.of(null, "password", "null email"),
                Arguments.of("", "password", "empty email"),
                Arguments.of("user@test.com", null, "null password"),
                Arguments.of("user@test.com", "", "empty password")
//                ,
//                Arguments.of("  ", "password", "blank email"),
//                Arguments.of("user@test.com", "   ", "blank password")
        );
    }
}