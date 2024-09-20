package com.project.BloggingApp.dto.user_dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Update User")
public class UpdateUserDTO {

    private String email;
    @NotEmpty
    private String password;
    private String bio;
}
