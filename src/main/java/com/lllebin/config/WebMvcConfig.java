package com.lllebin.config;

import com.lllebin.utils.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * ClassName: WebMvcConfig
 * Package: com.lllebin.config
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 设置静态资源映射（其实手动映射也行，有自动挡）
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/backend/**")
                .addResourceLocations("classpath:/static/backend/");
        registry.addResourceHandler("/front/**")
                .addResourceLocations("classpath:/static/front/");
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置底层对象转换器
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将该消息转换器对象追加到MVC框架转换器容器中(放第一个优点使用)
        converters.add(0, messageConverter);
    }
}
