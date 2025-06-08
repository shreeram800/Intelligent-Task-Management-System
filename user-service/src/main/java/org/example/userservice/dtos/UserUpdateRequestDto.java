package org.example.userservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class UserUpdateRequestDto {

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email cannot exceed 50 characters")
    private String email;

    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must be at least 8 characters with at least one letter, one number and one special character")
    private String password;

    @Pattern(regexp = "^(ADMIN|MANAGER|EMPLOYEE)$", message = "Role must be ADMIN, MANAGER or EMPLOYEE")
    private String role;

    private Boolean isEmailVerified;

    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$",
            message = "Invalid phone number format")
    private String phoneNumber;

    private String profilePictureUrl;

    @Pattern(regexp = "^(ACTIVE|INACTIVE|BLOCKED)$", message = "Status must be ACTIVE, INACTIVE or BLOCKED")
    private String status;

    private Long managerId;
}