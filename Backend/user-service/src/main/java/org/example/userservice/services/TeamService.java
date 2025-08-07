package org.example.userservice.services;


import jakarta.transaction.Transactional;
import org.example.userservice.dtos.TeamDTO;
import org.example.userservice.dtos.TeamResponseDto;
import org.example.userservice.dtos.UserResponseDto;
import org.example.userservice.entity.Team;
import org.example.userservice.entity.User;
import org.example.userservice.repository.TeamRepository;

import org.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }


    public List<TeamResponseDto> getTeamByManger(Long managerId) {

        System.out.println(managerId);
        List<Team> teams = teamRepository.findByManager(userRepository.findById(managerId).orElseThrow());

        System.out.println(teams);
        return teams.stream().map(team -> {
            // Convert list of User entities to UserResponseDto
            List<UserResponseDto> userDtos = team.getUsers().stream().map(user -> UserResponseDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .role(user.getRole())
                    .isAuthenticated(user.isAuthenticated())
                    .isEmailVerified(user.isEmailVerified())
                    .phoneNumber(user.getPhoneNumber())
                    .profilePictureUrl(user.getProfilePictureUrl())
                    .status(user.getStatus())
                    .managerId(user.getManagerId() != null ? user.getManagerId() : null)
                    .teamId(team.getId())
                    .build()).toList();

            // Map manager to UserResponseDto
            UserResponseDto managerDto = null;
            if (team.getManager() != null) {
                var manager = team.getManager();
                managerDto = UserResponseDto.builder()
                        .id(manager.getId())
                        .firstName(manager.getFirstName())
                        .lastName(manager.getLastName())
                        .email(manager.getEmail())
                        .username(manager.getUsername())
                        .role(manager.getRole())
                        .isAuthenticated(manager.isAuthenticated())
                        .isEmailVerified(manager.isEmailVerified())
                        .phoneNumber(manager.getPhoneNumber())
                        .profilePictureUrl(manager.getProfilePictureUrl())
                        .status(manager.getStatus())
                        .managerId(manager.getManagerId() != null ? manager.getManagerId() : null)
                        .teamId(team.getId())
                        .build();
            }



            return TeamResponseDto.builder()
                    .id(team.getId())
                    .name(team.getName())
                    .description(team.getDescription())
                    .manager(managerDto)
                    .users(userDtos)
                    .build();
        }).toList();
    }


    public void removeUser(Long teamId, Long userId) {
        Team temp= teamRepository.getById(teamId);
        Set<User> users = temp.getUsers();
        users.stream().filter(user -> user.getId().equals(userId)).findFirst().ifPresent(user -> {});
        temp.setUsers(users);
        teamRepository.save(temp);
    }

    @Transactional
    public TeamDTO getTeamById(long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        TeamDTO dto = new TeamDTO();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setDescription(team.getDescription());

        dto.setManager(mapToResponseDto(team.getManager()));
        dto.setUsers(
                team.getUsers()
                        .stream()
                        .map(this::mapToResponseDto)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    private UserResponseDto mapToResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .isAuthenticated(user.isAuthenticated())
                .isEmailVerified(user.isEmailVerified())
                .phoneNumber(user.getPhoneNumber())
                .profilePictureUrl(user.getProfilePictureUrl()!=null ? user.getProfilePictureUrl() : null)
                .status(user.getStatus())
                .managerId(user.getManagerId())
                .teamId(user.getTeam().getId())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
