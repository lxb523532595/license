package com.zjrj.license;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.File;

/**
 * @ProjectName LicenseCheckListener
 * @author Administrator
 * @version 1.0.0
 * @Description 在项目启动时安装证书
 * @createTime 2022/4/30 0030 18:28
 */
@Component
public class LicenseCheckListener implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger logger = LogManager.getLogger(LicenseCheckListener.class);

    /**
     * 证书subject
     */
    @Value("${license.subject}")
    private String subject;

    /**
     * 公钥别称
     */
    @Value("${license.publicAlias}")
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    @Value("${license.storePass}")
    private String storePass;

    /**
     * 证书生成路径
     */
    @Value("${license.licensePath}")
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    @Value("${license.publicKeysStorePath}")
    private String publicKeysStorePath;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //root application context 没有parent
        ApplicationContext context = event.getApplicationContext().getParent();
        if(context == null){
            if(StringUtils.isNotBlank(licensePath)){
                logger.info("++++++++ 开始安装证书 ++++++++");
                String path = this.getClass().getResource("/").getPath();
                LicenseVerifyParam param = new LicenseVerifyParam();
                param.setSubject(subject);
                param.setPublicAlias(publicAlias);
                param.setStorePass(storePass);
                param.setLicensePath(path + File.separator+"/license.lic");
                param.setPublicKeysStorePath(path + File.separator+"zjpublicCerts.keystore");

                LicenseVerify licenseVerify = new LicenseVerify();
                //安装证书
                licenseVerify.install(param);

                logger.info("++++++++ 证书安装结束 ++++++++");
            }
        }
    }
}
