package org.smartlearnai.repository;

import org.smartlearnai.model.CourseHistory;
import org.smartlearnai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseHistoryRepository extends JpaRepository<CourseHistory, Long>  {
    List<CourseHistory> findAllByUserCreator(User user);

    CourseHistory findByUserCreator(User user);
}
