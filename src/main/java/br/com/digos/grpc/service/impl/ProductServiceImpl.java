package br.com.digos.grpc.service.impl;

import br.com.digos.grpc.domain.Product;
import br.com.digos.grpc.dto.ProductInputDTO;
import br.com.digos.grpc.dto.ProductOutputDTO;
import br.com.digos.grpc.exception.ProductAlreadyExistsException;
import br.com.digos.grpc.exception.ProductNotFoundException;
import br.com.digos.grpc.repository.ProductRepository;
import br.com.digos.grpc.service.IProductService;
import br.com.digos.grpc.util.ProductConverterUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductOutputDTO create(ProductInputDTO inputDTO) {
        checkDuplicity(inputDTO.getName());
        Product product = ProductConverterUtil.productInputDTOToProduct(inputDTO);
        Product productCreated = this.productRepository.save(product);
        return ProductConverterUtil.productToProductOutputDTO(productCreated);
    }

    @Override
    public ProductOutputDTO findById(Long id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException(id));
        return ProductConverterUtil.productToProductOutputDTO(product);
    }

    @Override
    public void delete(Long id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException(id));

        this.productRepository.delete(product);
    }

    @Override
    public List<ProductOutputDTO> findAll() {
        List<Product> products = this.productRepository.findAll();
        return products.stream()
                .map(ProductConverterUtil::productToProductOutputDTO)
                .collect(Collectors.toList());
    }

    private void checkDuplicity(String name) {
        this.productRepository.findByNameIgnoreCase(name)
                .ifPresent(e ->
                {
                    throw new ProductAlreadyExistsException(name);
                });
    }
}
