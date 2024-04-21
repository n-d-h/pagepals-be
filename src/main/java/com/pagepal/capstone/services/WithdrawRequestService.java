package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.withdrawRequest.ListWithdrawRequestDto;
import com.pagepal.capstone.dtos.withdrawRequest.WithdrawQuery;
import com.pagepal.capstone.dtos.withdrawRequest.WithdrawRequestCreateDto;
import com.pagepal.capstone.dtos.withdrawRequest.WithdrawRequestReadDto;

import java.util.List;
import java.util.UUID;

public interface WithdrawRequestService {

    List<WithdrawRequestReadDto> getAllWithdrawRequests();

    ListWithdrawRequestDto getWithdrawRequestsByReaderId(UUID readerId, WithdrawQuery withdrawQuery);

    WithdrawRequestReadDto getWithdrawRequestById(UUID id);

    WithdrawRequestReadDto createWithdrawRequest(UUID readerId, WithdrawRequestCreateDto withdrawRequestCreateDto);

    WithdrawRequestReadDto acceptWithdrawRequest(UUID id, String imgUrl, UUID staffId);

    WithdrawRequestReadDto rejectWithdrawRequest(UUID id, String reason, UUID staffId);
}
