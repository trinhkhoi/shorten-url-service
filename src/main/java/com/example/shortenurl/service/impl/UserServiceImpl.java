package com.example.shortenurl.service.impl;

import com.example.shortenurl.entity.CustomUserDetails;
import com.example.shortenurl.entity.Customer;
import com.example.shortenurl.repository.CustomerRepository;
import com.example.shortenurl.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        Customer customer = customerRepository.findByUserName(userName);
        if (customer == null) {
            throw new UsernameNotFoundException(userName);
        }
        return new CustomUserDetails(customer);
    }
}
