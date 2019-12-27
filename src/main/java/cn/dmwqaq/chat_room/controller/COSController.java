package cn.dmwqaq.chat_room.controller;

import com.alibaba.fastjson.JSON;
import com.tencent.cloud.CosStsClient;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.TreeMap;

@Controller
@RequestMapping("/cos")
public class COSController {

    private static final String SECRET_ID = "AKIDpQoxrTYvlN5aw4qOfngT1JMDpCJAxSwL";

    private static final String SECRET_KEY = "hU9kCxj7MzVuESm0LbDjAVjrtkpqfDaA";

    private static final String BUCKET = "dmwqaq-1300596096";

    private static final String REGION = "ap-shanghai";

    @GetMapping(value = "/get")
    public void get(HttpServletResponse response) throws IOException {
        TreeMap<String, Object> config = new TreeMap<>();

        try {
            // 替换为您的 SecretId
            config.put("SecretId", SECRET_ID);
            // 替换为您的 SecretKey
            config.put("SecretKey", SECRET_KEY);

            // 临时密钥有效时长，单位是秒，默认1800秒，最长可设定有效期为7200秒
            config.put("durationSeconds", 1800);

            // 换成您的 bucket
            config.put("bucket", BUCKET);
            // 换成 bucket 所在地区
            config.put("region", REGION);

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径，例子：a.jpg 或者 a/* 或者 * 。
            // 如果填写了“*”，将允许用户访问所有资源；除非业务需要，否则请按照最小权限原则授予用户相应的访问权限范围。
            config.put("allowPrefix", "*");

            // 密钥的权限列表。简单上传、表单上传和分片上传需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[] {
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传、小程序上传
                    "name/cos:PostObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);

            JSONObject credential = CosStsClient.getCredential(config);
//            //成功返回临时密钥信息，如下打印密钥信息
            response.getWriter().write(JSON.toJSONString(credential));
//            return CosStsClient.getCredential(config);
        } catch (Exception e) {
            //失败抛出异常
            throw new IllegalArgumentException("no valid secret !");
        }
    }
}
