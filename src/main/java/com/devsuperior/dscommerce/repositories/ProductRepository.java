package com.devsuperior.dscommerce.repositories;

import com.devsuperior.dscommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

//Colocando como argumento a entidade e o tipo do ID dessa entidade
public interface ProductRepository extends JpaRepository<Product, Long> {

}
