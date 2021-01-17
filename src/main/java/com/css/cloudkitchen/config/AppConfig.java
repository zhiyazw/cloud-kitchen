package com.css.cloudkitchen.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * AppConfig
 * @author Joe Ding
 */
@Configuration
public class AppConfig {

    @Value("${thread.pool.size}")
    private int threadPoolSize;

    /**
     * Define the ThreadPollTaskSchedule Bean and set it's poll size
     * @return Return the ThreadPollTaskScheduler Bean
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(threadPoolSize);
        return scheduler;
    }
}
