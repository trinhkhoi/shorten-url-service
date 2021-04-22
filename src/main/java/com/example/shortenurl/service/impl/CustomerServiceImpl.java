package com.example.shortenurl.service.impl;

import com.example.shortenurl.common.exception.BusinessException;
import com.example.shortenurl.common.util.JwtUtil;
import com.example.shortenurl.dto.request.CustomerLoginRequest;
import com.example.shortenurl.dto.request.CustomerRegisterRequest;
import com.example.shortenurl.dto.request.ShortenLinkRequest;
import com.example.shortenurl.dto.response.ShortenUrlHistoryDTO;
import com.example.shortenurl.entity.CustomUserDetails;
import com.example.shortenurl.entity.Customer;
import com.example.shortenurl.repository.CustomerRepository;
import com.example.shortenurl.service.CustomerService;
import com.example.shortenurl.service.UrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
public class CustomerServiceImpl implements CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UrlService urlService;

    /**
     * Register new account service
     * @param request
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerNewAccount(@Valid @NotNull CustomerRegisterRequest request) throws BusinessException {
        Customer existedCustomer = customerRepository.findByUserName(request.getUserName());
        if (existedCustomer != null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "This user was already registered");
        }

        Customer newCustomer = Customer
                .builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        customerRepository.save(newCustomer);
    }

    /**
     * Login service
     * @param request
     * @return {@link String}
     * @throws BusinessException
     */
    @Override
    public String login(@Valid @NotNull CustomerLoginRequest request) throws BusinessException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserName(),
                            request.getPassword()
                    )
            );

            CustomUserDetails detail = (CustomUserDetails) authentication.getPrincipal();
            // Nếu không xảy ra exception tức là thông tin hợp lệ
            // Set thông tin authentication vào Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Trả về jwt cho người dùng.
            Customer customer = detail.getCustomer();
            String accessToken = JwtUtil.generateTokenForUser(customer.getId());
            return accessToken;
        } catch (BadCredentialsException ex) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "The username or password is incorrect");
        }
    }

    /**
     * Get customer by id
     * @param idCustomer
     * @return {@link CustomUserDetails}
     * @throws BusinessException
     */
    @Override
    public CustomUserDetails findById(Long idCustomer) throws BusinessException {
        Customer customer = customerRepository.findById(idCustomer).orElse(null);
        if (customer == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "The customer does not exist");
        }
        return CustomUserDetails.builder().customer(customer).build();
    }

    /**
     * Get customer information from session
     * @return {@link Customer}
     * @throws BusinessException
     */
    @Override
    public Customer getCustomerCurrentSession() throws BusinessException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer temp = null;
        if (principal instanceof CustomUserDetails) {
            temp = ((CustomUserDetails) principal).getCustomer();
        }
        return temp;
    }

    /**
     * Get all customer history short links
     * @return {@link List {@link ShortenUrlHistoryDTO }}
     * @throws BusinessException
     */
    @Override
    public List<ShortenUrlHistoryDTO> getListShortenUrl() throws BusinessException {
        Customer customer = getCustomerCurrentSession();
        if (customer == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Customer was not found");
        }
        return urlService.getListShortenLinkByCustomer(customer.getId());
    }

    /**
     * Method for generating short url
     * @param request
     * @return {@link String}
     * @throws BusinessException
     */
    @Override
    public String generateShortUrl(ShortenLinkRequest request) throws BusinessException {
        Customer customer = getCustomerCurrentSession();
        return urlService.generateShortUrl(customer, request);
    }
}
