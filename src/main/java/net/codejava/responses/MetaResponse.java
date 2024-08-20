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
public class MetaResponse<Meta, Data> {
    private int statusCode;
    private String message;
    boolean success = false;

    private Meta meta;
    private Data data;

    public MetaResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        if (statusCode == HttpStatus.OK.value()) this.success = true;
    }

    public static <Meta, Data> MetaResponse<Meta, Data> successfulResponse(String message, Meta meta, Data data) {
        MetaResponse<Meta, Data> metaResponse = new MetaResponse<>(HttpStatus.OK.value(), message);
        metaResponse.setSuccess(true);
        metaResponse.setMeta(meta);
        metaResponse.setData(data);
        return metaResponse;
    }
}
