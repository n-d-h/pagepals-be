package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.momo.MomoInfoCheckDto;
import com.pagepal.capstone.dtos.momo.Response;
import com.pagepal.capstone.services.MomoService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequiredArgsConstructor
public class MomoController {
    private final MomoService momoService;

    @MutationMapping
    public String checkPaymentMomo(@Argument(name ="info") MomoInfoCheckDto momoInfoCheckDto)
            throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Response res = momoService.reCheckAndResponseToClient(
                momoInfoCheckDto.getPartnerCode(),momoInfoCheckDto.getOrderId(), momoInfoCheckDto.getRequestId(),
                momoInfoCheckDto.getAmount(), momoInfoCheckDto.getOrderInfo(), momoInfoCheckDto.getOrderType(),
                momoInfoCheckDto.getTransId(), momoInfoCheckDto.getResultCode(), momoInfoCheckDto.getMessage(),
                momoInfoCheckDto.getPayType(), momoInfoCheckDto.getResponseTime(),
                momoInfoCheckDto.getExtraData(),
                momoInfoCheckDto.getSignature());

        if(res.getStatus().equals("0")) {
            return "Success";
        }
        return "Fail";
    }

    @MutationMapping
    public Object createOrder(@Argument(name = "amount") Integer amount, @Argument(name = "customerId") String customerId)
            throws InvalidKeyException,
            NoSuchAlgorithmException,
            UnsupportedEncodingException, IOException {
        Object object = momoService.getPaymentUrl(amount, customerId);
        return object;
    }

    @MutationMapping
    public Object createOrderMobile(@Argument(name = "amount") Integer amount, @Argument(name = "customerId") String customerId)
            throws InvalidKeyException,
            NoSuchAlgorithmException,
            UnsupportedEncodingException, IOException {
        Object object = momoService.getPaymentUrlMobile(amount, customerId);
        return object;
    }
}
