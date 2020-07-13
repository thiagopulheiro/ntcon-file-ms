package com.ntconsult.challenge.thiago.core;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ntconsult.challenge.thiago.core.data.NewFileEventRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.StringUtils;

@RunWith(MockitoJUnitRunner.class)
public class NewFileEventConsumerTest {

    @Mock
    private NewFileEventAggregator newFileEventAggregator;

    @InjectMocks
    private NewFileEventConsumer newFileEventConsumer;

    @Test
    public void listen_validMessage_return() {
        this.newFileEventConsumer.listen(new NewFileEventRequest());
        verify(this.newFileEventAggregator).aggregateAndCreateResult(any());
    }

}
