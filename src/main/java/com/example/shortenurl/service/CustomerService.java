package com.example.shortenurl.service;

import com.example.shortenurl.dto.request.*;
import com.example.shortenurl.dto.response.ShortenUrlHistoryDTO;
import com.example.shortenurl.entity.CustomUserDetails;
import com.example.shortenurl.entity.Customer;
import com.example.shortenurl.common.exception.BusinessException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface CustomerService {

    String login(@Valid @NotNull CustomerLoginRequest request) throws BusinessException;

    void registerNewAccount(@Valid @NotNull CustomerRegisterRequest request) throws BusinessException;

    CustomUserDetails findById(Long idCustomer) throws BusinessException;

    Customer getCustomerCurrentSession() throws BusinessException;

    List<ShortenUrlHistoryDTO> getListShortenUrl() throws BusinessException;

    String generateShortUrl(ShortenLinkRequest request) throws BusinessException;
}

