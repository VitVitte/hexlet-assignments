package exercise.service;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.BookMapper;
import exercise.mapper.JsonNullableMapper;
import exercise.model.Author;
import exercise.model.Book;
import exercise.repository.AuthorRepository;
import exercise.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private JsonNullableMapper jsonNullableMapper;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга с id " + id + " не найдена"));
    }

    public Book saveBook(Book book) {
        // Проверка автора
        Long authorId = book.getAuthor().getId();
        if (authorId == null || !authorRepository.existsById(authorId)) {
            throw new IllegalArgumentException("Автор с id " + authorId + " не найден");
        }
        // Установка полного объекта автора
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Автор с id " + authorId + " не найден"));
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    public Book updateBook(Book book) {
        // Этот метод предполагает, что book уже содержит все обновленные поля
        return saveBook(book);
    }

    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Книга с id " + id + " не найдена");
        }
        bookRepository.deleteById(id);
    }
}