package com.pagepal.capstone.repositories.postgre;

import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = {ReaderRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.pagepal.capstone.entities.postgre"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class ReaderRepositoryTest {
    @Autowired
    private ReaderRepository readerRepository;

    //Date
    LocalDate localDate1 = LocalDate.of(2023, 12, 21);
    LocalDate localDate2 = LocalDate.of(2022, 12, 21);
    LocalDate localDate3 = LocalDate.of(2021, 12, 21);
    Date date1 = java.sql.Date.valueOf(localDate1);
    Date date2 = java.sql.Date.valueOf(localDate2);
    Date date3 = java.sql.Date.valueOf(localDate3);

    //Reader
    Reader reader1 = new Reader(UUID.randomUUID(), "name1", 5, "genre1", "Vietnamese"
            , "accent1", "link1", "des1", null,
            null, "vid1", null, null, date1,
            null, null, Status.ACTIVE, null, null, null,
            null, null, null, null, null);

    Reader reader2 = new Reader(UUID.randomUUID(), "name2", 4, "genre2", "Vietnamese"
            , "accent2", "link2", "des2", null,
            null, "vid2", null, null, date2,
            null, null, Status.ACTIVE, null, null, null,
            null, null, null, null, null);

    Reader reader3 = new Reader(UUID.randomUUID(), "name3", 5, "genre3", "Vietnamese"
            , "accent3", "link3", "des3", null,
            null, "vid3", null, null, date3,
            null, null, Status.ACTIVE, null, null, null,
            null, null, null, null, null);


    /**
     * Method under test: {@link ReaderRepository#findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndRating(String, String, String, String, Integer, org.springframework.data.domain.Pageable)}
     */
    @Test
    void canFindByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndRating() {
        // Arrange
        readerRepository.saveAll(Arrays.asList(reader1, reader2, reader3));
        // Act
        Page<Reader> result = readerRepository.findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndRating("name", "genre", "", "", 5, PageRequest.of(0, 10));
        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertNotNull(result);
    }

    @Test
    void shouldReturnEmptyListWhenNoReaderFoundWithFindingByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndRating() {
        // Arrange
        readerRepository.saveAll(Arrays.asList(reader1, reader2, reader3));
        // Act
        Page<Reader> result = readerRepository.findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndRating("nothing", "genre", "language", "accent", 4, PageRequest.of(0, 10));
        // Assert
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertNotNull(result);
    }

    /**
     * Method under test: {@link ReaderRepository#findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCase(String, String, String, String, org.springframework.data.domain.Pageable)}
     */
    @Test
    void canFindByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCase() {
        // Arrange
        readerRepository.saveAll(Arrays.asList(reader1, reader2, reader3));
        // Act
        Page<Reader> result = readerRepository.findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCase("name", "genre", "", "", PageRequest.of(0, 10));
        // Assert
        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertNotNull(result);
    }

    @Test
    void shouldReturnEmptyListWhenNoReaderFoundWithFindingByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCase() {
        // Arrange
        readerRepository.saveAll(Arrays.asList(reader1, reader2, reader3));
        // Act
        Page<Reader> result = readerRepository.findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCase("nothing", "genre", "language", "accent", PageRequest.of(0, 10));
        // Assert
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertNotNull(result);
    }
}
