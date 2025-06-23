package exercise.mapper;

import exercise.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.TargetType;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import exercise.model.BaseEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

// BEGIN
@Mapper(componentModel = "spring")
public interface ReferenceMapper {
    ReferenceMapper INSTANCE = Mappers.getMapper(ReferenceMapper.class);
    Category toCategory(Long id);
}
// END
