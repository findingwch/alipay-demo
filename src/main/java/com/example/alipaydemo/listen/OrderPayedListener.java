package com.example.alipaydemo.listen;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.example.alipaydemo.config.AlipayTemplate;
import com.example.alipaydemo.service.OrderService;
import com.example.alipaydemo.vo.PayAsyncVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单支付成功监听器
 *
 * @author Finding
 * @date 2022/3/23
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderPayedListener {


    private final AlipayTemplate alipayTemplate;


    private final OrderService orderService;


    /**
     * 支付宝异步通知
     *
     * @param asyncVo 支付包异步通知响应的数据
     * @param request HTTP servlets 对象
     * @throws AlipayApiException ali-pay-api
     */
    @PostMapping(value = "/payed/notify")
    public String handlerAliPay(PayAsyncVo asyncVo, HttpServletRequest request) throws AlipayApiException {
        // 只要收到支付宝的异步通知，返回 success 支付宝便不再通知
        // 获取支付宝POST过来的反馈信息
        //TODO 这里需要验签
        Map<String, String> params = new HashMap<>();
        //获取全部响应信息
        Map<String, String[]> requestParams = request.getParameterMap();
        //遍历map
        for (String name : requestParams.keySet()) {
            //获取value
            String[] values = requestParams.get(name);
            /*
              全部数据使用string存储使用 , 分割
             */
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        /*
          调用SDK验证签名
         */
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayTemplate.getAlipay_public_key(),
                alipayTemplate.getCharset(), alipayTemplate.getSign_type());

        if (signVerified) {
            System.out.println("签名验证成功...");
            //TODO 去修改订单状态
            return orderService.handlePayResult(asyncVo);
        } else {
            System.out.println("签名验证失败...");
            return "error";
        }

    }


}
