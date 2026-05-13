package com.forum.utils;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
public class AliossUtil {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    /**
     * 上传文件
     *
     */

    public String uploadFile(byte[] bytes, String ObjectName) {
        //先创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try{
            ossClient.putObject(bucketName, ObjectName, new ByteArrayInputStream(bytes));
        } catch (OSSException e) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, ");
            System.out.println("but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + e.getErrorMessage());
            System.out.println("Error Code:" + e.getErrorCode());
            System.out.println("Request ID:" + e.getRequestId());
            System.out.println("Host ID:" + e.getHostId());
        }catch (ClientException ce){
            log.error("上传失败");
            System.out.println("上传失败"+ ce.getMessage());
        }finally {
            if(ossClient != null){
                ossClient.shutdown();
            }
        }

        //文件访问路径：http://bucketName.endpoint/ObjectName
        StringBuilder url = new StringBuilder("https://");
        url.append(bucketName).append(".").append(endpoint).append("/").append(ObjectName);
        log.info("文件上传成功，访问路径: {}", url);
        return url.toString();
    }
}
