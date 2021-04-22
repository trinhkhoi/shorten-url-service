package com.example.shortenurl.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Author: khoitd
 * Date: 2021-04-17 13:42
 * Description: this file defines the models of customer.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "customer")
@Where(clause = "deleted_at IS NULL")
public class Customer extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @Column(name = "user_name", columnDefinition = "varchar(50)")
    private String userName;

    @Column(name = "password", columnDefinition = "varchar(255)")
    private String password;
}
