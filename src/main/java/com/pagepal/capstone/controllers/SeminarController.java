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
            @Argument(name = "sort") String sort
    ) {
        return seminarService.getSeminarList(page, pageSize, sort);
    }

    @QueryMapping(name = "getAllSeminarsByReaderId")
    public ListSeminarDto getAllSeminarsByReaderId(
            @Argument(name = "readerId") UUID readerId,
            @Argument(name = "page") Integer page,
            @Argument(name = "pageSize") Integer pageSize,
            @Argument(name = "sort") String sort
    ) {
        return seminarService.getSeminarListByReaderId(readerId, page, pageSize, sort);
    }

    @QueryMapping(name = "getAllSeminarsByCustomerId")
    public ListSeminarDto getAllSeminarsByCustomerId(
            @Argument(name = "customerId") UUID customerId,
            @Argument(name = "page") Integer page,
            @Argument(name = "pageSize") Integer pageSize,
            @Argument(name = "sort") String sort
    ) {
        return seminarService.getSeminarListByCustomerId(customerId, page, pageSize, sort);
    }

    @QueryMapping(name = "getAllSeminarsNotJoinByCustomerId")
    public ListSeminarDto getAllSeminarsNotJoinByCustomerId(
            @Argument(name = "customerId") UUID customerId,
            @Argument(name = "page") Integer page,
            @Argument(name = "pageSize") Integer pageSize,
            @Argument(name = "sort") String sort
    ) {
        return seminarService.getSeminarListNotJoinByCustomerId(customerId, page, pageSize, sort);
    }

    @QueryMapping(name = "getSeminarById")
    public SeminarDto getSeminarById(@Argument(name = "id") UUID id) {
        return seminarService.getSeminar(id);
    }

    @MutationMapping(name = "createSeminar")
    public SeminarDto createSeminar(
            @Argument(name = "seminar") SeminarCreateDto dto
    ) {
        return seminarService.createSeminar(dto);
    }

    @MutationMapping(name = "updateSeminar")
    public SeminarDto updateSeminar(
            @Argument(name = "readerId") UUID readerId,
            @Argument(name = "id") UUID id,
            @Argument(name = "seminar") SeminarUpdateDto dto
    ) {
        return seminarService.updateSeminar(readerId, id, dto);
    }

    @MutationMapping(name = "deleteSeminar")
    public SeminarDto deleteSeminar(@Argument(name = "id") UUID id) {
        return seminarService.deleteSeminar(id);
    }

    @MutationMapping(name = "joinSeminar")
    public SeminarBookingDto joinSeminar(
            @Argument(name = "seminarId") UUID seminarId,
            @Argument(name = "customerId") UUID customerId
    ) {
        return seminarService.bookSeminar(seminarId, customerId);
    }
}
