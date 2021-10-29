package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer>{

}
