package com.pagepal.capstone.repositories.postgre;

import com.pagepal.capstone.entities.postgre.Reader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, UUID>{
    @Query("""
            SELECT r FROM Reader r
            LEFT JOIN FETCH r.workingTimes wt
            WHERE r.id = :id
            AND wt.date >= CURRENT_DATE
            """)
    Optional<Reader> findById(UUID id);

    Page<Reader> findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndRating(
            String name, String genre, String language, String countryAccent, Integer rating, Pageable pageable);

    Page<Reader> findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCase(
            String name, String genre, String language, String countryAccent, Pageable pageable);
}
