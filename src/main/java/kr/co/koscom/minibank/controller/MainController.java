package kr.co.koscom.minibank.controller;

import kr.co.koscom.minibank.dto.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {
    @GetMapping("/")
    public String hello() {
        return "hello";
    }

    @GetMapping("/koscom")
    public String koscom() {
        return "koscom";
    }

    @GetMapping("/books")
    public List<Book> books() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, "book1"));
        books.add(new Book(2, "book2"));
        return books;
    }
}
