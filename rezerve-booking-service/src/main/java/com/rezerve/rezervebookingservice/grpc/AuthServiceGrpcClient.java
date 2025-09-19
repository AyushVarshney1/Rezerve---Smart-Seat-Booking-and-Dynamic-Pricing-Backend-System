package com.rezerve.rezervebookingservice.grpc;

import auth.AuthRequest;
import auth.AuthResponse;
import auth.AuthServiceGrpc;
import com.rezerve.rezervebookingservice.dto.AuthServiceGrpcResponseDto;
import com.rezerve.rezervebookingservice.mapper.BookingMapper;
import com.rezerve.rezervebookingservice.exception.UnauthorisedException;
import com.rezerve.rezervebookingservice.exception.UserNotFoundException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceGrpcClient.class);
    private final AuthServiceGrpc.AuthServiceBlockingStub blockingStub;
    private final BookingMapper bookingMapper;

    public AuthServiceGrpcClient(@Value("${auth.service.address:localhost}") String serverAddress, @Value("${auth.service.grpc.port:9001}") int serverPort, BookingMapper bookingMapper) {

        log.info("Connecting to Auth service GRPC service at {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
        blockingStub = AuthServiceGrpc.newBlockingStub(channel);

        this.bookingMapper = bookingMapper;
    }

    public AuthServiceGrpcResponseDto extractUserInfo(String token){

        AuthRequest authRequest = AuthRequest.newBuilder().setToken(token).build();

        try{
            AuthResponse authResponse = blockingStub.extractUserInfo(authRequest);
            log.info("Received response from Auth service via GRPC: {}", authResponse);
            return bookingMapper.authServiceGrpcResponseDto(authResponse);

        }catch (StatusRuntimeException e) {
            switch (e.getStatus().getCode()) {
                case UNAUTHENTICATED -> {
                    log.warn("Invalid or expired token: {}", e.getStatus().getDescription());
                    throw new UnauthorisedException(
                            e.getStatus().getDescription() != null ? e.getStatus().getDescription() : "Invalid token"
                    );
                }
                case NOT_FOUND -> {
                    log.warn("User not found for token: {}", e.getStatus().getDescription());
                    throw new UserNotFoundException(
                            e.getStatus().getDescription() != null ? e.getStatus().getDescription() : "User ID not found"
                    );
                }
                default -> {
                    log.error("Auth service gRPC error: {} - {}", e.getStatus().getCode(), e.getMessage());
                    throw new RuntimeException("Auth service error: " + e.getStatus().getDescription());
                }
            }
        }
    }

}
