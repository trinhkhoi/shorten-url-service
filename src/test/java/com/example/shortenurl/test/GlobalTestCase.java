package com.example.shortenurl.test;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.junit.After;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.junit.Assert.fail;

/**
 * Author: khoitd
 * Date: 2021-04-17 15:22
 * Description:
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ExampleDataSetLoader.class)
public class GlobalTestCase {

    @Autowired
    private TruncateDatabaseService truncateDatabaseService;

    @After
    public void setUp() {
        try {
			truncateDatabaseService.truncate();
        } catch (Exception e) {
            fail("error " + e.getMessage());
        }
    }
}
