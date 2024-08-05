package com.sa1zer.botcamp7.payload.request;

import com.sa1zer.botcamp7.entity.OrderStatus;
import io.swagger.v3.oas.annotations.Parameter;

public record CreateOrderRequest(@Parameter(description = "Id пользователя", required = true) Long userId,
                                 @Parameter(description = "Email пользователя", required = true) String desc) {
}
