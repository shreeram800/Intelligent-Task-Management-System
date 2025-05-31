package org.example.userservice.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserResponseDto user;
}
