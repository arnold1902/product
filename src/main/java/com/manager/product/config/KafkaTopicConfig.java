package com.manager.product.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    
    @Bean
    public NewTopic productCreatedTopic() {
        return TopicBuilder.name("product.created")
                .partitions(3)
                .replicas(1) // 3 copies de chaque partitions doit toujours etre egal au nombre de broker
                .compact()
                .build();
    }

    @Bean
    public NewTopic productUpdatedTopic() {
        return TopicBuilder.name("product.updated")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic productDeletedTopic() {
        return TopicBuilder.name("product.deleted")
                .partitions(3)
                .replicas(1)
                .build();
    }

}
