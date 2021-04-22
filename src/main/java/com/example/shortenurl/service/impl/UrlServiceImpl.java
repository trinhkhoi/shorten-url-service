package com.example.shortenurl.service.impl;

import com.example.shortenurl.common.AppProperties;
import com.example.shortenurl.common.exception.BusinessException;
import com.example.shortenurl.common.util.UrlConverter;
import com.example.shortenurl.dto.request.ShortenLinkRequest;
import com.example.shortenurl.dto.response.ShortenUrlHistoryDTO;
import com.example.shortenurl.entity.Customer;
import com.example.shortenurl.entity.CustomerUrl;
import com.example.shortenurl.entity.Url;
import com.example.shortenurl.repository.CustomerUrlRepository;
import com.example.shortenurl.repository.UrlRepository;
import com.example.shortenurl.service.UrlService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.stream.Collectors;

@Service
@Validated
public class UrlServiceImpl implements UrlService {
    private static final Logger logger = LoggerFactory.getLogger(UrlServiceImpl.class);

    @Autowired
    private CustomerUrlRepository customerUrlRepository;
    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private AppProperties appProperties;

    /**
     * Method for generate short url
     * @param customer
     * @param request
     * @return {@link String}
     * @throws BusinessException
     */
    @Override
    public String generateShortUrl(Customer customer, ShortenLinkRequest request) throws BusinessException {
        Url url = urlRepository.findByLongUrl(request.getLongUrl());
        if (url != null) {
            saveCustomerUrlIfNotExisted(url, customer);
            return url.getShortUrl();
        }

        Url newUrl = saveNewUrl(request.getLongUrl());
        saveCustomerUrlIfNotExisted(newUrl, customer);
        return newUrl.getShortUrl();
    }

    /**
     * Method for convert short url to long url
     * @param shortUrl
     * @return {@link String}
     * @throws BusinessException
     */
    @Override
    public String getLongUrlFromId(String shortUrl) throws BusinessException {
        String uniqueId = shortUrl.substring(shortUrl.lastIndexOf('/') + 1);
        logger.info("uniqueId: " + uniqueId);
        Long dictionaryKey = UrlConverter.getDictionaryKeyFromUniqueID(uniqueId);
        logger.info("dictionaryKey: " + dictionaryKey);
        String longUrl = urlRepository.findLongUrlById(dictionaryKey);

        if (StringUtils.isBlank(longUrl)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Cannot found any long url with the input");
        }
        return longUrl;
    }

    /**
     * Method for get list all short url by id customer
     * @param idCustomer
     * @return
     * @throws BusinessException
     */
    @Override
    public List<ShortenUrlHistoryDTO> getListShortenLinkByCustomer(Long idCustomer) throws BusinessException {
        List<Url> customerShortenUrls = urlRepository.findAllByIdCustomer(idCustomer);
        return customerShortenUrls.stream().map(url -> ShortenUrlHistoryDTO.builder()
                .longUrl(url.getLongUrl())
                .shortUrl(url.getShortUrl())
                .build()).collect(Collectors.toList());
    }

    /**
     * Save customer url if it was not existed
     * @param url
     * @param customer
     */
    private void saveCustomerUrlIfNotExisted(Url url, Customer customer) {
        if (customer != null) {
            CustomerUrl customerUrl = customerUrlRepository.findByIdCustomerAndIdUrl(customer.getId(), url.getId());
            if (customerUrl == null) {
                customerUrlRepository.save(CustomerUrl
                        .builder()
                        .idCustomer(customer.getId())
                        .idUrl(url.getId())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());
            }
        }
    }

    /**
     * Save new url to database
     * @param longUrl
     * @return {@link Url}
     */
    private Url saveNewUrl(String longUrl) {
        Long nexId = urlRepository.findMaxId() + 1;
        logger.info("nexId: " + nexId);
        String shortUrl = String.format(appProperties.getShortUrlDomain(), UrlConverter.createUniqueID(nexId));
        Url newUrl = Url
                .builder()
                .longUrl(longUrl)
                .shortUrl(shortUrl)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return urlRepository.save(newUrl);
    }
}
