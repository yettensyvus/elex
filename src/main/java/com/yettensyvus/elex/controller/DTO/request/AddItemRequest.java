package com.yettensyvus.elex.controller.DTO.request;


import lombok.Data;


@Data
public class AddItemRequest {

    private Long productId;
    private int quantity;
}