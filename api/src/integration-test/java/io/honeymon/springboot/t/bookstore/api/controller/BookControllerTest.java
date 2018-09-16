package io.honeymon.springboot.t.bookstore.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import io.honeymon.springboot.t.bookstore.core.domain.book.BookRepository;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.honeymon.springboot.t.bookstore.api.service.book.BookService;
import io.honeymon.springboot.t.bookstore.core.domain.book.Book;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookControllerTest {
    private MockMvc mockMvc;
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    WebApplicationContext wac;
    @MockBean
    BookService bookService;

    @Before
    public void setUp() {
        //mockBookController 내에 Mock 처리된 bookService를 주입하기 위해 반드시 선언해줘야 함
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(print())
                .build();
    }

    @Test
    public void testOptions() throws Exception {
        this.mockMvc.perform(options("/books").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("books-options"));
    }

    @Test
    public void testHead() throws Exception {
        Long id = 1L;
        when(bookService.findById(id)).thenReturn(Optional.of(new Book("test-book", "test-isbn13", "test-isbn10")));

        this.mockMvc.perform(head("/books/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(document("books-head"));
    }
    
    @Test
    public void testGet() throws Exception {
        Long id = 1L;
        when(bookService.findById(1L)).thenReturn(Optional.of(new Book("test-book", "test-isbn13", "test-isbn10")));

        this.mockMvc.perform(get("/books/{id}", id).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", Is.is("test-book")))
                .andExpect(jsonPath("$.data.isbn13", Is.is("test-isbn13")))
                .andExpect(jsonPath("$.data.isbn10", Is.is("test-isbn10")))
                .andDo(document("books-get"));
    }
}
