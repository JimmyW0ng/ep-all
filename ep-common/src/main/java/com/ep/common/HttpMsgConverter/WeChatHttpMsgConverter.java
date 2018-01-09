package com.ep.common.HttpMsgConverter;

import com.google.common.collect.Lists;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

/**
 * 微信HttpMsgConverter
 */
public class WeChatHttpMsgConverter extends MappingJackson2HttpMessageConverter {

    public WeChatHttpMsgConverter() {
        List<MediaType> mediaTypes = Lists.newArrayList();
        mediaTypes.add(MediaType.TEXT_PLAIN);
        setSupportedMediaTypes(mediaTypes);
    }

}
