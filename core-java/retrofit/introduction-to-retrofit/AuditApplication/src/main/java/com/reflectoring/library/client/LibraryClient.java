package com.reflectoring.library.client;

import com.reflectoring.library.model.LibResponse;
import com.reflectoring.library.model.mapstruct.BookDto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface LibraryClient {

    /*@Headers({
            "Accept: application/json",
            "Cache-Control: max-age=640000"
    })*/
    @GET("/library/managed/books")
    Call<List<BookDto>> getAllBooks(@Query("type") String type);

    @GET("/library/managed/books/{requestId}")
    Call<BookDto> getAllBooksWithHeaders(@Header("requestId") String requestId);

    @POST("/library/managed/books")
    Call<LibResponse> createNewBook(@Body BookDto book);

    @PUT("/library/managed/books/{id}")
    Call<LibResponse> updateBook(@Path("id") Long id, @Body BookDto book);

    @DELETE("/library/managed/books/{id}")
    Call<LibResponse> deleteBook(@Path("id") Long id);
}
