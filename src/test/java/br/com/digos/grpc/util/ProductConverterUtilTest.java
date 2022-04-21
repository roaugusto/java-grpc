package br.com.digos.grpc.util;

import br.com.digos.grpc.domain.Product;
import br.com.digos.grpc.dto.ProductInputDTO;
import br.com.digos.grpc.dto.ProductOutputDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductConverterUtilTest {

    @Test
    public void productToProductOutputDTOTest() {
        Product product = new Product( 1L, "product name", 10.00, 10);
        ProductOutputDTO productOutputDTO = ProductConverterUtil.productToProductOutputDTO(product);

        Assertions.assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(productOutputDTO);
    }

    @Test
    public void productInputDTOToProductTest() {
        ProductInputDTO productInputDTO = new ProductInputDTO( "product name", 10.00, 10);
        Product product = ProductConverterUtil.productInputDTOToProduct(productInputDTO);

        Assertions.assertThat(productInputDTO)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

}
