package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.momo.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface MomoService {

    Response reCheckAndResponseToClient(
            String partnerCode, String orderId, String requestId,
            String amount, String orderInfo, String orderType,
            String transId, String resultCode, String message,
            String payType, String responseTime, String extraData,
            String signature
    ) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException;

    Object getPaymentUrl(Long amount, String orderId)
            throws InvalidKeyException,
            NoSuchAlgorithmException,
            IOException, UnsupportedEncodingException;
}
