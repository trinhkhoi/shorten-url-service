package com.example.shortenurl.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseDTO<T> {
    private String message;
    private Map<String, String> errors;
    private T data;

    public ResponseDTO() {
    }

    public ResponseDTO(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static final class ResponseBuilder<T> {
        private String message;
        private Map<String, String> errors;
        private T data;

        public static ResponseBuilder newBuilder() {
            return new ResponseBuilder<>();
        }

        public ResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ResponseBuilder<T> errors(Map<String, String> errors) {
            this.errors = errors;
            return this;
        }

        public ResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseDTO<T> build() {
            ResponseDTO<T> responseDTO = new ResponseDTO<>();
            responseDTO.setMessage(message);
            responseDTO.setErrors(errors);
            responseDTO.setData(data);
            return responseDTO;
        }
    }
}
