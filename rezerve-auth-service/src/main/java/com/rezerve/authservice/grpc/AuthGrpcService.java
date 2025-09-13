package com.rezerve.authservice.grpc;

import com.rezerve.authservice.exception.InvalidTokenException;
import com.rezerve.authservice.exception.UserNotFoundException;
import com.rezerve.authservice.model.User;
import com.rezerve.authservice.service.AuthService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import auth.AuthRequest;
import auth.AuthResponse;

@GrpcService
@RequiredArgsConstructor
public class AuthGrpcService extends auth.AuthServiceGrpc.AuthServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(AuthGrpcService.class.getName());
    private final AuthService authService;

    @Override
    public void extractUserInfo(AuthRequest authRequest, StreamObserver<AuthResponse> responseObserver){
        log.info("extractUserInfo request received and token is : {}", authRequest.getToken());

        try {
            User user = authService.extractUser(authRequest.getToken());

            AuthResponse response = AuthResponse.newBuilder()
                    .setUserId(user.getId())
                    .setUserEmail(user.getEmail())
                    .setUserRole(user.getRole().toString())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (InvalidTokenException e) {
            log.error("Invalid or expired JWT: {}", e.getMessage());
            responseObserver.onError(
                    Status.UNAUTHENTICATED
                            .withDescription(e.getMessage())
                            .augmentDescription(e.getMessage())
                            .asRuntimeException()
            );

        } catch (UserNotFoundException e) {
            log.error("User not found for token: {}", e.getMessage());
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("User ID/Email not found")
                            .asRuntimeException()
            );

        } catch (Exception e) {
            log.error("Unexpected error in extractUserId", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Auth Service internal error")
                            .augmentDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }
}
