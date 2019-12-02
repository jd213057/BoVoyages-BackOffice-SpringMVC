package fr.gtm.backoffice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.gtm.backoffice.images.StorageService;

@SpringBootApplication
public class BovoyagesBackOfficeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BovoyagesBackOfficeApplication.class, args);}
	@Bean
	CommandLineRunner init(StorageService storageService) {
			return (args) -> {
				storageService.deleteAll();
				storageService.init();
			};
	}

}
