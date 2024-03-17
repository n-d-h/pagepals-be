package com.pagepal.capstone.repositories.postgre;

import com.pagepal.capstone.entities.postgre.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

}
