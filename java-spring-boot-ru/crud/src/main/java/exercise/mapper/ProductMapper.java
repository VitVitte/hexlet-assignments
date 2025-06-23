package exercise.mapper;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.model.Category;
import exercise.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// BEGIN
@Mapper(
        componentModel = "spring",
        uses = {JsonNullableMapper.class}
)
public abstract class ProductMapper {

    @Autowired
    protected JsonNullableMapper jsonNullableMapper;

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    public abstract ProductDTO toDto(Product product);

    public Product toEntity(ProductCreateDTO dto, Category category) {
        Product product = new Product();
        product.setCategory(category);
        product.setTitle(dto.getTitle());
        product.setPrice(dto.getPrice());
        return product;
    }

    public void updateEntity(Product product, ProductUpdateDTO dto, Category category) {
        if (dto == null) {
            return;
        }

        if (jsonNullableMapper.isPresent(dto.getCategoryId())) {
            product.setCategory(category);
        }
        if (jsonNullableMapper.isPresent(dto.getTitle())) {
            product.setTitle(dto.getTitle().get());
        }
        if (jsonNullableMapper.isPresent(dto.getPrice())) {
            product.setPrice(dto.getPrice().get());
        }
    }
}
// END
