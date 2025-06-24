package exercise.util;

import exercise.model.Category;
import exercise.model.Product;
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
    private Model<Product> productModel;
    private Model<Category> categoryModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {
        categoryModel = Instancio.of(Category.class)
                .ignore(Select.field(Category::getId))
                .ignore(Select.field(Category::getProducts))
                .supply(Select.field(Category::getName), () -> faker.name().fullName())
                .toModel();


        productModel = Instancio.of(Product.class)
                .ignore(Select.field(Product::getId))
                .supply(Select.field(Product::getTitle), () -> faker.brand().car())
                .supply(Select.field(Product::getPrice), () -> faker.number().positive())
                .toModel();
    }
}
