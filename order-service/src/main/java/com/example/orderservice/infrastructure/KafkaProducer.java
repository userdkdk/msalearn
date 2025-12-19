package com.example.orderservice.infrastructure;

import com.example.orderservice.domain.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    public void send(String topic, Order order) {
        try {
            OrderItem orderItem = OrderItem.of(order);
            String jsonInString = mapper.writeValueAsString(orderItem);
            kafkaTemplate.send(topic, jsonInString);
            log.info("Kafka Producer sent data from the Order microservice: " + orderItem);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
