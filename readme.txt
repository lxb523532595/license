TrueLicense 实现证书License的授权和验证
在jdk路径下执行如下keytool.jar命令，如下生成证书。
（1）生成密钥库
keytool -genkeypair -keysize 1024 -validity 3650 -alias "zjprivateKey" -keystore "zjprivateKey.keystore" -storepass "zjrj8888" -keypass "lixb123456" -dname "CN=localhost, OU=localhost, O=localhost, L=SH, ST=SH, C=CN"

（2）生成证书文件
keytool -exportcert -alias "zjprivateKey" -keystore "zjprivateKey.keystore" -storepass "zjrj8888" -file "certfile.cer"

#导入命令
keytool -import -alias "zjpublicCert" -file "certfile.cer" -keystore "zjpublicCerts.keystore" -storepass "zjrj8888"