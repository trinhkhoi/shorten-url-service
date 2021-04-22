package com.example.shortenurl.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: khoitd
 * Date: 2021-04-17 13:42
 * Description: this file defines the models of customer.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortenUrlHistoryDTO {
    private String longUrl;
    private String shortUrl;
}
