package br.com.digos.grpc.resources;


import br.com.digos.grpc.ProductRequest;
import br.com.digos.grpc.ProductResponse;
import br.com.digos.grpc.ProductServiceGrpc;
import br.com.digos.grpc.RequestById;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext
public class ProductResourceIntegrationTest {

    @GrpcClient("inProcess")
    private ProductServiceGrpc.ProductServiceBlockingStub serviceBlockingStub;

    @Autowired
    private Flyway flyway;

    @BeforeEach
    public void setUp() {
      flyway.clean();
      flyway.migrate();
    }

    @Test
    @DisplayName("when valid data is provided a product is created")
    public void createProductSuccessTest() {

        ProductRequest productRequest = ProductRequest.newBuilder()
                .setName("product name")
                .setPrice(10.00)
                .setQuantityInStack(100).build();

        ProductResponse productResponse = serviceBlockingStub.create(productRequest);

        Assertions.assertThat(productRequest)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "price", "quantity_in_stock")
                .isEqualTo(productResponse);

    }

    @Test
    @DisplayName("when create is called with duplicated name, throw ProductAlreadyExistsException")
    public void createProductAlreadyExistsExceptionTest() {

        ProductRequest productRequest = ProductRequest.newBuilder()
                .setName("Product A")
                .setPrice(10.00)
                .setQuantityInStack(100)
                .build();

        Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
                        .isThrownBy(() -> serviceBlockingStub.create(productRequest))
                .withMessage("ALREADY_EXISTS: Produto Product A já cadastrado no sistema.");


    }

    @Test
    @DisplayName("when findById method is called with a valid id a product is returned")
    public void findByIdSuccessTest() {

        RequestById request = RequestById.newBuilder().setId(1L).build();

        ProductResponse productResponse = serviceBlockingStub.findById(request);

        Assertions.assertThat(productResponse.getId()).isEqualTo(request.getId());
        Assertions.assertThat(productResponse.getName()).isEqualTo("Product A");

    }

    @Test
    @DisplayName("when findById is called with an invalid id, throw ProductNotFoundException")
    public void findByIdExceptionTest() {

        RequestById request = RequestById.newBuilder().setId(4L).build();

        Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> serviceBlockingStub.findById(request))
                .withMessage("NOT_FOUND: Produto com ID 4 não encontrado.");

    }

    @Test
    @DisplayName("when delete is called, product should be deleted from database")
    public void deleteSuccessTest() {

        RequestById request = RequestById.newBuilder().setId(1L).build();

        assertThatNoException().isThrownBy(() -> serviceBlockingStub.delete(request));
    }

    @Test
    @DisplayName("when delete is called with an invalid id, throw ProductNotFoundException")
    public void deleteExceptionTest() {

        RequestById request = RequestById.newBuilder().setId(4L).build();

        Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> serviceBlockingStub.delete(request))
                .withMessage("NOT_FOUND: Produto com ID 4 não encontrado.");

    }

}
