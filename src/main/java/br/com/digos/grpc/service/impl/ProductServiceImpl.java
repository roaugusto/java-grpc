package br.com.digos.grpc.service.impl;

import br.com.digos.grpc.domain.Product;
import br.com.digos.grpc.dto.ProductInputDTO;
import br.com.digos.grpc.dto.ProductOutputDTO;
import br.com.digos.grpc.repository.ProductRepository;
import br.com.digos.grpc.service.IProductService;
import br.com.digos.grpc.util.ProductConverterUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductOutputDTO create(ProductInputDTO inputDTO) {
        Product product = ProductConverterUtil.productInputDTOToProduct(inputDTO);
        Product productCreated = this.productRepository.save(product);
        return ProductConverterUtil.productToProductOutputDTO(productCreated);
    }

    @Override
    public ProductOutputDTO findById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<ProductOutputDTO> findAll() {
        return null;
    }
}
