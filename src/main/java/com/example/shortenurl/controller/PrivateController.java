package com.example.shortenurl.controller;

import com.example.shortenurl.common.exception.BusinessException;
import com.example.shortenurl.dto.request.ShortenLinkRequest;
import com.example.shortenurl.dto.response.ResponseDTO;
import com.example.shortenurl.dto.response.ShortenUrlHistoryDTO;
import com.example.shortenurl.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@Validated
@RequestMapping("/private")
public class PrivateController {

    @Autowired
    private CustomerService customerService;

    /**
     * API for get all customer history short links
     * @return
     * @throws BusinessException
     */
    @GetMapping("/url/shorten/histories")
    @ResponseStatus(value = OK)
    public ResponseDTO<List<ShortenUrlHistoryDTO>> getListHistoryShortenUrls() throws BusinessException {
        ResponseDTO.ResponseBuilder<List<ShortenUrlHistoryDTO>> responseBuilder = new ResponseDTO.ResponseBuilder<>();
        return responseBuilder.data(customerService.getListShortenUrl()).build();
    }

    /**
     * API for generating short url
     * @param request
     * @return
     * @throws BusinessException
     */
    @PostMapping("/url/shorten")
    @ResponseStatus(value = OK)
    public ResponseDTO<String> shortenUrl(@RequestBody @Valid ShortenLinkRequest request) throws BusinessException {
        ResponseDTO.ResponseBuilder<String> responseBuilder = new ResponseDTO.ResponseBuilder<>();
        return responseBuilder.data(customerService.generateShortUrl(request)).build();
    }

    /**
     * API for check customer authentication
     * @return
     */
    @GetMapping("/authen/verify")
    @ResponseStatus(value = OK)
    public ResponseDTO<Boolean> verifyAuthentication() {
        ResponseDTO.ResponseBuilder<Boolean> responseBuilder = new ResponseDTO.ResponseBuilder<>();
        return responseBuilder.data(Boolean.TRUE).build();
    }
}
