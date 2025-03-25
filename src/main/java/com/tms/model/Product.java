package com.tms.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Scope("prototype")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class Product {
    private Integer id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name should not exceed 50 characters")
    private String name;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", message = "Price must be greater than zero")
    private Double price;

    private Timestamp created;
    private Timestamp updated;

}
