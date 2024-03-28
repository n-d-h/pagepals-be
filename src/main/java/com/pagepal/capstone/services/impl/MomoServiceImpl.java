package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.momo.Response;
import com.pagepal.capstone.services.MomoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MomoServiceImpl implements MomoService {

    @Value("${momo.partnerCode}")
    private String partnerCode;
    @Value("${momo.accessKey}")
    private String accessKey;
    @Value("${momo.secretKey}")
    private String secretKey;
    @Value("${momo.apiEndpoint}")
    private String endPoint;
    @Value("${momo.returnUrl}")
    private String returnUrl;
    @Value("${momo.notifyUrl}")
    private String notifyUrl;

    private String orderId = UUID.randomUUID().toString();
    private String orderInfo = "PAY WITH MOMO";
    private String requestId = UUID.randomUUID().toString();
    private String requestType = "captureWallet";
    private String extraData = "";
    private String lang = "en";
    private String partnerName = "CAR RENTAL";
    private String storeId = "MoMoStore";

    /**
     * @param amount
     * @return payment url
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    @Override
    public Object getPaymentUrl(Long amount, String orderId)
            throws InvalidKeyException,
            NoSuchAlgorithmException,
            IOException, UnsupportedEncodingException {
        requestId = UUID.randomUUID().toString();

        String requestRawData = new StringBuilder()
                .append("accessKey").append("=").append(accessKey).append("&")
                .append("amount").append("=").append(amount).append("&")
                .append("extraData").append("=").append(extraData).append("&")
                .append("ipnUrl").append("=").append(notifyUrl).append("&")
                .append("orderId").append("=").append(orderId).append("&")
                .append("orderInfo").append("=").append(orderInfo).append("&")
                .append("partnerCode").append("=").append(partnerCode).append("&")
                .append("redirectUrl").append("=").append(returnUrl).append("&")
                .append("requestId").append("=").append(requestId).append("&")
                .append("requestType").append("=").append(requestType)
                .toString();

        String signature = signHmacSHA256(requestRawData, secretKey);

        HashMap<String, String> values = new HashMap<String, String>() {
            {
                put("partnerCode", partnerCode);
                put("partnerName", partnerName);
                put("storeId", storeId);
                put("requestId", requestId);
                put("amount", String.valueOf(amount));
                put("orderId", orderId);
                put("orderInfo", orderInfo);
                put("redirectUrl", returnUrl);
                put("ipnUrl", notifyUrl);
                put("lang", lang);
                put("extraData", extraData);
                put("requestType", requestType);
                put("signature", signature);
            }
        };

        WebClient.Builder builder = WebClient.builder();

        WebClient webClient = builder.build();

        Mono<Object> result = webClient.post()
                .uri(endPoint)
                .bodyValue(values)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Object.class);

        return result.block();
    }

    /**
     * @param partnerCode
     * @param orderId
     * @param requestId
     * @param amount
     * @param orderInfo
     * @param orderType
     * @param transId
     * @param resultCode
     * @param message
     * @param payType
     * @param responseTime
     * @param extraData
     * @param signature
     * @return Response
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    @Override
    public Response reCheckAndResponseToClient(
            String partnerCode, String orderId, String requestId,
            String amount, String orderInfo, String orderType,
            String transId, String resultCode, String message,
            String payType, String responseTime, String extraData,
            String signature
    ) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String requestRawData = new StringBuilder()
                .append("accessKey").append("=").append(accessKey).append("&")
                .append("amount").append("=").append(amount).append("&")
                .append("extraData").append("=").append(extraData).append("&")
                .append("message").append("=").append(message).append("&")
                .append("orderId").append("=").append(orderId).append("&")
                .append("orderInfo").append("=").append(orderInfo).append("&")
                .append("orderType").append("=").append(orderType).append("&")
                .append("partnerCode").append("=").append(partnerCode).append("&")
                .append("payType").append("=").append(payType).append("&")
                .append("requestId").append("=").append(requestId).append("&")
                .append("responseTime").append("=").append(responseTime).append("&")
                .append("resultCode").append("=").append(resultCode).append("&")
                .append("transId").append("=").append(transId)
                .toString();

        String signRequest = signHmacSHA256(requestRawData, secretKey);

        if (!signRequest.equals(signature)) {
            Response res = Response.builder()
                    .message("INVALID SIGNATURE")
                    .status(resultCode)
                    .build();
            return res;
        }

        //Update state of booking

        return Response.builder()
                .message(message)
                .status(resultCode)
                .build();
    }

    /**
     * @param data
     * @param secretKey
     * @return signature
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     */
    public String signHmacSHA256(String data, String secretKey)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return toHexString(rawHmac);
    }

    /**
     * @param bytes
     * @return hex string
     */
    public String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        formatter.close();
        return sb.toString();
    }
}
