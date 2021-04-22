package com.example.shortenurl.common.exception;

import com.example.shortenurl.dto.response.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class BusinessExceptionMapper {
    private static Logger LOGGER = LoggerFactory.getLogger(BusinessExceptionMapper.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception e) {
        LOGGER.error("Unexpected error: " + e.getMessage(), e);
        ResponseDTO response = ResponseDTO.ResponseBuilder.newBuilder()
                                                          .message("Error occur")
                                                          .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(Exception e) {
        LOGGER.error("Validation error: " + e.getMessage(), e);
        ResponseDTO response = ResponseDTO.ResponseBuilder.newBuilder()
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDTO> handleBusinessException(final BusinessException ex) {
        LOGGER.debug(ex.getMessage());
        ResponseDTO responseDTO = ResponseDTO.ResponseBuilder.newBuilder()
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(responseDTO, ex.getStatus());
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ResponseDTO> handleMultipartException(MultipartException ex) {
        LOGGER.debug("Upload error", ex);
        ResponseDTO responseDTO = ResponseDTO.ResponseBuilder.newBuilder()
                .message("File upload is too large")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }
}
