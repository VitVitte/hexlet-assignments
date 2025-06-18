package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import exercise.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
