package com.example.shortenurl.test.service;

import com.example.shortenurl.common.exception.BusinessException;
import com.example.shortenurl.dto.request.CustomerLoginRequest;
import com.example.shortenurl.dto.request.CustomerRegisterRequest;
import com.example.shortenurl.entity.Customer;
import com.example.shortenurl.repository.CustomerRepository;
import com.example.shortenurl.service.CustomerService;
import com.example.shortenurl.test.GlobalTestCase;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: khoitd
 * Date: 2021-04-17 16:11
 * Description:
 */
@SpringBootTest
public class CustomerServiceTest extends GlobalTestCase {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void test_register_new_customer() throws Exception {
        String userName = "test";
        String password = "test";
        CustomerRegisterRequest registerRequest = CustomerRegisterRequest
                .builder()
                .userName(userName)
                .password(password)
                .build();
        customerService.registerNewAccount(registerRequest);

        Customer customer = customerRepository.findByUserName(userName);
        assertThat(customer).isNotNull();
        assertThat(customer.getUserName()).isEqualTo(userName);
    }

    @Test
    @DatabaseSetup(value = "/datasets/init_data_for_customer.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void test_login_with_correct_information() throws Exception {
        String userName = "test";
        String password = "test";
        String accessToken = customerService.login(CustomerLoginRequest
                .builder()
                .userName(userName)
                .password(password)
                .build());
        assertThat(accessToken).isNotBlank();
    }

    @Test
    public void test_login_with_incorrect_information() throws Exception {
        String userName = "test1";
        String password = "test";
        try {
            customerService.login(CustomerLoginRequest
                    .builder()
                    .userName(userName)
                    .password(password)
                    .build());
        } catch (Exception ex) {
            assertThat(ex instanceof BusinessException).isTrue();
            assertThat(ex.getMessage()).isEqualTo("The username or password is incorrect");
        }
    }

}
