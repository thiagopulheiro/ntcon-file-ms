package com.ntconsult.challenge.thiago.core;

import static org.mockito.Mockito.verify;

import com.ntconsult.challenge.thiago.core.data.NewFileEventRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RunWith(MockitoJUnitRunner.class)
public class NewFileEventProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    public void send_convertAndSendIsCalled() {
        final NewFileEventProducer newFileEventProducer = new NewFileEventProducer(rabbitTemplate, "queue");
        final NewFileEventRequest newFileEventRequest = new NewFileEventRequest();
        newFileEventRequest.setFileName("xpto.txt");
        newFileEventProducer.send(newFileEventRequest);
        verify(rabbitTemplate).convertAndSend("queue", newFileEventRequest);
    }
}