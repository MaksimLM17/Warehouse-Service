package ru.ylab.learn.warehouseservice.service;

import ru.ylab.team.lib.dto.StockDto;

import java.util.List;

public interface StockService {

    StockDto createStock(StockDto stockDto);

    StockDto getStockById(Integer productId);

    List<StockDto> getAllStocks();

    void deleteStock(Integer productId);

}