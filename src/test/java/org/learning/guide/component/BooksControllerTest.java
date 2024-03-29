package org.learning.guide.component;

import org.junit.jupiter.api.Test;
import org.learning.guide.controller.schema.Book;
import org.learning.guide.controller.schema.Books;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BooksControllerTest extends BaseComponent {

  @Autowired
  private Environment springEnvironment;

  @Test
  void testGetBooks_emptyList() {
    getAllBooks().forEach(book -> {
      ResponseEntity<String> deleteEntity =
              testRestTemplate.exchange(RequestEntity.delete(getBooksUri(book.getId()))
                      .accept(MediaType.APPLICATION_JSON)
                      .build(), String.class);
      assertEquals(HttpStatus.NO_CONTENT, deleteEntity.getStatusCode());
    });

    ResponseEntity<Books> booksResponseEntity =
            testRestTemplate.exchange(RequestEntity.get(getBooksUri())
                    .accept(MediaType.APPLICATION_JSON)
                    .build(), Books.class);

    assertEquals(HttpStatus.OK, booksResponseEntity.getStatusCode());
    List<Book> bookList = booksResponseEntity.getBody().getBooks();
    assertEquals(0, bookList.size());
  }

  @Test
  void testCreateBooks() {
    Book book = makeDefaultBook();
    ResponseEntity<Books> booksResponseEntity =
            testRestTemplate.exchange(RequestEntity.post(getBooksUri())
                    .accept(MediaType.APPLICATION_JSON)
                    .body(book), Books.class);
    assertEquals(HttpStatus.CREATED, booksResponseEntity.getStatusCode());

    URI location = booksResponseEntity.getHeaders().getLocation();
    Book foundBook = getBook(Long.valueOf(location.toString()));
    assertEquals("Tales From the Crypt", foundBook.getBookTitle());
    assertEquals("Fiction", foundBook.getBookCategory());
  }

  @Test
  void testPutBooks() {
    ResponseEntity<Books> booksResponseEntity =
            testRestTemplate.exchange(RequestEntity.post(getBooksUri())
                    .accept(MediaType.APPLICATION_JSON)
                    .body(makeDefaultBook()), Books.class);
    assertEquals(HttpStatus.CREATED, booksResponseEntity.getStatusCode());
    URI location = booksResponseEntity.getHeaders().getLocation();
    Long bookId = Long.valueOf(location.toString());

    Book requestedBook = getBook(bookId);
    requestedBook.setBookTitle("A New Tale");
    ResponseEntity<String> updateEntity =
            testRestTemplate.exchange(RequestEntity.put(getBooksUri(bookId))
                    .accept(MediaType.APPLICATION_JSON)
                    .body(requestedBook), String.class);
    assertEquals(HttpStatus.NO_CONTENT, updateEntity.getStatusCode());

    Book updatedBook = getBook(bookId);
    assertEquals("A New Tale", updatedBook.getBookTitle());
  }

  @Test
  void testDeleteBooks() {
    Book defaultBook = makeDefaultBook();
    ResponseEntity<Books> booksResponseEntity =
            testRestTemplate.exchange(RequestEntity.post(getBooksUri())
                    .accept(MediaType.APPLICATION_JSON)
                    .body(defaultBook), Books.class);
    assertEquals(HttpStatus.CREATED, booksResponseEntity.getStatusCode());
    URI location = booksResponseEntity.getHeaders().getLocation();
    Long bookId = Long.valueOf(location.toString());

    ResponseEntity<String> deleteEntity =
            testRestTemplate.exchange(RequestEntity.delete(getBooksUri(bookId))
                    .accept(MediaType.APPLICATION_JSON)
                    .build(), String.class);
    assertEquals(HttpStatus.NO_CONTENT, deleteEntity.getStatusCode());
    Book allBooks = getBook(bookId);
    assertEquals(new Book(), allBooks);
  }

  private List<Book> getAllBooks() {
    return testRestTemplate.exchange(RequestEntity.get(getBooksUri())
            .accept(MediaType.APPLICATION_JSON)
            .build(), Books.class).getBody().getBooks();
  }

  private Book getBook(Long bookId) {
    ResponseEntity<Book> exchange = testRestTemplate.exchange(RequestEntity.get(getBooksUri(bookId))
            .accept(MediaType.APPLICATION_JSON)
            .build(), Book.class);

    assertTrue(exchange.getStatusCode().is2xxSuccessful() || exchange.getStatusCode().value() == 404);
    return exchange.getBody();
  }

  private Book makeDefaultBook() {
    Book book = new Book();
    book.setBookTitle("Tales From the Crypt");
    book.setBookCategory("Fiction");
    book.setPublishDate(Instant.now());
    book.setAuthorId(1L);
    return book;
  }

  private String getBooksUri(Long bookId) {
    return getBooksUri() + "/" + bookId;
  }

  private String getBooksUri() {
    String port = springEnvironment.getProperty("local.server.port", "8080");
    return "http://localhost:" + port + "/books";
  }
}
