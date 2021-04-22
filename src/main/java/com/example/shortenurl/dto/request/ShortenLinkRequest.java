package com.example.shortenurl.dto.request;

import com.example.shortenurl.common.validation.HttpConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortenLinkRequest {
    @NotBlank
    @HttpConstraint(message = "the input is incorrect format")
    private String longUrl;
}
