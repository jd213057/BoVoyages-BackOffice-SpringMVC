package fr.gtm.backoffice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import fr.gtm.backoffice.images.StorageService;


/**
 * @author Erwan Soubeyrand, Denis Kuçuk, Jonathan Dimur.
 * @version 1.0
 * Classe BovoyagesBackOfficeApplication.
 */
@SpringBootApplication
public class BovoyagesBackOfficeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BovoyagesBackOfficeApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
			return (args) -> {
				storageService.deleteAll();
				storageService.init();
			};}
//	@Bean
//	public FilterRegistrationBean<ImageFilter> imageFilter(){
//	FilterRegistrationBean<ImageFilter> registrationBean = new FilterRegistrationBean<>();
//	registrationBean.setFilter(new ImageFilter());
//	registrationBean.addUrlPatterns("/images/*");
//	return registrationBean;		
//	}
	
//	@Bean
//	WebMvcConfigurerAdapter WebMvcConfigurer(ResourceHandlerRegistry registry) {
//		return new WebMvcConfigurerAdapter() {
//			@Override
//			public void addResourceHandler(...pathPatterns: "../static/*")
//			.addRessourceLocations("classpath:/static/");
//		}
//	};
//}
		
	
	
	
	
	
	
	

}
