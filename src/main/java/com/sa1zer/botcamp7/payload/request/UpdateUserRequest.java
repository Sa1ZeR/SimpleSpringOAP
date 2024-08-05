package com.sa1zer.botcamp7.payload.request;

import com.sa1zer.botcamp7.annotation.NotBlank;
import com.sa1zer.botcamp7.annotation.NotNull;
import com.sa1zer.botcamp7.annotation.Regex;
import io.swagger.v3.oas.annotations.Parameter;

public record UpdateUserRequest(@Parameter(description = "Имя пользователя", required = true) @NotNull Long id, @NotBlank @NotNull String name,
                                @Parameter(description = "Email пользователя", required = true) @Regex(regex = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$") String email) {
}
