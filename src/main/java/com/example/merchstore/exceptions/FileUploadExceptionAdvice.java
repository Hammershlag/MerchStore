package com.example.merchstore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * The FileUploadExceptionAdvice class is a controller advice in the application.
 * It provides exception handling for MaxUploadSizeExceededException across all @RestController classes.
 *
 * It has one method:
 * <ul>
 *     <li>handleMaxSizeException(MaxUploadSizeExceededException exc): This method is invoked when a MaxUploadSizeExceededException is thrown in the application. It returns a ResponseEntity with a status of HttpStatus.PAYLOAD_TOO_LARGE and a body message of "File too large! Maximum upload size is 10MB."</li>
 * </ul>
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 29.05.2024
 */

@RestControllerAdvice
public class FileUploadExceptionAdvice {

    /**
     * This method is invoked when a MaxUploadSizeExceededException is thrown in the application.
     * It returns a ResponseEntity with a status of HttpStatus.PAYLOAD_TOO_LARGE and a body message of "File too large! Maximum upload size is 10MB."
     *
     * @param exc The MaxUploadSizeExceededException that was thrown.
     * @return A ResponseEntity with a status of HttpStatus.PAYLOAD_TOO_LARGE and a body message of "File too large! Maximum upload size is 10MB."
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("File too large! Maximum upload size is 10MB.");
    }
}
