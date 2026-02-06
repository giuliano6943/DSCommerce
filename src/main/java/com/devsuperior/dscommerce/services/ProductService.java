package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    //O retorno do metodo precisa ser DTO, pois não queremos mandar entidade do banco de dados para o controller
    public ProductDTO findById(Long id) {
        Optional<Product> result = productRepository.findById(id);
        Product product = result.get();
        ProductDTO dto = new ProductDTO(product);
        return dto;
    }
    //Metodo para pesquisar todos os produtos salvos
    //É utilizado o tipo Page para que o a classe Pageable seja chamada como parâmetro, com isso, podemos dividir os produtos em paginas.
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> result = productRepository.findAll(pageable);
        return result.map(x->new ProductDTO(x));
    }

    //Metodo insert para criar um novo produto
    @Transactional
    public ProductDTO insert(ProductDTO dto) {

        //Instanciando um objeto da entidade Product e atribuindo as variáveis dela as variáveis recebidas no JSON que foram atribuidas ao DTO
        Product entity = new Product();
        copyDtoToEntity(dto,entity);
        //Salvando no Banco de dados
        entity = productRepository.save(entity);
        //Retornando o DTO
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {

        Product entity = productRepository.getReferenceById(id);
        copyDtoToEntity(dto,entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }
    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }


    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
    }


}
