package com.pagepal.capstone.dtos.momo;

import lombok.*;

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
    String extraData;
    String signature;
}
