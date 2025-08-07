package org.example.userservice.controllers;

import org.example.userservice.dtos.TeamDTO;
import org.example.userservice.dtos.TeamResponseDto;
import org.example.userservice.entity.Team;
import org.example.userservice.services.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("{teamId}/user/{userId}")
    private void removeUsers(@PathVariable  Long userId, @PathVariable Long teamId) {
        teamService.removeUser(teamId, userId);
    }

    @GetMapping("{teamId}")
    private ResponseEntity<TeamDTO> getTeamById(@PathVariable long teamId) {
        return ResponseEntity.ok(teamService.getTeamById(teamId));
    }

}
