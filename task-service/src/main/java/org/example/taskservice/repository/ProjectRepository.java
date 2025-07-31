package org.example.taskservice.repository;


import org.example.taskservice.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
  
    List<Project> findByManager(Long managerId);

    @Query("SELECT p FROM Project p JOIN p.userIds u WHERE u = :userId")
    List<Project> findByUserId( @Param("userId") Long userId);

    Optional<Project> findById(Long projectId);

    void deleteById(Long id);

    boolean existsById(Long id);
}

