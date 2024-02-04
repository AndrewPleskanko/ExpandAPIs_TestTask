package com.expandapis.productcatalog.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "entry_date")
    private Date entryDate;

    @Column(name = "item_code")
    private String itemCode;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_quantity")
    private int itemQuantity;

    @Column(name = "status")
    private String status;
}
