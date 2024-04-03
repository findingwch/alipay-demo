package com.example.alipaydemo.config;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.example.alipaydemo.base.enums.ResultEnum;
import com.example.alipaydemo.base.exception.CustomizeException;
import com.example.alipaydemo.entity.PaymentInfo;
import com.example.alipaydemo.service.OrderService;
import com.example.alipaydemo.vo.PayVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付宝模板
 *
 * @author finding
 * @date 2024/04/02
 */
@ConfigurationProperties(prefix = "alipay")
@Component
@Data
@Slf4j
public class AlipayTemplate {

    //穿透地址
    public String URL;

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public String app_id;

    // 商户私钥，您的PKCS8格式RSA2私钥
    public String merchant_private_key;

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public String alipay_public_key;

    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    public String notify_url;

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    public String return_url;

    // 签名方式
    private String sign_type;

    // 字符编码格式
    private String charset;

    //订单超时时间
    private String timeout = "1m";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    public String gatewayUrl;


    /**
     * 支付
     *
     * @param vo 配音
     * @return {@link String}
     * @throws AlipayApiException 支付宝API异常
     */
    public String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);

        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

        // 我的订单编号 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //订单金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();
        //统一收单下单并支付页面接口
        alipayRequest.setBizContent(
                "{\"out_trade_no\":\"" + out_trade_no + "\","
                        + "\"total_amount\":\"" + total_amount + "\","
                        + "\"subject\":\"" + subject + "\","
                        + "\"body\":\"" + body + "\","
                        + "\"timeout_express\":\"" + timeout + "\","
                        + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}"); // 固定配置

        //返回请求体
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应：" + result);

        return result;

    }


    /**
     * 退款
     *
     * @param payVo 支付 VO
     * @throws CustomizeException 自定义错误
     */
    public void returnPay(PaymentInfo payVo) throws CustomizeException {

        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //构建参数
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.set("trade_no", payVo.getAlipayTradeNo());  // 支付宝回调的订单流水号
        bizContent.set("refund_amount", payVo.getTotalAmount());  // 订单的总金额
        bizContent.set("out_request_no", payVo.getOrderSn());   //  我的订单编号
        request.setBizContent(bizContent.toString());

        // 3. 执行请求
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
        }

        if (response != null && !response.isSuccess()) {
            log.error("退款api调用失败");
            throw new CustomizeException(ResultEnum.REQUIRE_ILLEGAL);
        }

        log.debug("退款api调用成功");

    }
}
