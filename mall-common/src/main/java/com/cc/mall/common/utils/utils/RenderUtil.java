package com.cc.mall.common.utils.utils;

import cn.hutool.json.JSONUtil;
import com.cc.mall.common.utils.api.CommonResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RenderUtil {

    public static <T> void render(HttpServletResponse response, CommonResult<T> result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = cors(response).getOutputStream();
        String str = JSONUtil.toJsonStr(result);
        out.write(str.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }

    public static void render(HttpServletResponse response, String content) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        OutputStream out = cors(response).getOutputStream();
        out.write(content.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }

    // 允许跨域
    private static HttpServletResponse cors(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Header", "*");
        response.addHeader("Access-Control-Allow-Method", "*");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Max-Age", "86400");
        return response;
    }
}
