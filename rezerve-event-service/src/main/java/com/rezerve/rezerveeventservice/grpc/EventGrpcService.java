package com.rezerve.rezerveeventservice.grpc;

import com.rezerve.rezerveeventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
@RequiredArgsConstructor
public class EventGrpcService extends event.EventServiceGrpc.EventServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(EventGrpcService.class.getName());
    private final EventService eventService;


}
