package com.example.shortenurl.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Author: khoitd
 * Date: 2021-04-17 13:19
 * Description: this file defines the models of customer url.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "customer_url")
@Where(clause = "deleted_at IS NULL")
public class CustomerUrl extends BaseEntity {

    @Column(name = "id_customer", columnDefinition = "bigint")
    private Long idCustomer;

    @Column(name = "id_url", columnDefinition = "bigint")
    private Long idUrl;
}
