package exercise.component;

import java.util.List;
import java.util.stream.IntStream;

import org.instancio.Instancio;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import exercise.model.Category;
import exercise.model.Product;
import exercise.repository.CategoryRepository;
import exercise.repository.ProductRepository;
import lombok.AllArgsConstructor;
import net.datafaker.Faker;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final Faker faker;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        IntStream.range(1, 5).forEach(i -> {
            var category = Instancio.of(Category.class)
                    .ignore(Select.field(Category::getId))
                    .ignore(Select.field(Category::getProducts))
                    .supply(Select.field(Category::getName), () -> faker.commerce().department())
                    .create();

            categoryRepository.save(category);
        });

        List<Category> categories = categoryRepository.findAll();

        IntStream.range(1, 30).forEach(i -> {
            var randomIndex = faker.number().numberBetween(0, categories.size());
            var product = Instancio.of(Product.class)
                    .ignore(Select.field(Product::getId))
                    .supply(Select.field(Product::getTitle), () -> faker.commerce().productName())
                    .supply(Select.field(Product::getPrice), () -> faker.number().numberBetween(1, 100))
                    .supply(Select.field(Product::getRating), () -> faker.number().randomDouble(2, 1, 5))
                    .supply(Select.field(Product::getCategory), () -> categories.get(randomIndex))
                    .create();

            productRepository.save(product);
        });
    }
}
