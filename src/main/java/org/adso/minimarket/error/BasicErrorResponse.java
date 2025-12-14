package org.adso.minimarket.error;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class BasicErrorResponse {
    String message;
    String code;

    public BasicErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
