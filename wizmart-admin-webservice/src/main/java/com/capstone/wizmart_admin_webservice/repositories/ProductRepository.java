package com.capstone.wizmart_admin_webservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.wizmart_admin_webservice.model.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
}