package com.example.shortenurl.test;

/**
 * Author: khoitd
 * Date: 2021-04-19 14:09
 * Description:
 */

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * The class {@link ExampleDataSetLoader} allows us to use some 'non-static' values in datasets.
 */
public class ExampleDataSetLoader extends AbstractDataSetLoader {

    @Override
    protected IDataSet createDataSet(final Resource resource) throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        try (InputStream inputStream = resource.getInputStream()) {

            String fichier = new String(readFromStream(inputStream));

            LocalDateTime nowTime = LocalDateTime.now();
            fichier = fichier.replaceAll("\\[nowWithTime\\]", nowTime.format(ofPattern("yyyy-MM-dd HH:mm:ss")));
            return builder.build(new ByteArrayInputStream(fichier.getBytes()));
        }
    }

    /**
     * read data from and InputStream.
     *
     * @param is
     *            InputStream to read.
     * @return the byte[] read from the inputStream.
     */
    public static byte[] readFromStream(final InputStream is) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int lue = 0;
            byte[] tmp = new byte[1024 * 1024];
            while ((lue = is.read(tmp)) != -1) {
                baos.write(tmp, 0, lue);
            }
            return baos.toByteArray();
        } catch (IOException ioe) {
            return new byte[0];
        }
    }
}
