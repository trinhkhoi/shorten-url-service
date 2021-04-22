package com.example.shortenurl.test.controller;

import com.example.shortenurl.common.util.JSon;
import com.example.shortenurl.dto.request.CustomerLoginRequest;
import com.example.shortenurl.dto.request.CustomerRegisterRequest;
import com.example.shortenurl.dto.request.ShortenLinkRequest;
import com.example.shortenurl.entity.Customer;
import com.example.shortenurl.repository.CustomerRepository;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.fail;


@SpringBootTest
public class PublicControllerTest extends GlobalTestCase {

    @Autowired
    private CustomerRepository customerRepository;

    private String testLongUrl = "https://example.app/test/content";
    private String testUserName = "test";
    private String testPassword = "test";

    @Test
    public void test_that_short_new_url_success() {

        try {
            MockHttpServletResponse response = shortingNewUrl(testLongUrl);
            JSONObject json = new JSONObject(response.getContentAsString());
            String data = json.getString("data");
            Assertions.assertThat(data).startsWith("http://localhost:8080/public/url/");
        } catch (Exception e) {
            fail("No exception was expected here " + e.getMessage());
        }
    }

    @Test
    public void test_that_short_empty_url_fail() {
        ShortenLinkRequest shortenLinkRequest = ShortenLinkRequest
                .builder()
                .longUrl(StringUtils.EMPTY)
                .build();
        try {
            MockHttpServletResponse response = getMockMvc().perform(post("/public/url/shorten").content(JSon.toJson(shortenLinkRequest)).contentType(APPLICATION_JSON))
                    .andExpect(status().is4xxClientError()).andReturn().getResponse();
            JSONObject json = new JSONObject(response.getContentAsString());
            String data = json.getString("message");
            Assertions.assertThat(data).containsIgnoringCase("Validation failed for argument");
        } catch (Exception e) {
            fail("No exception was expected here " + e.getMessage());
        }
    }

    @Test
    public void test_that_short_incorrect_format_url_fail() {
        ShortenLinkRequest shortenLinkRequest = ShortenLinkRequest
                .builder()
                .longUrl("http:///test/test")
                .build();
        try {
            MockHttpServletResponse response = getMockMvc().perform(post("/public/url/shorten").content(JSon.toJson(shortenLinkRequest)).contentType(APPLICATION_JSON))
                    .andExpect(status().is4xxClientError()).andReturn().getResponse();
            JSONObject json = new JSONObject(response.getContentAsString());
            String data = json.getString("message");
            Assertions.assertThat(data).containsIgnoringCase("the input is incorrect format");
        } catch (Exception e) {
            fail("No exception was expected here " + e.getMessage());
        }
    }

    @Test
    public void test_that_convert_short_url_success() {
        try {
            MockHttpServletResponse shortUrlResponse = shortingNewUrl(testLongUrl);
            JSONObject jsonShortUrl = new JSONObject(shortUrlResponse.getContentAsString());
            String shortUrl = jsonShortUrl.getString("data");

            MockHttpServletResponse longUrlResponse = getMockMvc().perform(get("/public/url/revert").param("shortUrl", shortUrl))
                    .andExpect(status().is2xxSuccessful()).andReturn().getResponse();
            JSONObject jsonLongUrl = new JSONObject(longUrlResponse.getContentAsString());
            String data = jsonLongUrl.getString("data");
            Assertions.assertThat(data).isEqualTo(testLongUrl);
        } catch (Exception e) {
            fail("No exception was expected here " + e.getMessage());
        }
    }

    @Test
    public void test_that_convert_invalid_short_url_fail() {
        try {
            MockHttpServletResponse longUrlResponse = getMockMvc().perform(get("/public/url/revert").param("shortUrl", "https://test.test/test"))
                    .andExpect(status().is4xxClientError()).andReturn().getResponse();
            JSONObject jsonLongUrl = new JSONObject(longUrlResponse.getContentAsString());
            String longUrl = jsonLongUrl.getString("message");
            Assertions.assertThat(longUrl).isEqualTo("Cannot found any long url with the input");
        } catch (Exception e) {
            fail("No exception was expected here " + e.getMessage());
        }
    }

    @Test
    public void test_create_new_customer_success() {
        try {
            MockHttpServletResponse registerResponse = createNewCustomer();
            Customer customer = customerRepository.findByUserName(testUserName);
            Assertions.assertThat(customer).isNotNull();
            Assertions.assertThat(customer.getUserName()).isEqualTo(testUserName);
        } catch (Exception e) {
            fail("No exception was expected here " + e.getMessage());
        }
    }

    @Test
    public void test_login_success() {
        try {
            MockHttpServletResponse registerResponse = createNewCustomer();

             CustomerLoginRequest loginRequest = CustomerLoginRequest
                    .builder()
                    .userName(testUserName)
                    .password(testPassword)
                    .build();
            MockHttpServletResponse response = getMockMvc().perform(post("/public/customer/login").content(JSon.toJson(loginRequest)).contentType(APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful()).andReturn().getResponse();
            Assertions.assertThat(response).isNotNull();
            JSONObject json = new JSONObject(response.getContentAsString());
            String accessToken = json.getString("data");
            Assertions.assertThat(accessToken).isNotEmpty();
        } catch (Exception e) {
            fail("No exception was expected here " + e.getMessage());
        }
    }

    private MockHttpServletResponse shortingNewUrl(String longUrl) throws Exception {
        ShortenLinkRequest shortenLinkRequest = ShortenLinkRequest
                .builder()
                .longUrl(longUrl)
                .build();
        MockHttpServletResponse response = getMockMvc().perform(post("/public/url/shorten").content(JSon.toJson(shortenLinkRequest)).contentType(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn().getResponse();
        return response;
    }

    private MockHttpServletResponse createNewCustomer() throws Exception {
        CustomerRegisterRequest registerRequest = CustomerRegisterRequest
                .builder()
                .userName(testUserName)
                .password(testPassword)
                .build();
        MockHttpServletResponse response = getMockMvc().perform(post("/public/customer/register").content(JSon.toJson(registerRequest)).contentType(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn().getResponse();
        return response;
    }
}
