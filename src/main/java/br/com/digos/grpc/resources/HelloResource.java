package br.com.digos.grpc.resources;

import br.com.digos.grpc.HelloReq;
import br.com.digos.grpc.HelloRes;
import br.com.digos.grpc.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class HelloResource extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hello(HelloReq request, StreamObserver<HelloRes> responseObserver) {
        var response = HelloRes.newBuilder()
                .setMessage(request.getMessage())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
