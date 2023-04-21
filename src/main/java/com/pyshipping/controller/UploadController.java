package com.pyshipping.controller;

import com.alibaba.fastjson.JSON;
import com.pyshipping.common.codes.Codes;
import com.pyshipping.common.msg.Msg;
import com.pyshipping.common.pwd.RandomCode;
import com.pyshipping.model.Uploads;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {
    public static final String basePath = "src/main/resources/static/";
    /**
     * 上传文件 限制格式:图片 大小2M
     *
     * @param servletRequest 请求头 data :携带 图片类 : logos,goods... ,oldPath:旧文件web路径
     *
     * @param file
     * @return
     */
    @PostMapping
    public Msg<String> upload(ServletRequest servletRequest, MultipartFile file) {
        // 上传文件标识: logo, goods....
        String type = "images";
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 判断是否有旧文件 需要删除
        String dataStr = request.getHeader("data");
        if (!Objects.equals(dataStr, "")) {
            Uploads uploadData = JSON.parseObject(dataStr, Uploads.class);
            if (uploadData != null) {
                type = uploadData.getType();
                String oldPath = uploadData.getOldPath();
                if (!Objects.equals(oldPath, "")) {
                    // 删除旧文件
                    // "/1111.png"
                   // String oldFile = oldPath.substring(oldPath.lastIndexOf("/"));
                    // "src/main/resources/static/logos/111.png"
                    String oldFilePath = basePath + oldPath;
                    File old = new File(oldFilePath);
                    if (old.isFile() && old.exists()) {
                       if (old.delete()) {
                           log.info("旧文件:{},已删除",old);
                       }
                    }
                }
            }
        }
        // 上传新文件
        if (file.isEmpty()) {
            return Msg.error(Codes.InvalidArgument, "图片数据不存在", "file empty or null");
        }
        // 接收到的file为系统缓存文件 转存为固定文件
        String fileType = file.getContentType();
        String oldFileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        //判断格式
        if (!(Objects.equals(fileType, "image/png") || Objects.equals(fileType, "image/jpeg") || Objects.equals(fileType, "image/jpg"))) {
            return Msg.error(Codes.InvalidArgument, "只能是图片格式", "file type error");
        }
        // 判断大小
        if (fileSize > 1000 * 1000 * 2) {
            return Msg.error(Codes.InvalidArgument, "图片不能大于2M", "file size error");
        }
        // 重新命名
        assert oldFileName != null;
        String extension = oldFileName.substring(oldFileName.lastIndexOf("."));
        String randomCode = RandomCode.randomString(4);
        String newName = Long.toString(System.currentTimeMillis()) + randomCode + extension;

        // 判断文件夹是否存在
        // "src/main/resources/static/logos"
        String logosDir = basePath + type;
        File saveDir = new File(logosDir);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        String canonicalPath = "";
        try {
            canonicalPath = saveDir.getCanonicalPath();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String filePath =  canonicalPath + "/" + newName;
        // 保存文件
        try {
            log.info(filePath);
            file.transferTo(new File(filePath));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String fileUrl = type + "/" + newName;
        log.info(fileUrl);
        return Msg.success(Codes.OK, "ok", fileUrl);
    }

    @DeleteMapping
    public Msg<String> remove(@RequestParam String path) {
        String decodedUrl = "";
        try{
            decodedUrl = URLDecoder.decode(path, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        log.info(decodedUrl);
        if (!Objects.equals(decodedUrl, "")) {
            // 删除文件
            String oldFilePath = basePath + decodedUrl;
            File old = new File(oldFilePath);
            if (old.isFile() && old.exists()) {
                if (old.delete()) {
                    return Msg.success(Codes.OK,"ok",oldFilePath);
                }
            }
        }
        return Msg.error(Codes.Internal,"error","err");
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        // 输入流读取文件
        FileInputStream fis = new FileInputStream(new File(basePath + name));
            // 设置返回类型
            response.setContentType("image/jpeg");
            // 输出流写回浏览器
            ServletOutputStream ops = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fis.read(bytes)) != -1) {
                ops.write(bytes,0,len);
                ops.flush();
            }
            fis.close();
            ops.close();

    }
}
