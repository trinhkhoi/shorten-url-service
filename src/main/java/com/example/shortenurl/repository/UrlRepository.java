package com.example.shortenurl.repository;

import com.example.shortenurl.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository of {@link Url}
 * Date: 2021-04-18 10:05
 * @author khoitd
 */
@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    Url findByLongUrl(String longUrl);

    @Query(nativeQuery = true, value = "select COALESCE(max(id), 0) from url")
    Long findMaxId();

    @Query(nativeQuery = true, value = "select long_url from url where id = ?1")
    String findLongUrlById(Long id);

    @Query(nativeQuery = true, value = "select u.* from url u inner join customer_url cu on u.id = cu.id_url and cu.id_customer = ?1 where u.deleted_at is null order by cu.created_at desc")
    List<Url> findAllByIdCustomer(Long idCustomer);

    @Query(nativeQuery = true, value = "select count(1) from url where long_url = ?1")
    int countNumberUniqueUrl(String longUrl);
}
