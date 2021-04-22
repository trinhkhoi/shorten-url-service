package com.example.shortenurl.controller;

import com.example.shortenurl.common.exception.BusinessException;
import com.example.shortenurl.dto.request.CustomerLoginRequest;
import com.example.shortenurl.dto.request.CustomerRegisterRequest;
import com.example.shortenurl.dto.request.ShortenLinkRequest;
import com.example.shortenurl.dto.response.ResponseDTO;
import com.example.shortenurl.service.CustomerService;
import com.example.shortenurl.service.UrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@Validated
@RequestMapping("/public")
public class PublicController {
    private static final Logger logger = LoggerFactory.getLogger(PublicController.class);

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UrlService urlService;

    /**
     * API for register new customer
     * @param request
     * @throws BusinessException
     */
    @PostMapping("/customer/register")
    @ResponseStatus(value = OK)
    public void registerNewAccount(@RequestBody @Valid CustomerRegisterRequest request) throws BusinessException {
        customerService.registerNewAccount(request);
    }

    /**
     * API for login
     * @param customerLoginRequest
     * @return
     * @throws BusinessException
     */
    @PostMapping("/customer/login")
    @ResponseStatus(value = OK)
    public ResponseDTO<String> login(@Valid @RequestBody CustomerLoginRequest customerLoginRequest) throws BusinessException {
        ResponseDTO.ResponseBuilder<String> responseBuilder = new ResponseDTO.ResponseBuilder<>();
        return responseBuilder.data(customerService.login(customerLoginRequest)).build();
    }

    /**
     * API for revert long url
     * @param shortUrl
     * @return
     * @throws BusinessException
     */
    @GetMapping("/url/revert")
    @ResponseStatus(value = OK)
    public ResponseDTO<String> getRegisterCustomerByToken(@RequestParam String shortUrl) throws BusinessException {
        ResponseDTO.ResponseBuilder<String> responseBuilder = new ResponseDTO.ResponseBuilder<>();
        return responseBuilder.data(urlService.getLongUrlFromId(shortUrl)).build();
    }

    /**
     * API for generating short url
     * @param request
     * @return
     * @throws BusinessException
     */
    @PostMapping("/url/shorten")
    @ResponseStatus(value = OK)
    public ResponseDTO<String> generateShortenUrl(@RequestBody @Valid ShortenLinkRequest request) throws BusinessException {
        ResponseDTO.ResponseBuilder<String> responseBuilder = new ResponseDTO.ResponseBuilder<>();
        return responseBuilder.data(customerService.generateShortUrl(request)).build();
    }
}
