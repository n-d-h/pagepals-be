package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.seminar.*;
import com.pagepal.capstone.services.SeminarService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SeminarController {
    private final SeminarService seminarService;

    @QueryMapping(name = "getAllSeminars")
    public ListSeminarDto getAllSeminars(
            @Argument(name = "page") Integer page,
            @Argument(name = "pageSize") Integer pageSize,
            @Argument(name = "sort") String sort,
            @Argument(name = "state") String state
    ) {
        return null;
    }

    @QueryMapping(name = "getAllSeminarsByReaderId")
    public ListSeminarDto getAllSeminarsByReaderId(
            @Argument(name = "readerId") UUID readerId,
            @Argument(name = "page") Integer page,
            @Argument(name = "pageSize") Integer pageSize,
            @Argument(name = "sort") String sort,
            @Argument(name = "state") String state,
            @Argument(name = "isCustomer") Boolean isCustomer
    ) {
        return null;
    }

    @QueryMapping(name = "getAllSeminarsByCustomerId")
    public ListSeminarDto getAllSeminarsByCustomerId(
            @Argument(name = "customerId") UUID customerId,
            @Argument(name = "page") Integer page,
            @Argument(name = "pageSize") Integer pageSize,
            @Argument(name = "sort") String sort
    ) {
        return null;
    }

    @QueryMapping(name = "getAllSeminarsNotJoinByCustomerId")
    public ListSeminarDto getAllSeminarsNotJoinByCustomerId(
            @Argument(name = "customerId") UUID customerId,
            @Argument(name = "page") Integer page,
            @Argument(name = "pageSize") Integer pageSize,
            @Argument(name = "sort") String sort,
            @Argument(name = "state") String state
    ) {
        return null;
    }

    @QueryMapping(name = "getSeminarById")
    public SeminarDto getSeminarById(@Argument(name = "id") UUID id) {
        return null;
    }

    @MutationMapping(name = "createSeminar")
    public SeminarDto createSeminar(
            @Argument(name = "seminar") SeminarCreateDto dto
    ) {
        return null;
    }

    @MutationMapping(name = "updateSeminar")
    public SeminarDto updateSeminar(
            @Argument(name = "readerId") UUID readerId,
            @Argument(name = "id") UUID id,
            @Argument(name = "seminar") SeminarUpdateDto dto
    ) {
        return null;
    }

    @MutationMapping(name = "deleteSeminar")
    public SeminarDto deleteSeminar(@Argument(name = "id") UUID id) {
        return null;
    }

    @MutationMapping(name = "joinSeminar")
    public SeminarBookingDto joinSeminar(
            @Argument(name = "seminarId") UUID seminarId,
            @Argument(name = "customerId") UUID customerId
    ) {
        return null;
    }

    @MutationMapping(name = "completeSeminar")
    public SeminarDto completeSeminar(@Argument(name = "seminarId") UUID seminarId) {
        return null;
    }
}
