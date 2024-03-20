package com.mash.loginservice.service;

import com.mash.grpc.loginservice.CredentialsRequest;
import com.mash.grpc.loginservice.CredentialsResponse;
import com.mash.grpc.loginservice.UserServiceGrpc;
import com.mash.loginservice.entity.User;
import com.mash.loginservice.repository.UserRepository;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.reflection.v1alpha.ErrorResponse;
import io.grpc.stub.StreamObserver;
import jakarta.persistence.EntityExistsException;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    private static final Log log = LogFactory.getLog(UserService.class);

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void register(CredentialsRequest request, StreamObserver<CredentialsResponse> responseObserver) {
        Metadata.Key<ErrorResponse> errorResponseKey = ProtoUtils.keyForProto(ErrorResponse.getDefaultInstance());
        Metadata metadata = new Metadata();

        User user = new User();
        user.setUsername(request.getLogin());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (!userRepository.existsByUsername(request.getLogin())) {
            userRepository.save(user);
        } else {
            ErrorResponse errorResponse = ErrorResponse.newBuilder()
                    .setErrorMessage("User already exists")
                    .build();
            metadata.put(errorResponseKey, errorResponse);
            responseObserver.onError(Status.ALREADY_EXISTS.withDescription("This user already exists")
                    .asRuntimeException(metadata));
        }
        //todo: delete log
        log.info(request.getLogin() + " " + request.getPassword() + " " + user.getPassword());

        CredentialsResponse response = CredentialsResponse.newBuilder().setToken("hello").build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
