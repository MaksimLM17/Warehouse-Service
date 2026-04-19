package ru.ylab.learn.warehouseservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ylab.learn.warehouseservice.exception.NotFoundException;
import ru.ylab.learn.warehouseservice.mapper.StockMapper;
import ru.ylab.learn.warehouseservice.model.StockInfo;
import ru.ylab.learn.warehouseservice.repository.StockRepository;
import ru.ylab.team.lib.StockResponse;

@RequiredArgsConstructor
@Service
@Slf4j
public class StockServiceGrpcImpl implements StockServiceGrpc {

    private final StockRepository repository;
    private final StockMapper stockMapper;

    @Override
    public StockResponse getStockProto(Integer productId) {
        return repository.findById(productId)
            .map(stockMapper::toProto)
            .orElseThrow(() -> new NotFoundException("Товар не найден: " + productId));
    }

    @Override
    public StockResponse reserveStock(Integer productId, Integer quantity) {
        log.info("gRPC: Попытка резерва товара {} в количестве {}", productId, quantity);

        StockInfo info = repository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Товар не найден: " + productId));

        if (info.getQuantity() < quantity) {
            throw new IllegalArgumentException("Недостаточно товара на складе. В наличии: " + info.getQuantity());
        }

        info.setQuantity(info.getQuantity() - quantity);

        // Если товар закончился, меняем статус
        if (info.getQuantity() == 0) {
            info.setAvailable(false);
            log.warn("Товар {} закончился на складе!", productId);
        }

        repository.save(info);
        log.info("gRPC: Резерв успешен. Остаток товара {}: {}", info.getProductName(), info.getQuantity());

        return stockMapper.toProto(info);
    }
}
