package com.rubrain.libertex.rest.responses;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class HelloResponse {
    private ResultCode resultCode;
    private String message;


    public enum ResultCode{
        OK("Ok"),
        UNAUTHORIZED("Unauthorized");

        private String code;

        ResultCode(String code){
            this.code = code;
        }

        @JsonValue
        public String jsonValue() {
            return this.code;
        }
    }
}
