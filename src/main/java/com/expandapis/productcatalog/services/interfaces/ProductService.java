package com.expandapis.productcatalog.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.expandapis.productcatalog.dto.ProductDto;
import com.expandapis.productcatalog.entity.Product;

public interface ProductService {
    void saveProducts(ProductDto request);

    Page<Product> getAllProducts(Pageable pageable);
}
