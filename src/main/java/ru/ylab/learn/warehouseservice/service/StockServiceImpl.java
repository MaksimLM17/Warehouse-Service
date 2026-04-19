package ru.ylab.learn.warehouseservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ylab.learn.warehouseservice.exception.NotFoundException;
import ru.ylab.learn.warehouseservice.mapper.StockMapper;
import ru.ylab.learn.warehouseservice.model.StockInfo;
import ru.ylab.learn.warehouseservice.repository.StockRepository;
import ru.ylab.team.lib.dto.StockDto;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository repository;
    private final StockMapper stockMapper;
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public StockDto createStock(StockDto stockDto) {
        stockDto.setProductId(idGenerator.getAndIncrement());
        StockInfo stockInfo = stockMapper.toInfo(stockDto);
        return stockMapper.toDto(repository.save(stockInfo));
    }

    @Override
    public StockDto getStockById(Integer productId) {
        return repository.findById(productId)
            .map(stockMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
    }

    @Override
    public List<StockDto> getAllStocks() {
        return repository.findAll().stream()
            .map(stockMapper::toDto)
            .toList();
    }

    @Override
    public void deleteStock(Integer productId) {
        repository.deleteById(productId);
    }
}
