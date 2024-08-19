package com.example.alipaydemo.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * WX Pay 配置
 *
 * @author finding
 * @date 2024/04/18
 */
@Configuration
@ConditionalOnClass(WxPayService.class)
@EnableConfigurationProperties(WxPayPropertiesV3.class)
@AllArgsConstructor
public class WxPayConfiguration {

    private WxPayPropertiesV3 wxPayPropertiesV3;

    @Bean
    @ConditionalOnMissingBean
    public WxPayService wxService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(this.wxPayPropertiesV3.getAppId()));
        payConfig.setMchId(StringUtils.trimToNull(this.wxPayPropertiesV3.getMchId()));

        payConfig.setApiV3Key(StringUtils.trimToNull(this.wxPayPropertiesV3.getApiV3Key()));
        payConfig.setPrivateCertPath(StringUtils.trimToNull(this.wxPayPropertiesV3.getPrivateCertPath()));
        payConfig.setPrivateKeyPath(StringUtils.trimToNull(this.wxPayPropertiesV3.getPrivateKeyPath()));
        payConfig.setNotifyUrl(StringUtils.trimToNull(this.wxPayPropertiesV3.getNotifyUrl()));

        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);

        // 微信支付接口请求实现类，默认使用Apache HttpClient实现
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

}

