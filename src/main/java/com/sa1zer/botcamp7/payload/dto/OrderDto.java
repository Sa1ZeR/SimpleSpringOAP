package com.sa1zer.botcamp7.payload.dto;

import com.sa1zer.botcamp7.entity.OrderStatus;
import lombok.Builder;

@Builder
public record OrderDto(Long id,
        String desc,
        OrderStatus status) {


}
