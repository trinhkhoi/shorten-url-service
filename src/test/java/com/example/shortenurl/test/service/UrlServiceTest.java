package com.example.shortenurl.test.service;

import com.example.shortenurl.dto.response.ShortenUrlHistoryDTO;
import com.example.shortenurl.entity.Customer;
import com.example.shortenurl.entity.CustomerUrl;
import com.example.shortenurl.repository.CustomerUrlRepository;
import com.example.shortenurl.service.UrlService;
import com.example.shortenurl.test.GlobalTestCase;
import com.example.shortenurl.dto.request.ShortenLinkRequest;
import com.example.shortenurl.entity.Url;
import com.example.shortenurl.repository.UrlRepository;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.fail;

/**
 * Author: khoitd
 * Date: 2021-04-17 15:15
 * Description:
 */
@SpringBootTest
public class UrlServiceTest extends GlobalTestCase {

    @Autowired
    private UrlService urlService;
    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private CustomerUrlRepository customerUrlRepository;

    @Test
    public void test_generateShortUrl_with_null_customer() {
        try {
            String testUrl = "https://example.app/test/content";
            ShortenLinkRequest request = ShortenLinkRequest.builder().longUrl(testUrl).build();
            String shortUrl = urlService.generateShortUrl(null, request);
            assertThat(shortUrl).isNotBlank();
            Url url = urlRepository.findByLongUrl(testUrl);
            System.out.println(shortUrl);
            assertThat(url).isNotNull();
            assertThat(url.getLongUrl()).isEqualTo(testUrl);
            assertThat(url.getShortUrl()).isEqualTo(shortUrl);
        } catch (Exception e) {
            fail("No exception was expected here " + e.getMessage());
        }
    }

    @Test
    public void test_generateShortUrl_with_customer() {
        try {
            String testUrl = "https://example.app/test/content";
            Long idCustomer = 1L;
            ShortenLinkRequest request = ShortenLinkRequest.builder().longUrl(testUrl).build();
            Customer customer = Customer.builder().id(1L).build();
            String shortUrl = urlService.generateShortUrl(customer, request);
            assertThat(shortUrl).isNotBlank();
            Url url = urlRepository.findByLongUrl(testUrl);
            System.out.println(shortUrl);
            assertThat(url).isNotNull();
            assertThat(url.getLongUrl()).isEqualTo(testUrl);
            assertThat(url.getShortUrl()).isEqualTo(shortUrl);

            CustomerUrl customerUrl = customerUrlRepository.findByIdCustomerAndIdUrl(idCustomer, url.getId());
            assertThat(customerUrl).isNotNull();
        } catch (Exception e) {
            fail("No exception was expected here " + e.getMessage());
        }
    }

    @Test
    @DatabaseSetup(value = "/datasets/generate_existed_url.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void test_generateShortUrl_with_existed_long_url() {
        try {
            String testUrl = "https://example.app/test/content";
            String expectedShortUrl = "http://localhost:8080/public/url/b";
            ShortenLinkRequest request = ShortenLinkRequest.builder().longUrl(testUrl).build();
            String shortUrl = urlService.generateShortUrl(null, request);
            assertThat(shortUrl).isNotBlank();
            assertThat(shortUrl).isEqualTo(expectedShortUrl);

            int numberExistedUrl = urlRepository.countNumberUniqueUrl(testUrl);
            assertThat(numberExistedUrl).isEqualTo(1);
        } catch (Exception e) {
            fail("No exception was expected here " + e.getMessage());
        }
    }

    @Test
    @DatabaseSetup(value = "/datasets/generate_existed_url.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void test_getLongUrlFromId() {
        try {
            String uniqueId = "http://localhost:8080/public/url/b";
            String expectedLongUrl = "https://example.app/test/content";
            String longUrl = urlService.getLongUrlFromId(uniqueId);
            assertThat(longUrl).isNotBlank();
            assertThat(longUrl).isEqualTo(expectedLongUrl);
        } catch (Exception e) {
            fail("No exception was expected here " + e.getMessage());
        }
    }

    @Test
    @DatabaseSetup(value = "/datasets/generate_existed_url.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void test_getListShortenLinkByCustomer() throws Exception {
        try {
            Long idCustomer = 1L;
            List<ShortenUrlHistoryDTO> shortenLinkHistorys = urlService.getListShortenLinkByCustomer(idCustomer);
            assertThat(shortenLinkHistorys.size()).isEqualTo(2);
        } catch (Exception e) {
            fail("No exception was expected here " + e.getMessage());
        }
    }
}
