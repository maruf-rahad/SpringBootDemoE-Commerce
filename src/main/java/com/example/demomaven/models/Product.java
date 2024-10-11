package com.example.demomaven.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String productName;

    private String productDescription;

    private String productBrand;

    private String productCategory;

    private Date productDate;

    private boolean isAvailable;

    private Long quantity;

    private Long productPrice;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productBrand='" + productBrand + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", productDate=" + productDate +
                ", isAvailable=" + isAvailable +
                ", quantity=" + quantity +
                ", productPrice=" + productPrice +
                '}';
    }
}
