package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.enums.Status;
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
            """)
    Optional<Reader> findById(UUID id);

    Optional<Reader> findByIdAndStatus(UUID id, Status status);

    @Query("""
        SELECT r FROM Reader r
        WHERE LOWER(r.nickname) LIKE %:nickname%
        AND LOWER(r.genre) LIKE %:genre%
        AND LOWER(r.language) LIKE %:language%
        AND LOWER(r.countryAccent) LIKE %:countryAccent%
        AND r.rating = :rating
        AND r.account.accountState.name = :state
        """)
    Page<Reader> findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndRatingAndAccountState(
            String nickname, String genre, String language, String countryAccent, Integer rating, String state, Pageable pageable);

    @Query("""
        SELECT r FROM Reader r
        WHERE LOWER(r.nickname) LIKE %:nickname%
        AND LOWER(r.genre) LIKE %:genre%
        AND LOWER(r.language) LIKE %:language%
        AND LOWER(r.countryAccent) LIKE %:countryAccent%
        AND r.account.accountState.name = :state
        """)
    Page<Reader> findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndAccountState(
            String nickname, String genre, String language, String countryAccent, String state, Pageable pageable);

    List<Reader> findTop10ByAccountInOrderByRatingDesc(List<Account> account);

    List<Reader> findTop8ByAccountInOrderByRatingDesc(List<Account> account);

    Optional<Reader> findByReaderUpdateReferenceId(UUID id);

    Page<Reader> findByReaderUpdateReferenceIdIsNotNullAndAccountIsNull(Pageable pageable);

}
