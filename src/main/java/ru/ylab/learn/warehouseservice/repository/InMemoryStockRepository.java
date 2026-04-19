package ru.ylab.learn.warehouseservice.repository;

import org.springframework.stereotype.Repository;
import ru.ylab.learn.warehouseservice.model.StockInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryStockRepository implements StockRepository {

    private final Map<Integer, StockInfo> storage = new ConcurrentHashMap<>();

    @Override
    public StockInfo save(StockInfo stockInfo) {
        storage.put(stockInfo.getProductId(), stockInfo);
        return stockInfo;
    }

    @Override
    public Optional<StockInfo> findById(Integer productId) {
        return Optional.ofNullable(storage.get(productId));
    }

    @Override
    public List<StockInfo> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(Integer productId) {
        storage.remove(productId);
    }
}
