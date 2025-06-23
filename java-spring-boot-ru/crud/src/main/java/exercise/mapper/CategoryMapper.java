package exercise.mapper;

import exercise.dto.CategoryCreateDTO;
import exercise.dto.CategoryDTO;
import exercise.model.Category;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

// BEGIN
@Mapper( componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO toDto(Category category);

    @Mapping(target = "id", ignore = true) // id генерируется автоматически
    Category toEntity(CategoryCreateDTO dto);

    void updateEntity(@MappingTarget Category entity, CategoryCreateDTO dto);
}
// END
