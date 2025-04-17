package com.yettensyvus.elex.controller.DTO.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateProductRequest {

    private String title;
    private String description;
    private int mrpPrice;
    private int sellingPrice;
    private List<String> images;
    private String category;
    private String category2;
    private String category3;
}
