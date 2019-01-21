package io.ztz.simple.mq.client;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import io.ztz.simple.mq.client.api.Consumer;
import io.ztz.simple.mq.client.api.Producer;
import io.ztz.simple.mq.client.api.impl.ConsumerImpl;
import io.ztz.simple.mq.client.api.impl.ProducerImpl;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@PropertySource("classpath:simple-mq.properties")
public class SimpleMQClientConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
	
	@Bean
	public Producer producer() {
		return new ProducerImpl();
	}
	
	@Bean
	public Consumer consumer() {
		return new ConsumerImpl();
	}
	
}
