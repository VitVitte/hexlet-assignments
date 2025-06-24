package exercise.controller;

import java.util.List;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductParamsDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.mapper.ProductMapper;
import exercise.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.ProductRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    // BEGIN
    @GetMapping("")
    public List<ProductDTO> getProducts(
            @RequestParam(name = "priceGt", required = false) Integer priceGt,
            @RequestParam(name = "priceLt", required = false) Integer priceLt,
            @RequestParam(name = "CategoryId", required = false) Long categoryIdUpper,
            @RequestParam(name = "categoryId", required = false) Long categoryIdLower,
            @RequestParam(name = "ratingGt", required = false) Double ratingGt,
            @RequestParam(name = "titleCont", required = false) String titleCont,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        Long categoryId = null;
        if (categoryIdUpper != null) {
            categoryId = categoryIdUpper;
        } else if (categoryIdLower != null) {
            categoryId = categoryIdLower;
        }

        ProductParamsDTO params = new ProductParamsDTO();
        params.setCategoryId(categoryId);
        params.setPriceLt(priceLt);
        params.setPriceGt(priceGt);
        params.setRatingGt(ratingGt);
        params.setTitleCont(titleCont);

        var spec = ProductSpecification.getByParams(params);

        var pageable = PageRequest.of(page, 10);

        var productsPage = productRepository.findAll(spec, pageable);

        return productsPage.stream()
                .map(productMapper::map)
                .toList();
    }
// END

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    ProductDTO create(@Valid @RequestBody ProductCreateDTO productData) {
        var product = productMapper.map(productData);
        productRepository.save(product);
        var productDto = productMapper.map(product);
        return productDto;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ProductDTO show(@PathVariable Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not Found: " + id));
        var productDto = productMapper.map(product);
        return productDto;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ProductDTO update(@RequestBody @Valid ProductUpdateDTO productData, @PathVariable Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not Found: " + id));

        productMapper.update(productData, product);
        productRepository.save(product);
        var productDto = productMapper.map(product);
        return productDto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void destroy(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
