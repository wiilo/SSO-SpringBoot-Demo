package com.wiilo.common.config;

import com.wiilo.common.interceptor.LoginStatusInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Web容器配置
 *
 * @author Whitlock Wang
 * @date 2022/8/10 17:46
 */
@Configuration
@EnableWebMvc
@ComponentScan
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 登录拦截
     */
    @Bean
    public LoginStatusInterceptor getLoginStatusInterceptor() {
        return new LoginStatusInterceptor();
    }

    public WebMvcConfig() {
        super();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //静态文件的拦截配置,项目暂无静态文件，忽略此配置
        registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/templates/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截规则：除了login，其他都拦截判断
        registry.addInterceptor(getLoginStatusInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/v1/confirmEmail/**", "/swagger-resources/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE")
                .allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Authorization")
                .allowCredentials(true);
    }

}
