package ru.ylab.learn.warehouseservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ylab.learn.warehouseservice.service.StockService;
import ru.ylab.team.lib.dto.StockDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class WarehouseController {

    private final StockService stockService;

    @PostMapping
    public ResponseEntity<StockDto> create(@RequestBody StockDto dto) {
        log.info("REST: Получен запрос на создание товара: {}", dto.getProductName());
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.createStock(dto));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<StockDto> getStockById(@PathVariable Integer productId) {
        log.info("REST: Получен запрос на поиск товара с ID: {}", productId);
        return ResponseEntity.ok(stockService.getStockById(productId));
    }

    @GetMapping
    public ResponseEntity<List<StockDto>> getAll() {
        return ResponseEntity.ok().body(stockService.getAllStocks());
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer productId) {
        log.info("REST: Получен запрос на удаление товара с ID: {}", productId);
        stockService.deleteStock(productId);
        log.info("REST: Товар с ID: {} успешно удален", productId);
    }
}