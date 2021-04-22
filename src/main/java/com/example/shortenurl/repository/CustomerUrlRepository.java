package com.example.shortenurl.repository;

import com.example.shortenurl.entity.CustomerUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository of {@link CustomerUrl}
 * Date: 2021-04-18 08:05
 * @author khoitd
 */
@Repository
public interface CustomerUrlRepository extends JpaRepository<CustomerUrl, Long> {

    CustomerUrl findByIdCustomerAndIdUrl(Long idCustomer, Long idUrl);
}
