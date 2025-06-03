package config;

import job.WeatherJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail weatherJobDetail() {
        return JobBuilder.newJob(WeatherJob.class)
                .withIdentity("weatherJob", "weatherGroup")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger weatherJobTrigger(JobDetail weatherJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(weatherJobDetail)
                .withIdentity("weatherTrigger", "weatherGroup")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("0/10 * * * * ?")
                )
                .build();
    }
}