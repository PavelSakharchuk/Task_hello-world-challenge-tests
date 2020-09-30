package com.rubrain.libertex.rest.responses;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class LoginResponse {
    private ResultCodeEnum resultCode;
    private String errorMessage;
}
