package weather_analysis_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"aspect","client","job","config","controller","service","dto"})
public class WeatherAnalysisServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherAnalysisServiceApplication.class, args);
	}

}
