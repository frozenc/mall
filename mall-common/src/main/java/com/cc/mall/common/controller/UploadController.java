package com.cc.mall.common.controller;

import com.cc.mall.common.component.qiniu.QiniuService;
import com.cc.mall.common.utils.api.CommonResult;
import com.cc.mall.common.utils.api.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/18 16:09
 **/
@Api(tags = "上传文件")
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private QiniuService qiniuService;

    @ApiOperation("上传文件")
    @PostMapping("")
    public CommonResult<String> upload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String path = qiniuService.upload(file.getBytes());
                return CommonResult.success(path);
            } catch (IOException e) {
                return CommonResult.failed(ResultCode.FILE_UPLOAD_ERROR);
            }
        }
        return CommonResult.failed(ResultCode.FILE_UPLOAD_EMPTY);
    }
}
