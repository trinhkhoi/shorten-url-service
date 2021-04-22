package com.example.shortenurl.test;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: khoitd
 * Date: 2021-04-17 14:32
 * Description:
 */
@Service
@Profile("dev")
public class TruncateDatabaseService {

    @Autowired
    private EntityManager entityManager;

    public TruncateDatabaseService() {
        super();
    }

    @Transactional
    public void truncate() throws Exception {
        Session session = entityManager.unwrap(Session.class);

        List<String> tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Table.class) != null)
                .map(e -> e.getJavaType().getAnnotation(Table.class).name())
                .collect(Collectors.toList());

        entityManager.flush();
        entityManager.createNativeQuery("SET @@foreign_key_checks = 0").executeUpdate();
        tableNames.forEach(tableName -> entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate());
        entityManager.createNativeQuery("SET @@foreign_key_checks = 1").executeUpdate();
    }
}
