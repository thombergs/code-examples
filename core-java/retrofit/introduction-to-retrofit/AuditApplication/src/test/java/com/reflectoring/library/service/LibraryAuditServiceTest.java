package com.reflectoring.library.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reflectoring.library.client.LibraryClient;
import com.reflectoring.library.mapper.AuditMapper;
import com.reflectoring.library.mapper.LibraryMapper;
import com.reflectoring.library.model.LibResponse;
import com.reflectoring.library.model.Status;
import com.reflectoring.library.model.mapstruct.BookDto;
import com.reflectoring.library.repository.AuditRepository;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Response;
import retrofit2.mock.Calls;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LibraryAuditServiceTest {

    private static final Logger log = LoggerFactory.getLogger(LibraryAuditServiceTest.class);

    @Autowired
    private LibraryMapper libraryMapper;

    @Autowired
    private AuditMapper auditMapper;

    @Mock
    private LibraryClient libraryClient;

    @Mock
    private AuditRepository auditRepository;

    private LibraryAuditService libraryAuditService;

    @Before
    public void setup() {
        libraryAuditService = new LibraryAuditService(libraryMapper, auditMapper, auditRepository, libraryClient);
    }


    @Test
    @DisplayName("Successful getAllBooks call")
    public void getAllBooksTest() throws Exception {
        String booksResponse = getBooksResponse("/response/getAllBooks.json");
        List<BookDto> bookDtoList =
                new ObjectMapper().readValue(booksResponse, new TypeReference<>() {
                });
        when(libraryClient.getAllBooks("all")).thenReturn(Calls.response(bookDtoList));
        doReturn(null).when(auditRepository).save(any());
        List<BookDto> allBooks = libraryAuditService.getAllBooks();
        assertAll(
                () -> assertNotNull(allBooks),
                () -> assertTrue(allBooks.size()>0)
        );

    }

    @Test
    @DisplayName("Successful call to get a specific book")
    public void getRequestBookTest() throws Exception {
        String booksResponse = getBooksResponse("/response/getOneBook.json");
        BookDto bookDto = new ObjectMapper().readValue(booksResponse, BookDto.class);
        when(libraryClient.getAllBooksWithHeaders("1")).thenReturn(Calls.response(bookDto));
        doReturn(null).when(auditRepository).save(any());
        BookDto book = libraryAuditService.getBooks("1");
        assertAll(
                () -> assertNotNull(book),
                () -> assertTrue(book.getId()==1)
        );

    }

    @Test
    @DisplayName("Successful call to create a book")
    public void postBookRequestTest() throws Exception {
        String booksResponse = getBooksResponse("/request/postBook.json");
        BookDto bookDto = new ObjectMapper().readValue(booksResponse, BookDto.class);
        LibResponse response = new LibResponse("Success", "Book created successfully");
        when(libraryClient.createNewBook(bookDto)).thenReturn(Calls.response(response));
        doReturn(null).when(auditRepository).save(any());
        LibResponse libResponse = libraryAuditService.createBook(bookDto);
        assertAll(
                () -> assertNotNull(libResponse),
                () -> assertTrue(libResponse.getResponseCode().equals("Success"))
        );

    }

    @Test
    @DisplayName("Successful call to update a book")
    public void putBookRequestTest() throws Exception {
        String booksResponse = getBooksResponse("/request/putBook.json");
        BookDto bookDto = new ObjectMapper().readValue(booksResponse, BookDto.class);
        LibResponse response = new LibResponse("Success", "Book updated successfully");
        when(libraryClient.updateBook(Long.valueOf("1"), bookDto)).thenReturn(Calls.response(response));
        doReturn(null).when(auditRepository).save(any());
        LibResponse libResponse = libraryAuditService.updateBook(Long.valueOf("1"), bookDto);
        assertAll(
                () -> assertNotNull(libResponse),
                () -> assertTrue(libResponse.getResponseCode().equals("Success"))
        );

    }

    @Test
    @DisplayName("Cannot delete a book")
    public void deleteBookRequestTest() throws Exception {
        LibResponse response = new LibResponse(Status.ERROR.toString(), "Could not delete book for id : 1000");
        ResponseBody respBody = ResponseBody.create(MediaType.parse("application/json"),
                new ObjectMapper().writeValueAsString(response));
        Response<LibResponse> respLib = Response.error(500, respBody);
        when(libraryClient.deleteBook(Long.valueOf("1000"))).thenReturn(Calls.response(respLib));
        doReturn(null).when(auditRepository).save(any());
        LibResponse libResponse = libraryAuditService.deleteBook(Long.valueOf("1000"));
        assertAll(
                () -> assertNotNull(libResponse),
                () -> assertTrue(libResponse.getResponseCode().equals("Error"))
        );

    }


    private String getBooksResponse(String path) throws IOException {
        return IOUtils.toString(this.getClass().getResourceAsStream(path));
    }

}
