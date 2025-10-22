package com.manager.product.config;

public enum KafkaTopics {
    
    PRODUCT_CREATED("product-created"),
    PRODUCT_UPDATED("product-updated"),
    PRODUCT_DELETED("product-deleted");
    
    private final String topicName;
    
    KafkaTopics(String topicName) {
        this.topicName = topicName;
    }
    
    public String getTopicName() {
        return topicName;
    }
    
    @Override
    public String toString() {
        return topicName;
    }
}