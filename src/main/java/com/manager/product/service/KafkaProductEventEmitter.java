package com.manager.product.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.manager.product.dto.ProductResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProductEventEmitter {
    
    private final KafkaTemplate<String, ProductResponseDto> kafkaTemplate;

    public void sendMessage(String topic, String key, ProductResponseDto message){
        kafkaTemplate.send(topic, key, message);
    }

}
