package ru.ylab.learn.warehouseservice.controller;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.ylab.learn.warehouseservice.exception.NotFoundException;
import ru.ylab.learn.warehouseservice.service.StockServiceGrpc;
import ru.ylab.team.lib.ReserveStockRequest;
import ru.ylab.team.lib.StockRequest;
import ru.ylab.team.lib.StockResponse;
import ru.ylab.team.lib.WarehouseServiceGrpc;

/**
 * gRPC-контроллер, реализующий серверную часть контракта WarehouseService.
 * <p>
 * Класс обрабатывает входящие бинарные вызовы, делегирует бизнес-логику внутренним сервисам
 * и управляет жизненным циклом потока ответа через {@link StreamObserver}.
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class WarehouseGrpcService extends WarehouseServiceGrpc.WarehouseServiceImplBase {

    private final StockServiceGrpc stockServiceGrpc;

    /**
     * Получает информацию о состоянии запасов товара по его ID.
     *
     * @param request          объект запроса, содержащий идентификатор товара.
     * @param responseObserver наблюдатель потока ответа.
     * Вызывает {@code onNext} для передачи данных и {@code onCompleted} для закрытия стрима.
     * * <p><b>Статусы ответов:</b>
     * <ul>
     * <li>{@link Status#NOT_FOUND} — если товар с указанным ID отсутствует в БД.</li>
     * <li>{@link Status#INTERNAL} — при возникновении непредвиденных системных ошибок.</li>
     * </ul>
     */
    @Override
    public void getStock(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        try {
            StockResponse response = stockServiceGrpc.getStockProto(request.getProductId());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (NotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    /**
     * Выполняет бизнес-операцию резервирования (списания) товара.
     *
     * @param request          содержит ID товара и требуемое количество.
     * @param responseObserver наблюдатель потока ответа.
     * * <p><b>Статусы ответов:</b>
     * <ul>
     * <li>{@link Status#FAILED_PRECONDITION} — если товара недостаточно на складе для выполнения заказа.</li>
     * <li>{@link Status#INTERNAL} — при критических сбоях в процессе обработки транзакции.</li>
     * </ul>
     */
    @Override
    public void reserveStock(ReserveStockRequest request, StreamObserver<StockResponse> responseObserver) {
        try {
            StockResponse response = stockServiceGrpc.reserveStock(request.getProductId(), request.getQuantity());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("gRPC: Ошибка резерва - {}", e.getMessage());
            responseObserver.onError(Status.FAILED_PRECONDITION.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}