package fr.gtm.backoffice.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Erwan Soubeyrand, Denis Kuçuk, Jonathan Dimur.
 * @version 1.0
 * Classe WebConfigurer, serialisable.
 */
@Configuration
@EnableWebMvc
@ComponentScan
public class WebConfigurer implements WebMvcConfigurer {
    public static String uploadDirectory= System.getProperty("user.home") + "\\images";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("file:" + uploadDirectory+"\\");
    }
}