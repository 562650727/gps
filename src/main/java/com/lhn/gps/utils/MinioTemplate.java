package com.lhn.gps.utils;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Slf4j
public class MinioTemplate {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private int partSize;
    public MinioClient client;

    @SneakyThrows
    public MinioTemplate(String endpoint, String accessKey, String secretKey, int partSize) {
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.partSize = partSize;
        this.client = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }

    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     */
    public void createBucket(String bucketName) throws Exception {
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * @param bucketName
     * @param days
     * @param ruleName
     * @throws Exception
     */
    public void setBucketExpirationByDays(String bucketName, String prefix, Integer days, String ruleName) throws Exception {
        // 获取配置
        LifecycleConfiguration lifecycleConfiguration = client.getBucketLifecycle(GetBucketLifecycleArgs.builder().bucket(bucketName).build());
        List<LifecycleRule> rules = lifecycleConfiguration.rules();

        List<LifecycleRule> rulesNew = new ArrayList<>();
        rulesNew.addAll(rules);
        // 配置生命周期规则
        rulesNew.add(new LifecycleRule(Status.ENABLED, // 开启状态
                null, new Expiration((ZonedDateTime) null, days, null), // 保存365天
                new RuleFilter(prefix), // 目录配置
                ruleName, null, null, null));
        LifecycleConfiguration lifecycleConfigurationNew = new LifecycleConfiguration(rulesNew);
        // 添加生命周期配置
        client.setBucketLifecycle(SetBucketLifecycleArgs.builder().bucket(bucketName).config(lifecycleConfigurationNew).build());
    }

    public List<LifecycleRule> getBucketLifecycleRule(String bucketName) throws Exception {
        // 获取配置
        LifecycleConfiguration lifecycleConfiguration1111 = client.getBucketLifecycle(GetBucketLifecycleArgs.builder().bucket(bucketName).build());
        List<LifecycleRule> rules1 = lifecycleConfiguration1111.rules();
        for (LifecycleRule lifecycleRule : rules1) {
            log.debug(lifecycleRule.toString());
            log.debug("Lifecycle status is " + lifecycleRule.status() + "\nLifecycle prefix is " + lifecycleRule.filter().prefix() + "\nLifecycle expiration days is " + lifecycleRule.expiration().days());
        }
        return rules1;
    }

    /**
     * 获取全部bucket
     * <p>
     * https://docs.minio.io/cn/java-client-api-reference.html#listBuckets
     */
    public List<Bucket> getAllBuckets() throws Exception {
        return client.listBuckets();
    }

    /**
     * 根据bucketName获取信息
     *
     * @param bucketName bucket名称
     */
    public Optional<Bucket> getBucket(String bucketName) throws Exception {
        return client.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 根据bucketName删除信息
     *
     * @param bucketName bucket名称
     */
    public void removeBucket(String bucketName) throws Exception {
        client.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * 分区上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream     文件流
     * @param size       文件大小
     */
    public String putObject(String bucketName, String objectName, InputStream stream, Long size) throws Exception {
        PutObjectArgs putObjectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(stream, size, partSize).build();
        ObjectWriteResponse objectWriteResponse = client.putObject(putObjectArgs);
        return objectWriteResponse.object();
    }

    /**
     * 根据文件前置查询文件
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return MinioItem 列表
     */
    public List<Item> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) throws Exception {
        List<Item> objectList = new ArrayList<>();
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(recursive).build();

        Iterable<Result<Item>> objectsIterator = client.listObjects(listObjectsArgs);

        while (objectsIterator.iterator().hasNext()) {
            objectList.add(objectsIterator.iterator().next().get());
        }
        return objectList;
    }

    /**
     * 获取文件外链
     * 这里的 method 方法决定最后链接是什么请求获得
     * expiry 决定这个链接多久失效
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param duration   有效大小
     * @param
     * @return url
     */
    public String getObjectURL(String bucketName, String objectName, Integer duration, TimeUnit unit) throws Exception {
        if (null == duration || null == unit) {
            duration = 7;
            unit = TimeUnit.DAYS;
        }
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder().bucket(bucketName).method(Method.GET).expiry(duration, unit).object(objectName).build();

        return client.getPresignedObjectUrl(args);
    }

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucketName).object(objectName).build();
        return client.getObject(getObjectArgs);
    }

    /**
     * 上传文件 base64
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param base64Str  文件base64
     */
    public String putObject(String bucketName, String objectName, String base64Str) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(base64Str.getBytes());
        // 进行解码
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] byt = new byte[0];
        try {
            byt = base64Decoder.decodeBuffer(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputStream = new ByteArrayInputStream(byt);
        putObject(bucketName, objectName, inputStream, Long.valueOf(byt.length));
        return objectName;
    }

    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param file       文件
     * @throws Exception
     */
    public String putObject(String bucketName, String objectName, MultipartFile file) throws Exception {
        this.putObject(bucketName, objectName, file.getInputStream(), file.getSize());
        return objectName;
    }

    /**
     * 获取文件信息
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#statObject
     */
    public StatObjectResponse getObjectInfo(String bucketName, String objectName) throws Exception {
        StatObjectArgs statObjectArgs = StatObjectArgs.builder().bucket(bucketName).object(objectName).build();
        return client.statObject(statObjectArgs);
    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#removeObject
     */
    public void removeObject(String bucketName, String objectName) throws Exception {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build();
        client.removeObject(removeObjectArgs);
    }

    /**
     * 获取直传链接
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception
     */
    public String presignedPutObject(String bucketName, String objectName) throws Exception {
        GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder().method(Method.PUT).bucket(bucketName).object(objectName).expiry(7, TimeUnit.DAYS).build();
        return client.getPresignedObjectUrl(getPresignedObjectUrlArgs);
    }

    /**
     * 合并文件
     *
     * @param bucketName
     * @param chunkNames
     * @param targetObjectName
     * @return
     * @throws Exception
     */
    public String composeObject(String bucketName, List<String> chunkNames, String targetObjectName) throws Exception {
        List<ComposeSource> sources = new ArrayList<>(chunkNames.size());
        for (String chunkName : chunkNames) {
            ComposeSource composeSource = ComposeSource.builder().bucket(bucketName).object(chunkName).build();
            sources.add(composeSource);
        }
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder().bucket(bucketName).sources(sources).object(targetObjectName).build();
        ObjectWriteResponse objectWriteResponse = client.composeObject(composeObjectArgs);
        return objectWriteResponse.object();
    }
}