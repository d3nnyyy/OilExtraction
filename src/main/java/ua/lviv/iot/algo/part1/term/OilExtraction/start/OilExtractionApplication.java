package ua.lviv.iot.algo.part1.term.OilExtraction.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan("ua.lviv.iot.algo.part1.term.OilExtraction")
public class OilExtractionApplication {

	public static void main(String[] args) {
		SpringApplication.run(OilExtractionApplication.class, args);
	}

}
