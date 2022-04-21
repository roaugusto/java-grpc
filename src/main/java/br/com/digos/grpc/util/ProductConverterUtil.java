package br.com.digos.grpc.util;

import br.com.digos.grpc.domain.Product;
import br.com.digos.grpc.dto.ProductInputDTO;
import br.com.digos.grpc.dto.ProductOutputDTO;

public class ProductConverterUtil {

    public static ProductOutputDTO productToProductOutputDTO(Product product) {
        return new ProductOutputDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantityInStock()
        );
    }

    public static Product productInputDTOToProduct(ProductInputDTO product) {
        return new Product(
                null,
                product.getName(),
                product.getPrice(),
                product.getQuantityInStock()
        );
    }

}
