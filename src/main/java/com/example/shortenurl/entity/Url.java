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
 * Description: this file defines the models of url.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "url")
@Where(clause = "deleted_at IS NULL")
public class Url extends BaseEntity {

    @Column(name = "long_url", columnDefinition = "text")
    private String longUrl;

    @Column(name = "short_url", columnDefinition = "varchar(255)")
    private String shortUrl;
}
