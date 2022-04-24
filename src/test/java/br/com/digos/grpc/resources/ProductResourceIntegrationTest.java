package br.com.digos.grpc.resources;


import br.com.digos.grpc.*;
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

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.tuple;

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
                .setQuantityInStock(100).build();

        ProductResponse productResponse = serviceBlockingStub.create(productRequest);

        assertThat(productRequest)
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
                .setQuantityInStock(100)
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

        assertThat(productResponse.getId()).isEqualTo(request.getId());
        assertThat(productResponse.getName()).isEqualTo("Product A");

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

    @Test
    @DisplayName("when findAll method is called, a list of products is returned")
    public void findAllSuccessTest() {

        EmptyRequest request = EmptyRequest.newBuilder().build();
        ProductResponseList responseList = serviceBlockingStub.findAll(request);

        assertThat(responseList).isInstanceOf(ProductResponseList.class);
        assertThat(responseList.getProductsCount()).isEqualTo(2);

        assertThat(responseList.getProductsList())
                .extracting("id", "name", "price", "quantityInStock")
                .contains(
                        tuple(1L, "Product A", 10.99, 10),
                        tuple(2L, "Product B", 10.99, 10)
                );

    }

}
