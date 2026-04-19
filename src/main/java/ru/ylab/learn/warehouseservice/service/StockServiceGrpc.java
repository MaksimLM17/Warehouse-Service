package ru.ylab.learn.warehouseservice.service;

import ru.ylab.team.lib.StockResponse;

public interface StockServiceGrpc {

    StockResponse getStockProto(Integer productId);

    StockResponse reserveStock(Integer productId, Integer quantity);

}
