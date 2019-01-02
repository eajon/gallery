package cn.no7player.service;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class OssService {


    @Value("${gallery.file.endpoint}")
    private String END_POINT;

    @Value("${gallery.file.keyid}")
    private String ACCESS_KEY_ID;

    @Value("${gallery.file.keysecret}")
    private String ACCESS_KEY_SECRET;

    @Value("${gallery.file.bucketname}")
    private String BUCKET_NAME;

    @Value("${gallery.file.type}")
    private String READ_TYPE;


    /**
     * 生成OSSClient <>基础方法</>
     *
     * @return 访问路径 ，结果为null时说明失败
     */
    public OSSClient generateOSSClient() {
        return new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 私有方法
     * OSS上传<>基础方法</>
     *
     * @param ossClient OSSClient
     * @param map       出参
     * @param file      文件
     * @return 访问路径 ，结果为null时说明上传失败
     */
    private void upload(OSSClient ossClient, Map<String, String> map, MultipartFile file, String fileFolder, String fileHost) throws IOException {
        String key = generateKey(file, fileFolder, fileHost);
        new PutObjectRequest(BUCKET_NAME, key, file.getInputStream());
        ossClient.putObject(new PutObjectRequest(BUCKET_NAME, key, file.getInputStream()));
        String url = generateUrl(ossClient, key);
        map.put(key, url);
    }

    /**
     * 单上传文件<>基础方法</>
     *
     * @param file 文件
     * @return 访问路径 ，结果为null时说明上传失败
     */
    public Map<String, String> upload(MultipartFile file, String fileFolder, String fileHost) throws IOException {
        if (file == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        OSSClient ossClient = generateOSSClient();
        upload(ossClient, map, file, fileFolder, fileHost);
        ossClient.shutdown();
        return map;
    }


    /**
     * 多上传文件<>基础方法</>
     *
     * @param files 文件
     * @return 访问路径 ，结果为null时说明上传失败
     */
    public Map<String, String> upload(MultipartFile[] files, String fileFolder, String fileHost) throws IOException {
        if (ArrayUtils.isEmpty(files)) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        OSSClient ossClient = generateOSSClient();
        for (MultipartFile file : files) {
            upload(ossClient, map, file, fileFolder, fileHost);
        }
        ossClient.shutdown();
        return map;
    }


    /**
     * 获取文件<>基础方法</>
     *
     * @param key 文件名
     * @return 访问路径
     */
    public String generateUrl(OSSClient ossClient, String key) {
        String url;
        if (READ_TYPE.toUpperCase().equals("PRIVATE")) {
            Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 100);
            url = ossClient.generatePresignedUrl(BUCKET_NAME, key, expiration).toString();
        }else
        {
            StringBuffer sb = new StringBuffer();
            sb.append("http://").append(BUCKET_NAME).append(".").append(END_POINT).append("/").append(key);
            url = sb.toString();
        }
        return url;
    }


    /**
     * 删除单个文件<>基础方法</>
     *
     * @param key 文件名
     */
    public boolean delete(String key) {
        // 创建OSSClient实例
        OSSClient ossClient = generateOSSClient();
        // 删除Object
        ossClient.deleteObject(BUCKET_NAME, key);
        // 关闭client
        ossClient.shutdown();
        return true;

    }


    /**
     * 删除多个文件<>基础方法</>
     *
     * @param keys 多个文件名的集合
     */
    public boolean delete(List<String> keys) {
        // 创建OSSClient实例
        OSSClient ossClient = generateOSSClient();
        // 删除Objects
        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(BUCKET_NAME)
                .withKeys(keys));
        List<String> result = deleteObjectsResult.getDeletedObjects();

        // 关闭client
        ossClient.shutdown();
        return !CollectionUtils.isEmpty(result);
    }


    /**
     * 生成文件名（bucket里的唯一key）
     * 上传和删除时除了需要bucketName外还需要此值
     */
    private String generateKey(final MultipartFile file, String fileFolder, String fileHost) {
        if (file == null || file.getSize() == 0) {
            return null;
        }
        String fileName = file.getOriginalFilename();
        String suffix = "";
        if (!StringUtils.isEmpty(fileName)) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        //生成uuid,替换 - 的目的是因为后期可能会用 - 将key进行split，然后进行分类统计
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path = DateUtils.formatDate(new Date(), "yyyyMMdd") + "-" + uuid;

        if (StringUtils.isNotBlank(fileHost)) {
            path = fileHost + "-" + path;
        }
        if (StringUtils.isNotBlank(fileFolder)) {
            path = fileFolder + "/" + path;
        }

        if (suffix.startsWith(".")) {
            path = path + suffix;
        } else {
            path = path + "." + suffix;
        }

        return path;
    }
}
