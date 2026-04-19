package ru.ylab.learn.warehouseservice.controller;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.ylab.learn.warehouseservice.service.StockServiceGrpc;
import ru.ylab.team.lib.ReserveStockRequest;
import ru.ylab.team.lib.StockRequest;
import ru.ylab.team.lib.StockResponse;
import ru.ylab.team.lib.WarehouseServiceGrpc;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class WarehouseGrpcService extends WarehouseServiceGrpc.WarehouseServiceImplBase {

    private final StockServiceGrpc stockServiceGrpc;

    @Override
    public void getStock(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        try {
            StockResponse response = stockServiceGrpc.getStockProto(request.getProductId());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void reserveStock(ReserveStockRequest request, StreamObserver<StockResponse> responseObserver) {
        try {
            StockResponse response = stockServiceGrpc.reserveStock(request.getProductId(), request.getQuantity());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("gRPC: Ошибка резерва - {}", e.getMessage());
            // Возвращаем ошибку FAILED_PRECONDITION (подходящий статус для нехватки товара)
            responseObserver.onError(Status.FAILED_PRECONDITION.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}