package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import exercise.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
