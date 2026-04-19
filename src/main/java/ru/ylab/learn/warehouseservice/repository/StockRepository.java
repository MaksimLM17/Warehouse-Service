package ru.ylab.learn.warehouseservice.repository;

import ru.ylab.learn.warehouseservice.model.StockInfo;
import java.util.List;
import java.util.Optional;

public interface StockRepository {

    StockInfo save(StockInfo stockInfo);

    Optional<StockInfo> findById(Integer productId);

    List<StockInfo> findAll();

    void deleteById(Integer productId);
}
