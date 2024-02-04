package com.expandapis.productcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expandapis.productcatalog.entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}