package com.manager.product.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    
    private static final String RETENTION_MS = "retention.ms";
    private static final String RETENTION_BYTE = "retention.byte";
    private static final String TIME_DELAY = "86400000";
    private static final String MAX_SIZE = "536870912";

    @Bean
    public NewTopic productCreatedTopic() {
        return TopicBuilder.name(KafkaTopics.PRODUCT_CREATED.getTopicName())
                .partitions(3)
                .replicas(1) // 3 copies de chaque partitions doit toujours etre egal au nombre de broker
                .config(RETENTION_MS, TIME_DELAY) // Les messages ont une duree de 1jour
                .config(RETENTION_BYTE, MAX_SIZE) // Au bout de 500Mo on supprime tout les messages
                // .compact() // les messages de meme cle s'ecrasent les un des autres
                .build();
    }

    @Bean
    NewTopic productUpdatedTopic() {
        return TopicBuilder.name(KafkaTopics.PRODUCT_UPDATED.getTopicName())
                .partitions(3)
                .replicas(1)
                .config(RETENTION_MS, TIME_DELAY) // Les messages ont une duree de 1jour
                .config(RETENTION_BYTE, MAX_SIZE) // Au bout de 500Mo on supprime tout les messages
                .build();
    }

    @Bean
    public NewTopic productDeletedTopic() {
        return TopicBuilder.name(KafkaTopics.PRODUCT_DELETED.getTopicName())
                .partitions(3)
                .replicas(1)
                .config(RETENTION_MS, TIME_DELAY) // Les messages ont une duree de 1jour
                .config(RETENTION_BYTE, MAX_SIZE) // Au bout de 500Mo on supprime tout les messages
                .build();
    }

}
