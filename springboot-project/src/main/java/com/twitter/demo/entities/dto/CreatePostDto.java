package com.twitter.demo.entities.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreatePostDto {

    @NotBlank
    @NotNull
    private String content;
}
