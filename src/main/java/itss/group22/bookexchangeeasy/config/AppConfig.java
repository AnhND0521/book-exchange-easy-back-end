package itss.group22.bookexchangeeasy.config;

import com.cloudinary.Cloudinary;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(16);  // Adjust core pool size as needed
        executor.setMaxPoolSize(50);   // Adjust max pool size as needed
        executor.setQueueCapacity(200); // Adjust queue capacity as needed
        executor.setThreadNamePrefix("async-task-");
        executor.initialize();
        return executor;
    }
    @Bean
    public Cloudinary getCloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", "dffwgt5wu"); // Replace with your cloud name
        config.put("api_key", "324141127131466");   // Replace with your API key
        config.put("api_secret", "s8hapeYK6Es2jyzbHaAOVW7r68o"); // Replace with your API secret
        config.put("secure", false);
        return new Cloudinary(config);
    }
}
