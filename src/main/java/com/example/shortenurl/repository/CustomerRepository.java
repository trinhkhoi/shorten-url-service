package com.example.shortenurl.repository;

import com.example.shortenurl.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository of {@link Customer}
 * Date: 2021-04-18 08:07
 * @author khoitd
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByUserName(String userName);
}
