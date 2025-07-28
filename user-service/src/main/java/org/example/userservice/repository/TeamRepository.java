package org.example.userservice.repository;

import lombok.extern.java.Log;
import org.example.userservice.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Log> {
    Team getById(Long teamId);
}
