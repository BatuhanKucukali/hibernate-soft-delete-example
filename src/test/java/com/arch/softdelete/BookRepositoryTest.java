package com.arch.softdelete;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    void delete_ShouldUpdateDeletedColumnTrue() {
        // arrange
        Book book = bookRepository.save(new Book("Guns, Germs, and Steel"));

        // act
        bookRepository.delete(book);

        // assertion
        Query query = entityManager.createNativeQuery("select * from book where id = :id", Book.class);
        query.setParameter("id", book.getId());
        Book savedBook = (Book) query.getSingleResult();

        assertTrue(savedBook.isDeleted());
    }

    @Test
    void findById_ShouldReturnEmtpy_WhenDeletedColumnIsTrue() {
        // arrange
        Book book = bookRepository.save(new Book("Guns, Germs, and Steel"));
        bookRepository.delete(book);

        // act
        Optional<Book> result = bookRepository.findById(book.getId());

        // assertion
        assertTrue(result.isEmpty());

        Query query = entityManager.createNativeQuery("select * from book where id = :id", Book.class);
        query.setParameter("id", book.getId());
        Book savedBook = (Book) query.getSingleResult();

        assertNotNull(savedBook);
    }
}
