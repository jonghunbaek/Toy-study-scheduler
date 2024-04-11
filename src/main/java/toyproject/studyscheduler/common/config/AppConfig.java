package toyproject.studyscheduler.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import toyproject.studyscheduler.common.formatter.LocalDateFormatter;

@Configuration
public class AppConfig {

    @Bean
    public LocalDateFormatter localDateFormatter() {
        return new LocalDateFormatter();
    }
}
