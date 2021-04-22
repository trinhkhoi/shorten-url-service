package com.example.shortenurl.common.util;

import com.example.shortenurl.config.CustomLocaleContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author khoitd
 * Date: 2021-04-17 10:07
 * Description:
 */
public class MessageUtil {
    private static Logger         logger         = LoggerFactory.getLogger(MessageUtil.class);
    private static final String   BUNDLE_NAME    = "message/message";

    private MessageUtil() {
    }

    /**
     * Get message by key and params
     * @param key
     * @param arguments
     * @return
     */
    public static String get(final String key, final Object... arguments) {
        String value = getResourceBundle().getString(key);
        value = new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        return arguments == null || arguments.length == 0 ? value : MessageFormat.format(value, arguments);
    }

    private static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle(BUNDLE_NAME, CustomLocaleContextHolder.getLocale());
    }
}
