package com.example.shortenurl.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRegisterRequest {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
}
