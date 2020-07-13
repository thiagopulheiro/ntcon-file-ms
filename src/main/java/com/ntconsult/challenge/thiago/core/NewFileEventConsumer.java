package com.ntconsult.challenge.thiago.core;

import com.ntconsult.challenge.thiago.core.data.NewFileEventRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NewFileEventConsumer {

    @Autowired
    private NewFileEventAggregator newFileEventAggregator;

    @RabbitListener(queues = "#{newFileEventQueue.name}", concurrency = "${app.queue_config.newfileevent.consumers}")
    public void listen(final NewFileEventRequest newFileEventRequest) {
        newFileEventAggregator.aggregateAndCreateResult(newFileEventRequest);
    }

}
