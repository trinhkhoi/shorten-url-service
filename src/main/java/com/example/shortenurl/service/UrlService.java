package com.example.shortenurl.service;

import com.example.shortenurl.common.exception.BusinessException;
import com.example.shortenurl.dto.request.ShortenLinkRequest;
import com.example.shortenurl.dto.response.ShortenUrlHistoryDTO;
import com.example.shortenurl.entity.Customer;

import java.util.List;

public interface UrlService {

    String generateShortUrl(Customer customer, ShortenLinkRequest request) throws BusinessException;

    String getLongUrlFromId(String shortUrl) throws BusinessException;

    List<ShortenUrlHistoryDTO> getListShortenLinkByCustomer(Long idCustomer) throws BusinessException;
}
