package com.zjrj.controller;

import com.zjrj.license.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;

/**
 * @ProjectName LicenseCreatorController
 * @author Administrator
 * @version 1.0.0
 * @Description 于生成证书文件，不能放在给客户部署的代码里
 * @createTime 2022/4/30 0030 18:13
 */
@RestController
@RequestMapping("/license")
public class LicenseCreatorController {

    /**
     * 证书生成路径
     */
    @Value("${license.licensePath}")
    private String licensePath;

    /**
     * @title 获取服务器硬件信息
     * @description @param osName 操作系统类型，如果为空则自动判断
     * @author Administrator
     * @updateTime 2022/4/30 0030 18:14
     */
    @RequestMapping(value = "/getServerInfos")
    public LicenseCheckModel getServerInfos(@RequestParam(value = "osName",required = false) String osName) {
        //操作系统类型
        if(StringUtils.isBlank(osName)){
            osName = System.getProperty("os.name");
        }
        osName = osName.toLowerCase();

        AbstractServerInfos abstractServerInfos = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        }else{//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }

        return abstractServerInfos.getServerInfos();
    }


    /**
     * @title 生成证书
     * @description
     * {
     *     "result": "ok",
     *     "msg": {
     *         "subject": "license_demo",
     *         "privateAlias": "privateKey",
     *         "keyPass": "private_password1234",
     *         "storePass": "public_password1234",
     *         "licensePath": "D:/license/license.lic",
     *         "privateKeysStorePath": "D:/license/privateKeys.keystore",
     *         "issuedTime": "2022-04-10 00:00:01",
     *         "expiryTime": "2022-05-31 23:59:59",
     *         "consumerType": "User",
     *         "consumerAmount": 1,
     *         "description": "这是证书描述信息",
     *         "licenseCheckModel": {
     *             "ipAddress": [],
     *             "macAddress": [],
     *             "cpuSerial": "",
     *             "mainBoardSerial": ""
     *         }
     *     }
     * }
     * @author Administrator
     * @updateTime 2022/4/30 0030 18:14
     */
    @RequestMapping(value = "/generateLicense")
    public Map<String,Object> generateLicense(LicenseCreatorParam param) {
        Map<String,Object> resultMap = new HashMap<>(2);

        System.out.println("*************************生成文件开始");
        if(param==null){
            param = new LicenseCreatorParam();
        }
        if(StringUtils.isBlank(param.getLicensePath())){
            param.setLicensePath(licensePath);
        }

        param.setSubject("license_zj");
        param.setPrivateAlias("zjprivateKey");
        param.setKeyPass("lixb123456");
        param.setStorePass("zjrj8888");
        String path = LicenseCreatorController.class.getResource("/").getPath();
        System.out.println(path);
        param.setLicensePath(path+ File.separator+"license.lic");
        param.setPrivateKeysStorePath(path+ File.separator+"zjprivateKey.keystore");
        Calendar issueCalendar = Calendar.getInstance();
        param.setIssuedTime(issueCalendar.getTime());
        Calendar expiryCalendar = Calendar.getInstance();
        expiryCalendar.set(2025, Calendar.UNDECIMBER, 31, 23, 59, 59);
        param.setExpiryTime(expiryCalendar.getTime());
        param.setConsumerType("user");
        param.setConsumerAmount(1);
        param.setDescription("这是证书描述信息");
        //自定义需要校验的License参数
        LicenseCheckModel licenseCheckModel = new LicenseCheckModel();
        licenseCheckModel.setCpuSerial("");
        licenseCheckModel.setMainBoardSerial("");
        List<String> ipList = new ArrayList<>();
        ipList.add("192.168.0.78");
        licenseCheckModel.setIpAddress(ipList);
        List<String> macList = new ArrayList<>();
        macList.add("8C-EC-4B-C2-23-39");
        licenseCheckModel.setMacAddress(macList);
        param.setLicenseCheckModel(licenseCheckModel);
        // 生成license
        LicenseCreator licenseCreator = new LicenseCreator(param);
        boolean result = licenseCreator.generateLicense();

        if(result){
            resultMap.put("result","ok");
            resultMap.put("msg",param);
        }else{
            resultMap.put("result","error");
            resultMap.put("msg","证书文件生成失败！");
        }

        return resultMap;
    }

}
