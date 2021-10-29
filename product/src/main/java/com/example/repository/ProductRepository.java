package com.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

	@Query(value=" SELECT product.*,tb_brands.brandname FROM product " 
	+ " INNER JOIN tb_brands ON tb_brands.id=product.brand_id " 
	+ " WHERE product.name LIKE %?1% OR tb_brands.brandname LIKE %?1% OR product.made_in LIKE %?1%"
			,nativeQuery = true)
	public Page<Product> findAll(String keyword, Pageable pageable);
	
}
