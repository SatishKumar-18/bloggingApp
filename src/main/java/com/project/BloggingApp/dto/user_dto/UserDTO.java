package com.project.BloggingApp.dto.user_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Signup")
public class UserDTO {
    @NotEmpty
    private String username;
    private String email;
    @NotEmpty
    private String password;
}
