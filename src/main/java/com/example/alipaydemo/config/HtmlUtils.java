package com.example.alipaydemo.config;

import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


/**
 * HTML 实用程序
 *
 * @author finding
 * @date 2024/04/18
 */
@Slf4j
public class HtmlUtils {

    /**
     * 获取请求头信息
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String fetchRequest2Str(HttpServletRequest request) {
        // 初始化请求正文字符串变量，初始值为 null
        String requestBody = null;

        try (BufferedReader streamReader = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {

            // 初始化 StringBuilder 用于构建请求正文字符串
            StringBuilder bodyStringBuilder = new StringBuilder();

            // 循环读取请求流中的每一行
            String inputLine;
            while ((inputLine = streamReader.readLine()) != null) {
                // 将当前行添加到 StringBuilder，并在其后添加系统默认的换行符
                bodyStringBuilder.append(inputLine).append(System.lineSeparator());
            }

            // 构建完成的请求正文字符串，去除末尾可能存在的多余换行符
            requestBody = bodyStringBuilder.toString().trim();

            // 记录日志，输出收到的请求正文
            log.info("Received Request Stream:\n{}", requestBody);
        } catch (IOException e) {
            // 当读取请求流过程中发生异常时，记录错误日志
            log.error("Error reading request stream:", e);
        }

        // 返回请求正文字符串
        return requestBody;
    }


    /**
     * 获取请求头签名
     *
     * @param request 请求
     * @return {@link SignatureHeader}
     */
    public static SignatureHeader fetchRequest2SignatureHeader(HttpServletRequest request) {
        SignatureHeader signatureHeader = new SignatureHeader();
        signatureHeader.setSignature(request.getHeader("Wechatpay-Signature"));
        signatureHeader.setNonce(request.getHeader("Wechatpay-Nonce"));
        signatureHeader.setSerial(request.getHeader("Wechatpay-Serial"));
        signatureHeader.setTimeStamp(request.getHeader("Wechatpay-TimeStamp"));
        return signatureHeader;
    }
}

