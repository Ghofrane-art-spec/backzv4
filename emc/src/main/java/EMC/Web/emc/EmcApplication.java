package EMC.Web.emc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import EMC.Web.emc.shared.FileStorageProperties;




@EnableConfigurationProperties({
    FileStorageProperties.class
})
@SpringBootApplication
public class EmcApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmcApplication.class, args);
	}
	
	}


