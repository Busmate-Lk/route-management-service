package com.busmate.routeschedule.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OperatorListener {

    @KafkaListener(topics = "operator-events", groupId = "busmate-group")
    public void handleOperatorCreated(String operatorJson) {
        // Parse operatorJson and update the database
        logger.info("Operator created: " + operatorJson);
    }

    private Logger logger = LoggerFactory.getLogger(OperatorListener.class);
}
