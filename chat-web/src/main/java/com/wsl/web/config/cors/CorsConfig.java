package com.wsl.web.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
/**
 * 跨域设置
 * @author WangShilei
 * @date 2020/12/1-19:03
 **/

@Configuration
public class CorsConfig {
    private CorsConfiguration corsConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:8000");
        corsConfiguration.addAllowedOrigin("http://oss.wslhome.top");
        corsConfiguration.addAllowedOrigin("http://admin.wslhome.top");
        corsConfiguration.addAllowedOrigin("http://www.wslhome.top");
        corsConfiguration.addAllowedOrigin("http://test.wslhome.top");
        corsConfiguration.addAllowedOrigin("http://kill.wslhome.top");
        corsConfiguration.addAllowedOrigin("https://oss.wslhome.top");
        corsConfiguration.addAllowedOrigin("https://admin.wslhome.top");
        corsConfiguration.addAllowedOrigin("https://www.wslhome.top");
        corsConfiguration.addAllowedOrigin("https://test.wslhome.top");
        corsConfiguration.addAllowedOrigin("https://kill.wslhome.top");
        corsConfiguration.addAllowedOrigin("https://chat.wslhome.top");
        corsConfiguration.addAllowedOrigin("http://chat.wslhome.top");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);
        return corsConfiguration;
    }
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig());
        return new CorsFilter(source);
    }
}
