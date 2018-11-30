package com.haier.openplatform.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 解决Jackson 差8小时的问题
 * @author L.cm
 */
@Component("jacksonObjectMapper2")
public class JacksonObjectMapper2 extends ObjectMapper {

    private static final long serialVersionUID = 4288193147502386170L;

    private static final Locale CHINA = Locale.CHINA;
    
    public JacksonObjectMapper2() {
        this.setLocale(CHINA);
        this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", CHINA));
        this.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

}
