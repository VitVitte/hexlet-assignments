package exercise.controller;

import exercise.dto.CategoryDTO;
import exercise.mapper.CategoryMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;

import exercise.repository.CategoryRepository;
import exercise.dto.CategoryCreateDTO;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/categories")
public class CategoriesController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping(path = "")
    public List<CategoryDTO> index() {
        var categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::map)
                .toList();
    }

    @GetMapping(path = "/{id}")
    public CategoryDTO show(@PathVariable long id) {

        var category =  categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
        var categoryDTO = categoryMapper.map(category);
        return categoryDTO;
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO create(@Valid @RequestBody CategoryCreateDTO categoryData) {
        var category = categoryMapper.map(categoryData);
        categoryRepository.save(category);
        var categoryDto = categoryMapper.map(category);
        return categoryDto;
    }
}
