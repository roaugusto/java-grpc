package br.com.digos.grpc.service.impl;

import br.com.digos.grpc.domain.Product;
import br.com.digos.grpc.dto.ProductInputDTO;
import br.com.digos.grpc.dto.ProductOutputDTO;
import br.com.digos.grpc.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("when create product service is called with a valid data, a product is returned")
    public void createProductSuccessTest() {
        Product product = new Product(1L, "product name", 10.00, 10);

        when(productRepository.save(any())).thenReturn(product);

        ProductInputDTO inputDTO = new ProductInputDTO("product name", 10.00, 10);
        ProductOutputDTO outputDTO = productService.create(inputDTO);

        assertThat(outputDTO)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }
}
