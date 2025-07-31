package org.example.userservice.repository;

import lombok.extern.java.Log;
import org.example.userservice.entity.Team;
import org.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Log> {


    Team getById(Long teamId);

    List<Team> findByManager(User manager);
}
