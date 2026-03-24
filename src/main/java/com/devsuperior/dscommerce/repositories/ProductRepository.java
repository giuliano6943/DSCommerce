package com.devsuperior.dscommerce.repositories;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

//Colocando como argumento a entidade e o tipo do ID dessa entidade
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT obj FROM Product obj " +
            "WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%')) ")
    Page<Product> searchByName(String name, Pageable pageable);
}
