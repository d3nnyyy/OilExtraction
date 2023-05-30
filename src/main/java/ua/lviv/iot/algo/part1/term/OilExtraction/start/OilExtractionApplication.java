package ua.lviv.iot.algo.part1.term.OilExtraction.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import ua.lviv.iot.algo.part1.term.OilExtraction.fileManagers.EntityReader;
import ua.lviv.iot.algo.part1.term.OilExtraction.models.Rig;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan("ua.lviv.iot.algo.part1.term.OilExtraction")
public class OilExtractionApplication {

	public static void main(String[] args) {
		System.out.println(EntityReader.getLastId(Rig.class));
		SpringApplication.run(OilExtractionApplication.class, args);
	}

}
