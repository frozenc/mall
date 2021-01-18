package com.cc.mall.common.component.qiniu;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cc.mall.common.utils.utils.Constants;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/18 15:24
 **/
@Service
public class QiniuServiceImpl implements QiniuService{
    @Autowired
    private Auth auth;

    @Autowired
    private QiniuConfig qiniuConfig;

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private BucketManager bucketManager;

    //获取上传凭证
    //上传凭证有效截止时间。该截止时间为上传完成后，在七牛空间生成文件的校验时间，而非上传的开始时间，一般建议设置为上传开始时间 + 3600s，用户可根据具体的业务场景对凭证截止时间进行调整。
    private String getUploadToken(String keyName) {
        return auth.uploadToken(qiniuConfig.getBucketName(), keyName, Constants.QINIU_EXPIRE_SECONDS, Constants.PUT_POLICY);
    }

    @Override
    public String upload(byte[] file) throws QiniuException {
        return upload(file, null);
    }

    @Override
    public String upload(byte[] file, String fileKey) throws QiniuException {
        Response response = uploadManager.put(file, fileKey, getUploadToken(fileKey));
        //默认3次重传
        int retry = Constants.QINIU_UPLOAD_RETRY;
        while (response.needRetry() && retry > 0) {
            response = uploadManager.put(file, fileKey, getUploadToken(fileKey));
            retry --;
        }
        DefaultPutRet putRet = JSONUtil.toBean(response.bodyString(), DefaultPutRet.class);

        return qiniuConfig.getDomainName() + putRet.key;
    }

    @Override
    public Response delete(String fileKey) throws QiniuException {
        Response response = bucketManager.delete(qiniuConfig.getBucketName(), fileKey);
        //默认3次重传
        int retry = Constants.QINIU_UPLOAD_RETRY;
        while (response.needRetry() && retry > 0) {
            response = bucketManager.delete(qiniuConfig.getBucketName(), fileKey);
            retry --;
        }
        return response;
    }
}
