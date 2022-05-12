package com.reflectoring.library.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reflectoring.library.client.LibraryClient;
import com.reflectoring.library.mapper.AuditMapper;
import com.reflectoring.library.mapper.LibraryMapper;
import com.reflectoring.library.model.LibResponse;
import com.reflectoring.library.model.Status;
import com.reflectoring.library.model.mapstruct.AuditDto;
import com.reflectoring.library.model.mapstruct.BookDto;
import com.reflectoring.library.model.persistence.AuditLog;
import com.reflectoring.library.model.persistence.Book;
import com.reflectoring.library.repository.AuditRepository;
import com.reflectoring.library.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.*;

@Service
public class LibraryAuditService {

    private static final Logger log = LoggerFactory.getLogger(LibraryAuditService.class);

    private final LibraryMapper libraryMapper;

    private final AuditMapper auditMapper;

    private final AuditRepository auditRepository;

    private final LibraryClient libraryClient;

    public LibraryAuditService(LibraryMapper libraryMapper,
                               AuditMapper auditMapper, AuditRepository auditRepository, LibraryClient libraryClient) {
        this.libraryMapper = libraryMapper;
        this.auditMapper = auditMapper;
        this.auditRepository = auditRepository;
        this.libraryClient = libraryClient;
    }

    /*public void getBooksAsync(String bookRequest) {
        Call<BookDto> bookDtoCall = libraryClient.getAllBooksWithHeaders(bookRequest);
        bookDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<BookDto> call, Response<BookDto> response) {
                if (response.isSuccessful()) {
                    log.info("Success response : {}", response.body());
                } else {
                    log.info("Error response : {}", response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<BookDto> call, Throwable throwable) {
                log.info("Network error occured : {}", throwable.getLocalizedMessage());
            }
        });
    }*/

    public BookDto getBooks(String bookRequest) {
        Response<BookDto> allBooksResponse = null;
        BookDto books = null;
        try {
            AuditDto audit = null;
            allBooksResponse = libraryClient.getAllBooksWithHeaders(bookRequest).execute();
            if (allBooksResponse.isSuccessful()) {
                books = allBooksResponse.body();
                log.info("Get All Books : {}", books);
                audit = auditMapper.populateAuditLogForGetBook(books);
            } else {
                log.error("Error calling library client: {}", allBooksResponse.errorBody());
                if (Objects.nonNull(allBooksResponse.errorBody())) {
                    audit = auditMapper.populateAuditLogForException(
                            null, HttpMethod.GET, allBooksResponse.errorBody().string());
                }

            }

            if (Objects.nonNull(audit)) {
                AuditLog savedObj = auditRepository.save(libraryMapper.auditDtoToAuditLog(audit));
                log.info("Saved into audit successfully: {}", savedObj);
            }
            return books;
        } catch (Exception ex) {
            log.error("Error handling retrofit call for getAllBooks", ex);
            return books;
        }

    }

    public List<BookDto> getAllBooks() {
        List<BookDto> books = Collections.emptyList();
        try {
            AuditDto audit = null;
            Call<List<BookDto>> callBookResponse = libraryClient.getAllBooks("all");
            Response<List<BookDto>> allBooksResponse = callBookResponse.execute();
            if (allBooksResponse.isSuccessful()) {
                books = allBooksResponse.body();
                log.info("Get All Books : {}", books);
                audit = auditMapper.populateAuditLogForGet(books);
            } else {
                log.error("Error calling library client: {}", allBooksResponse.errorBody());
                if (Objects.nonNull(allBooksResponse.errorBody())) {
                    audit = auditMapper.populateAuditLogForException(
                            null, HttpMethod.GET, allBooksResponse.errorBody().string());
                }

            }

            if (Objects.nonNull(audit)) {
                AuditLog savedObj = auditRepository.save(libraryMapper.auditDtoToAuditLog(audit));
                log.info("Saved into audit successfully: {}", savedObj);
            }
            return books;
        } catch (Exception ex) {
            log.error("Error handling retrofit call for getAllBooks", ex);
            return books;
        }

    }


