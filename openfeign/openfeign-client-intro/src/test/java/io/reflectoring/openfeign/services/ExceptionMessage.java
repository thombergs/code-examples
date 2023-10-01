package io.reflectoring.openfeign.services;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class ExceptionMessage {
    private String timestamp;

    @NonNull
    private Integer status;

    private String error;

    @NonNull
    private String message;

    private String path;
}
