package com.sa1zer.botcamp7.payload.request;

import com.sa1zer.botcamp7.annotation.NotNull;
import com.sa1zer.botcamp7.entity.OrderStatus;
import io.swagger.v3.oas.annotations.Parameter;

public record UpdateOrderRequest(@Parameter(description = "Статус заказа", required = true) @NotNull OrderStatus status) {
}
