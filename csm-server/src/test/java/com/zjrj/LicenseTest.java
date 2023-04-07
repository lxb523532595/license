package com.zjrj;

import com.zjrj.license.LicenseCheckModel;
import com.zjrj.license.LicenseCreator;
import com.zjrj.license.LicenseCreatorParam;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sun.applet.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0.0
 * @ProjectName qingfeng-license
 * @Description TODO
 * @createTime 2022年04月30日 21:27:00
 */
@SpringBootTest
public class LicenseTest {

    /**
     * {
     * 	"subject": "license_demo",
     * 	"privateAlias": "privateKey",
     * 	"keyPass": "private_password1234",
     * 	"storePass": "public_password1234",
     * 	"licensePath": "D:/license/license.lic",
     * 	"privateKeysStorePath": "D:/license/privateKeys.keystore",
     * 	"issuedTime": "2022-04-10 00:00:01",
     * 	"expiryTime": "2022-05-31 23:59:59",
     * 	"consumerType": "User",
     * 	"consumerAmount": 1,
     * 	"description": "这是证书描述信息",
     * 	"licenseCheckModel": {
     * 		"ipAddress": [],
     * 		"macAddress": [],
     * 		"cpuSerial": "",
     * 		"mainBoardSerial": ""
     *        }
     * }
     */
    @Test
    public void licenseCreate() {
        // 生成license需要的一些参数
        System.out.println("*************************生成文件开始");
        LicenseCreatorParam param = new LicenseCreatorParam();
        param.setSubject("license_zj");
        param.setPrivateAlias("zjprivateKey");
        param.setKeyPass("lixb123456");
        param.setStorePass("zjrj8888");
        String path = LicenseCreator.class.getResource("/").getPath();
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
        licenseCreator.generateLicense();
        System.out.println("*************************生成文件结束");
    }

}