    public LibResponse createBook(BookDto bookDto) {
        log.info("Book DTO from POST request : {}", bookDto);
        LibResponse resp = null;
        AuditDto audit = null;
        try {
            Call<LibResponse> callLibResponse = libraryClient.createNewBook(bookDto);
            Response<LibResponse> libResponse = callLibResponse.execute();
            if (libResponse.isSuccessful()) {
                resp = libResponse.body();
                audit = auditMapper.populateAuditLogForPostAndPut(bookDto, resp, HttpMethod.POST);
            } else {
                log.error("Error calling library client: {}", libResponse.errorBody());
                if (Objects.nonNull(libResponse.errorBody())) {
                    audit = auditMapper.populateAuditLogForException(
                            new ObjectMapper().writeValueAsString(bookDto),
                            HttpMethod.POST, libResponse.errorBody().string());
                }
            }
            if (Objects.nonNull(audit)) {
                AuditLog savedObj = auditRepository.save(libraryMapper.auditDtoToAuditLog(audit));
                log.info("Saved into audit successfully: {}", savedObj);
            }
            return resp;
        } catch (Exception ex) {
            log.error("Error handling retrofit call for createNewBook", ex);
            return new LibResponse(Constants.ERROR, "Failed");
        }

    }

    public LibResponse updateBook(Long id, BookDto bookdto) {
        log.info("Book DTO from PUT request : {}", bookdto);
        LibResponse resp = null;
        AuditDto audit = null;
        try {
            Call<LibResponse> callLibResponse = libraryClient.updateBook(id, bookdto);
            Response<LibResponse> libResponse = callLibResponse.execute();
            if (libResponse.isSuccessful()) {
                resp = libResponse.body();
                audit = auditMapper.populateAuditLogForPostAndPut(bookdto, resp, HttpMethod.PUT);
            } else {
                log.error("Error calling library client: {}", libResponse.errorBody());
                if (Objects.nonNull(libResponse.errorBody())) {
                    audit = auditMapper.populateAuditLogForException(
                            new ObjectMapper().writeValueAsString(bookdto),
                            HttpMethod.POST, libResponse.errorBody().string());
                }
            }
        } catch (Exception ex) {
            log.error("Error handling retrofit call for updateBook", ex);
            resp = new LibResponse(Constants.ERROR, "Failed");
        }

        if (Objects.nonNull(audit)) {
            AuditLog savedObj = auditRepository.save(libraryMapper.auditDtoToAuditLog(audit));
            log.info("Saved into audit successfully: {}", savedObj);
        }
        return resp;
    }

    public LibResponse deleteBook(Long id) {
        LibResponse resp = null;
        AuditDto audit = null;
        try {
            Call<LibResponse> callLibResponse = libraryClient.deleteBook(id);
            Response<LibResponse> libResponse = callLibResponse.execute();

            if (libResponse.isSuccessful()) {
                resp = libResponse.body();
                audit = auditMapper.populateAuditLogForDelete(id, resp);
            } else {
                log.error("Error calling library client: {}", libResponse.errorBody());
                if (Objects.nonNull(libResponse.errorBody())) {
                    resp = new ObjectMapper().readValue(libResponse.errorBody().string(), LibResponse.class);
                    audit = auditMapper.populateAuditLogForException(
                            String.valueOf(id), HttpMethod.POST, libResponse.errorBody().string());
                }
            }
        } catch (Exception ex) {
            log.error("Error handling retrofit call for deleteBook", ex);
            resp = new LibResponse(Constants.ERROR, "Failed");
        }


        if (Objects.nonNull(audit)) {
            AuditLog savedObj = auditRepository.save(libraryMapper.auditDtoToAuditLog(audit));
            log.info("Saved into audit successfully : {}", savedObj);
        }
        return resp;
    }
}
