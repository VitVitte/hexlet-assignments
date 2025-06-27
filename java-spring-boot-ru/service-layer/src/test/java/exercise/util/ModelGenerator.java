package exercise.util;

import exercise.model.Author;
import exercise.model.Book;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ModelGenerator {
    private Model<Book> bookModel;
    private Model<Author> authorModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {
        authorModel = Instancio.of(Author.class)
                .ignore(Select.field(Author::getId))
                .ignore(Select.field(Author::getBooks))
                .supply(Select.field(Author::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(Author::getLastName), () -> faker.name().lastName())
                .toModel();


        bookModel = Instancio.of(Book.class)
                .ignore(Select.field(Book::getId))
                .supply(Select.field(Book::getTitle), () -> faker.book().title())
                .toModel();
    }
}
