package net.codejava.responses;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class Response<Data> {
    private int statusCode;
    private String message;
    boolean success = false;

    private Data data;

    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        if (statusCode == HttpStatus.OK.value()) this.success = true;
    }

    public static <Data> Response<Data> failedResponse(String message) {
        return failedResponse(HttpStatus.BAD_REQUEST.value(), message, null);
    }

    public static <Data> Response<Data> failedResponse(Data data) {
        return failedResponse(HttpStatus.BAD_REQUEST.value(), "Bad request", data);
    }

    public static <Data> Response<Data> failedResponse(int statusCode, String message) {
        return failedResponse(statusCode, message, null);
    }

    public static <Data> Response<Data> failedResponse(int statusCode, String message, Data data) {
        Response<Data> response = new Response<>(statusCode, message);
        response.setSuccess(false);
        response.setData(data);
        return response;
    }

    public static <Data> Response<Data> successfulResponse(String message, Data data) {
        return successfulResponse(HttpStatus.OK.value(), message, data);
    }

    public static <Data> Response<Data> successfulResponse(String message) {
        return successfulResponse(HttpStatus.OK.value(), message, null);
    }

    public static <Data> Response<Data> successfulResponse(int statusCode, String message, Data data) {
        Response<Data> response = new Response<>(statusCode, message);
        response.setSuccess(true);
        response.setData(data);
        return response;
    }
}
