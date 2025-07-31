package org.example.userservice.controllers;

import org.example.userservice.dtos.TeamResponseDto;
import org.example.userservice.services.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/team/")
public class TeamController {

    private final TeamService teamService;


    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE', 'ADMIN')")
    @GetMapping("manager/{id}")
    private ResponseEntity<List<TeamResponseDto>> getUsers(@PathVariable long id) {

        return  ResponseEntity.ok(teamService.getTeamByManger(id));
    }



}
