package com.sa1zer.botcamp7.api;

import com.sa1zer.botcamp7.annotation.NotNull;
import com.sa1zer.botcamp7.annotation.Valid;
import com.sa1zer.botcamp7.entity.Order;
import com.sa1zer.botcamp7.facade.OrderFacade;
import com.sa1zer.botcamp7.payload.dto.OrderDto;
import com.sa1zer.botcamp7.payload.request.CreateOrderRequest;
import com.sa1zer.botcamp7.payload.request.UpdateOrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order/")
public class OrderController {

    private final OrderFacade orderFacade;

    @PostMapping("create")
    @Operation(description = "Создание заказа для пользователя")
    public OrderDto createOrder(@RequestBody @Valid @ParameterObject CreateOrderRequest request) {
        return orderFacade.createOrder(request);
    }

    @Operation(description = "Обновление заказа")
    @PatchMapping("update/{id}")
    public OrderDto updateOrder(@PathVariable @Valid @NotNull Long id, @RequestBody @Valid @ParameterObject UpdateOrderRequest request) {
        return orderFacade.updateOrder(id, request);
    }

    @Operation(description = "Получение информации о заказе")
    @GetMapping("{id}")
    public OrderDto getOrder(@PathVariable @Valid @NotNull @Parameter(description = "Id заказа", required = true) Long id) {
        return orderFacade.findOrder(id);
    }

    @Operation(description = "Удаление заказа")
    @DeleteMapping("delete/{id}")
    public String deleted(@PathVariable @Parameter(description = "Id заказа", required = true) Long id) {
        return orderFacade.delete(id);
    }
}
