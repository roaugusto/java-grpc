package br.com.digos.grpc.resources;

import br.com.digos.grpc.ProductRequest;
import br.com.digos.grpc.ProductResponse;
import br.com.digos.grpc.ProductServiceGrpc;
import br.com.digos.grpc.dto.ProductInputDTO;
import br.com.digos.grpc.dto.ProductOutputDTO;
import br.com.digos.grpc.service.IProductService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ProductResource extends ProductServiceGrpc.ProductServiceImplBase {

    private final IProductService productService;

    public ProductResource(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public void create(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        ProductInputDTO inputDTO =  new ProductInputDTO(
                request.getName(),
                request.getPrice(),
                request.getQuantityInStack()
        );

        ProductOutputDTO outputDTO = this.productService.create(inputDTO);

        ProductResponse response = ProductResponse.newBuilder()
                .setId(outputDTO.getId())
                .setName(outputDTO.getName())
                .setPrice(outputDTO.getPrice())
                .setQuantityInStack(outputDTO.getQuantityInStock())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
