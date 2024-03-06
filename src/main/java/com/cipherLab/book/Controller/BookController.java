package com.cipherLab.book.Controller;

import com.cipherLab.book.Entity.Book;
import com.cipherLab.book.Exception.BookNotFoundException;
import com.cipherLab.book.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/books")
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @GetMapping("/books/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @PutMapping("/books/{id}")
    public Book updateBookById(@RequestBody Book newBook, @PathVariable Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException(id);
        } else {
            var oldBook = bookOptional.get();
            oldBook.setName(newBook.getName() != null ? newBook.getName() : oldBook.getName());
            oldBook.setGenre(newBook.getGenre() != null ? newBook.getGenre() : oldBook.getGenre());
            newBook.setLastChangeTs(LocalDateTime.now());
            return bookRepository.save(oldBook);
        }
    }

    @PostMapping("/books")
    public Book createBook(@RequestBody Book newBook) {
        newBook.setLastChangeTs(LocalDateTime.now());
        return bookRepository.save(newBook);
    }

    @DeleteMapping("/books/{id}")
    void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}
