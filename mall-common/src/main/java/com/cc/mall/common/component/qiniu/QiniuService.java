package com.cc.mall.common.component.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/18 15:15
 **/
public interface QiniuService {

    /**
     * 上传文件
     */
    public String upload(byte[] file) throws QiniuException;

    /**
     * 覆盖上传
     */
    public String upload(byte[] file, String fileKey) throws QiniuException;

    /**
     * 删除文件
     */
    public Response delete(String fileKey) throws QiniuException;
}
