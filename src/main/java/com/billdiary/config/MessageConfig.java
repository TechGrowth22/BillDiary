package com.billdiary.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageConfig {

    @Autowired
    MessageSource messageSource;

    public String getMessage(String key) {
        return messageSource.getMessage(key,null, Locale.ENGLISH);
    }

    public String getMessage(String key, String... args) {
        return messageSource.getMessage(key, args, Locale.ENGLISH);
    }
}
