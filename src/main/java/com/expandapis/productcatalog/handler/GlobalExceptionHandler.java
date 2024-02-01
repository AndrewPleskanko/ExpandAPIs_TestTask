package com.expandapis.productcatalog.handler;

import com.expandapis.productcatalog.entity.Product;
import com.expandapis.productcatalog.exceptions.ProductCatalogException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /*@ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Handle AccessDeniedException", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }*/

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<String> handleOtherExceptions(DataIntegrityViolationException e) {
        log.error("Handle other exceptions", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
/*
    @ExceptionHandler(ProductCatalogException.class)
    public ResponseEntity<List<Product>> handleProductCatalogException(ProductCatalogException e) {
        log.error("Handle ProductCatalogException", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }*/
}
