package ru.ylab.learn.warehouseservice.mapper;

import org.springframework.stereotype.Component;
import ru.ylab.learn.warehouseservice.model.StockInfo;
import ru.ylab.team.lib.StockResponse;
import ru.ylab.team.lib.dto.StockDto;

@Component
public class StockMapper {

    // --- REST Mapping ---

    public StockDto toDto(StockInfo info) {
        if (info == null) return null;
        StockDto dto = new StockDto();
        dto.setProductId(info.getProductId());
        dto.setProductName(info.getProductName());
        dto.setIsAvailable(info.isAvailable());
        dto.setQuantity(info.getQuantity());
        return dto;
    }

    public StockInfo toInfo(StockDto dto) {
        if (dto == null) return null;
        return new StockInfo(
            dto.getProductId(),
            dto.getProductName(),
            dto.getQuantity() != null && dto.getQuantity() > 0,
            dto.getQuantity() != null ? dto.getQuantity() : 0
        );
    }

    // --- gRPC Mapping ---

    public StockResponse toProto(StockInfo info) {
        if (info == null) return StockResponse.getDefaultInstance();
        return StockResponse.newBuilder()
            .setProductId(info.getProductId())
            .setProductName(info.getProductName())
            .setIsAvailable(info.isAvailable())
            .setQuantity(info.getQuantity())
            .build();
    }
}