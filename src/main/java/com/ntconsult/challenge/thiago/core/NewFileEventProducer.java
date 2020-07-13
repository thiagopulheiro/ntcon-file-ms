package com.ntconsult.challenge.thiago.core;

import com.ntconsult.challenge.thiago.core.data.NewFileEventRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;

@AllArgsConstructor
@Slf4j
public class NewFileEventProducer {
    private final RabbitTemplate rabbitTemplate;
    private final String newFileEventQueue;

    @Async
    public void send(final NewFileEventRequest newFileEventRequest) {
        rabbitTemplate.convertAndSend(newFileEventQueue, newFileEventRequest);
    }

}
