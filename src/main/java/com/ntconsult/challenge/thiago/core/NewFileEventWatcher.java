package com.ntconsult.challenge.thiago.core;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import com.ntconsult.challenge.thiago.core.data.NewFileEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import javax.annotation.PostConstruct;

@Component
public class NewFileEventWatcher {

    @Autowired
    private NewFileEventProducer newFileEventProducer;

    @Value("${file.dir.in}")
    private String dirIn;

    private WatchService watchService;
    private Path logDir;

    @PostConstruct
    public void init() throws Exception {
        watchService = FileSystems.getDefault().newWatchService();
        logDir = Paths.get(dirIn);
        logDir.register(watchService, ENTRY_CREATE);
    }

    @Scheduled(fixedDelay = 1)
    public void watch() throws Exception {
        WatchKey key = watchService.take();
        for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent.Kind<?> kind = event.kind();
            if (ENTRY_CREATE.equals(kind)) {
                newFileEventProducer.send(
                    new NewFileEventRequest(String.valueOf(event.context())));
            }
        }
        key.reset();
    }

}
