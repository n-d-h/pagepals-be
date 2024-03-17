package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.googlebook.BookSearchResult;
import com.pagepal.capstone.services.GoogleBookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Transactional
@Service
public class GoogleBookServiceImpl implements GoogleBookService {

    private final WebClient webClient;
    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    public GoogleBookServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.googleapis.com/books/v1/").build();
    }

    public BookSearchResult searchBook(String query, Integer page, Integer pageSize) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("volumes")
                            .queryParam("q", query)
                            .queryParam("key", apiKey)
                            .queryParam("startIndex", (page - 1) * pageSize)
                            .queryParam("maxResults", pageSize)
                            .build())
                    .retrieve()
                    .bodyToMono(BookSearchResult.class)
                    .block();
        } catch (WebClientResponseException ex) {
            // Log the error and handle it appropriately
            System.out.printf("Error occurred while calling the Google Books API: {}", ex.getRawStatusCode());
            throw ex; // or return null, throw a custom exception, etc.
        }
    }
}
