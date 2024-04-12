package com.submit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class WebConf extends WebMvcConfigurationSupport {
    @Value("${file.upload.apk_info_url}")
    private String APK_PATH;

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream()
                // 过滤出StringHttpMessageConverter类型实例
                .filter(StringHttpMessageConverter.class::isInstance)
                .map(c -> (StringHttpMessageConverter) c)
                // 这里将转换器的默认编码设置为utf-8
                .forEach(c -> c.setDefaultCharset(StandardCharsets.UTF_8));
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //映射static路径的请求到static目录下
        // 静态资源访问路径和存放路径配置
        registry.addResourceHandler("/screenshot/**").addResourceLocations("file:" + APK_PATH + File.separator + "screenshot" + File.separator);
        registry.addResourceHandler("/icon/**").addResourceLocations("file:" + APK_PATH + File.separator + "icon" + File.separator);
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        // swagger访问配置
//        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/");
        //通过image访问本地的图片
    }

}