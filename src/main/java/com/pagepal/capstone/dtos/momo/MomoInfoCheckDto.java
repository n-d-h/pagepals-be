package com.pagepal.capstone.dtos.momo;

import lombok.*;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MomoInfoCheckDto {
    String partnerCode;
    String orderId;
    String requestId;
    String amount;
    String orderInfo;
    String orderType;
    String transId;
    String resultCode;
    String message;
    String payType;
    String responseTime;
    Optional<String> extraData;
    String signatur;
}
