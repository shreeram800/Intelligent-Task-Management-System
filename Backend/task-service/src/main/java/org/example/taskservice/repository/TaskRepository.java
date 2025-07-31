package org.example.taskservice.repository;


import org.example.taskservice.entity.Project;
import org.example.taskservice.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findByAssignedToUserIdAndDeletedFalse(Long userId);

    @Query("""
        SELECT t FROM Task t
        WHERE t.assignedToUserId = :userId
        ORDER BY\s
            CASE t.priority
                WHEN 'CRITICAL' THEN 1
                WHEN 'HIGH' THEN 2
                WHEN 'MEDIUM' THEN 3
                WHEN 'LOW' THEN 4
                ELSE 5
            END
   \s""")
    List<Task> findByAssignedToUserId(Long userId);

    List<Task> getAllByCreatedBy(Long managerId);

    List<Task> getTaskByProject(Project project);

    List<Task> findTasksByDueDateBetweenAndDeletedFalse(LocalDateTime start, LocalDateTime end);
}
