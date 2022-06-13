package com.spring.userslist.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
   
	@Autowired
	private ClientAllowed cors = new ClientAllowed();

    @Bean
    public WebMvcConfigurer getCorsConfiguration(){
        if (cors.isEnabled()) {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    CorsRegistration reg = registry.addMapping("/**")
                            .allowedMethods("GET", "POST", "PUT", "DELETE")
                            .allowedHeaders("*");

                    //add one or more endpoints
                    for(String url: cors.getOrigin().split(",")) {
                        url = url.replaceAll("\"","").replaceAll(" ","").replaceAll("\\{","").replaceAll("\\}","");
                        reg = reg.allowedOrigins(url);
                        System.out.println("within CorsConfig, adding allowed origin " + url);
                    }
                }
            };
        } else {
            return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
                System.out.println("within CorsConfig, cors settings disabled -allowing all");
                }
            };
        }
    }
}