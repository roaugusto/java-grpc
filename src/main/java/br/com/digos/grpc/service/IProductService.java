package br.com.digos.grpc.service;

import br.com.digos.grpc.dto.ProductInputDTO;
import br.com.digos.grpc.dto.ProductOutputDTO;

import java.util.List;

public interface IProductService {
    ProductOutputDTO create(ProductInputDTO inputDTO);
    ProductOutputDTO findById(Long id);
    void delete(Long id);
    List<ProductOutputDTO> findAll();
}
