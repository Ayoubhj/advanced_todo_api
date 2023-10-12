package com.todo.todoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodoRequest {

    @NotBlank
    @NotNull
    @Size(min = 3 , max = 255)
    private String title;
    @NotBlank
    @NotNull
    private String descreption;

}
