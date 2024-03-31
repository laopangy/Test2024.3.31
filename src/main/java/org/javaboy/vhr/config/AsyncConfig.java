package org.javaboy.vhr.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class AsyncConfig{

    @Value(value="${mytask.execution.pool.core-size}")
    private String CORE_SIZE;

    @Value(value="${mytask.execution.pool.max-size}")
    private String MAX_SIZE;

    @Value("${mytask.execution.pool.queue-capacity}")
    private String QUEUE_SIZE;

    @Value("${mytask.execution.thread-name-prefix}")
    private String THREAD_NAME_PREFIX;

    @Value("${mytask.execution.pool.keep-alive}")
    private int KEEP_ALIVE;

    @Bean("messageServiceExecutor")
    public Executor messageServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(Integer.parseInt(CORE_SIZE));
        //配置最大线程数
        executor.setMaxPoolSize(Integer.parseInt(MAX_SIZE));
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //配置队列大小
        executor.setQueueCapacity(Integer.parseInt(QUEUE_SIZE));
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

}


