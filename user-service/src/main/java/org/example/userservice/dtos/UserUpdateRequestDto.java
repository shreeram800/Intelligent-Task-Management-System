package org.example.userservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequestDto {

    private String firstName;
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    private String phoneNumber;
    private String profilePictureUrl;
}
