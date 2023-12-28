package com.example.testtasks.services;

import com.example.testtasks.dto.ProductDTO;
import com.example.testtasks.entity.Product;
import com.example.testtasks.repositories.ProductRepository;
import com.example.testtasks.services.interfaces.ProductServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements ProductServiceImp {
    private static final Logger logger = LogManager.getLogger();

    private static final String SAVING_PRODUCTS_MESSAGE = "Saving products: {}";
    private static final String PRODUCTS_SAVED_SUCCESSFULLY_MESSAGE = "Products saved successfully";
    private static final String ERROR_SAVING_PRODUCTS_MESSAGE = "Error saving products";
    private static final String RETRIEVING_ALL_PRODUCTS_MESSAGE = "Retrieving all products";
    private static final String PRODUCTS_RETRIEVED_SUCCESSFULLY_MESSAGE = "Products retrieved successfully";
    private static final String ERROR_RETRIEVING_PRODUCTS_MESSAGE = "Error retrieving products";

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void saveProducts(ProductDTO request) {
        try {
            logger.info(SAVING_PRODUCTS_MESSAGE, request.getRecords());
            List<Product> records = request.getRecords();
            productRepository.saveAll(records);
            logger.info(PRODUCTS_SAVED_SUCCESSFULLY_MESSAGE);
        } catch (Exception e) {
            logger.error(ERROR_SAVING_PRODUCTS_MESSAGE, e);
            throw e;
        }
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            logger.info(RETRIEVING_ALL_PRODUCTS_MESSAGE);
            List<Product> products = productRepository.findAll();
            logger.info(PRODUCTS_RETRIEVED_SUCCESSFULLY_MESSAGE);
            return products;
        } catch (Exception e) {
            logger.error(ERROR_RETRIEVING_PRODUCTS_MESSAGE, e);
            throw e;
        }
    }
}