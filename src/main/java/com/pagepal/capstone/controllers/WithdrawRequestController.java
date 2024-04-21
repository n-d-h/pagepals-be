package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.withdrawRequest.ListWithdrawRequestDto;
import com.pagepal.capstone.dtos.withdrawRequest.WithdrawQuery;
import com.pagepal.capstone.dtos.withdrawRequest.WithdrawRequestCreateDto;
import com.pagepal.capstone.dtos.withdrawRequest.WithdrawRequestReadDto;
import com.pagepal.capstone.services.WithdrawRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class WithdrawRequestController {
    private final WithdrawRequestService withdrawRequestService;

    @MutationMapping
    public WithdrawRequestReadDto createWithdrawRequest(@Argument("readerId") UUID readerId, @Argument("input") WithdrawRequestCreateDto input) {
        return withdrawRequestService.createWithdrawRequest(readerId, input);
    }

    @MutationMapping
    public WithdrawRequestReadDto acceptWithdrawRequest(@Argument("id" )UUID id,@Argument("imgUrl") String imgUrl,@Argument("staffId") UUID staffId) {
        return withdrawRequestService.acceptWithdrawRequest(id, imgUrl, staffId);
    }

    @MutationMapping
    public WithdrawRequestReadDto rejectWithdrawRequest(@Argument("id")UUID id,
                                                        @Argument("rejectReason") String rejectReason,
                                                        @Argument("staffId") UUID staffId) {
        return withdrawRequestService.rejectWithdrawRequest(id, rejectReason, staffId);
    }

    @QueryMapping
    public WithdrawRequestReadDto withdrawRequestById(@Argument("id") UUID id) {
        return withdrawRequestService.getWithdrawRequestById(id);
    }

    @QueryMapping
    public List<WithdrawRequestReadDto> withdrawRequests() {
        return withdrawRequestService.getAllWithdrawRequests();
    }

    @QueryMapping
    public ListWithdrawRequestDto withdrawRequestsByReaderId(@Argument("readerId")UUID readerId, @Argument("query")WithdrawQuery query) {
        return withdrawRequestService.getWithdrawRequestsByReaderId(readerId, query);
    }
}
