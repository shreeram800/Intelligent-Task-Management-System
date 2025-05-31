package org.example.userservice.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String role;
    private boolean isAuthenticated;
    private boolean isEmailVerified;
    private String phoneNumber;
    private String profilePictureUrl;
    private String status;
}
