package ru.ylab.learn.warehouseservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockInfo {

    private int productId;

    private String productName;

    private boolean isAvailable;

    private int quantity;
}
