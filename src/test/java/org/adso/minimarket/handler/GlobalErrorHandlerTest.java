package org.adso.minimarket.handler;

import org.adso.minimarket.error.ConstraintViolationResponse;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalErrorHandlerTest {
    @Test
    void whenKnownDataIntegrityError_ReturnDetailedErrorResponse() {
        GlobalErrorHandler handl = new GlobalErrorHandler();

        var di = new DataIntegrityViolationException("uk_user_email");

        ResponseEntity<@NonNull ConstraintViolationResponse> got = handl.handleDataIntegrity(di);

        assertTrue(got.hasBody());
        assertEquals(HttpStatus.CONFLICT, got.getStatusCode());

        ConstraintViolationResponse bod = got.getBody();

        assert bod != null;
        assertEquals("email", bod.getErrors().get(0).field());
        assertNotNull(bod.getErrors().get(0).message());
    }
}
