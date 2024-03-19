package com.pagepal.capstone.controllers;

import com.pagepal.capstone.services.MomoService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MomoController {
    private final MomoService momoService;

//    @QueryMapping
//    public ResponseEntity<?> momoInfo(
//            @RequestParam String partnerCode,
//            @RequestParam String orderId,
//            @RequestParam String requestId,
//            @RequestParam String amount,
//            @RequestParam String orderInfo,
//            @RequestParam String orderType,
//            @RequestParam String transId,
//            @RequestParam String resultCode,
//            @RequestParam String message,
//            @RequestParam String payType,
//            @RequestParam String responseTime,
//            @RequestParam Optional<String> extraData,
//            @RequestParam String signature
//    ) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
//        Response res = momoService.reCheckAndResponseToClient(
//                partnerCode,orderId, requestId,
//                amount, orderInfo, orderType,
//                transId, resultCode, message,
//                payType, responseTime, extraData.orElse(""), signature);
//        log.info("res: {}", res);
//        log.info(Momo.builder()
//                .partnerCode(partnerCode)
//                .orderId(orderId)
//                .requestId(requestId)
//                .amount(amount)
//                .orderInfo(orderInfo)
//                .orderType(orderType)
//                .transId(transId)
//                .resultCode(resultCode)
//                .message(message)
//                .payType(payType)
//                .responseTime(responseTime)
//                .extraData(extraData.orElse(""))
//                .signature(signature)
//                .build().toString());
//        if(res.getStatus().equals("0")) {
//            return ResponseEntity.status(HttpStatus.FOUND)
//                    .location(URI.create("http://localhost:3000/"))
//                    .build();
//        }
//        return ResponseEntity.ok(res);
//    }
//
//    @QueryMapping
//    public ResponseEntity<?> createOrder(@RequestParam Long amount, @RequestParam String orderId)
//            throws InvalidKeyException,
//            NoSuchAlgorithmException,
//            UnsupportedEncodingException, IOException {
//        Object object = momoService.getPaymentUrl(amount, orderId);
//        return ResponseEntity.ok(object);
//    }
}
