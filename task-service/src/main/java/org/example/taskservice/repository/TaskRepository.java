package org.example.taskservice.repository;


import org.example.taskservice.entity.Project;
import org.example.taskservice.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findByAssignedToUserIdAndDeletedFalse(Long userId);

    List<Task> findByAssignedToUserId(Long userId);

    List<Task> getAllByCreatedBy(Long managerId);

    List<Task> getTaskByProject(Project project);
}
