package org.example.taskservice.repository;


import org.example.taskservice.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findByAssignedToUserIdAndDeletedFalse(Long userId);

    List<Task> findByCreatedByAndDeletedFalse(Long userId);

    List<Task> findByAssignedToUserId(Long userId);

    List<Task> getAllByCreatedBy(Long managerId);


    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.attachmentList WHERE t.id = :taskId")
    Optional<Task> findByIdWithAttachments(@Param("taskId") Long taskId);

}
