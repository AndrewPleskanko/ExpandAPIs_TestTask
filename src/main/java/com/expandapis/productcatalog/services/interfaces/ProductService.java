package com.expandapis.productcatalog.services.interfaces;

import com.expandapis.productcatalog.dto.ProductDto;
import com.expandapis.productcatalog.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    void saveProducts(ProductDto request);

    Page<Product> getAllProducts(Pageable pageable);
}
