package ru.vasabijaj.rest.models.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class Product {
    private String name;
    @NotNull(message = "product code required")
    @Size(min = 13, max = 13, message = "product code must contain 13 characters")
    private String code;
}
