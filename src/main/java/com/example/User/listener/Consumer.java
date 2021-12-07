package com.example.User.listener;

import com.example.User.data.Employee;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    @KafkaListener(topics = "kafka_topic", groupId = "group_json",
            containerFactory = "userKafkaListenerFactory")
    public void consumeJson(Employee employee) {
        System.out.println("Consumed JSON Message: " + employee);
    }

}
