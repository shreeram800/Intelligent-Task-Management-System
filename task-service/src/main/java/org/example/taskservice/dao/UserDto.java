package org.example.taskservice.dao;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String phoneNumber;
    private String role;
    private String profilePictureUrl;
    private boolean isAuthenticated;
    private boolean isEmailVerified;
    private String status;
    private Long managerId;
    private boolean deleted;

}
