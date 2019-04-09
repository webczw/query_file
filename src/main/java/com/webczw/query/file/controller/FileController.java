package com.webczw.query.file.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Wilber
 */
@RestController
@RequestMapping(value = "/rest")
public class FileController {
    private Logger logger = LoggerFactory.getLogger(FileController.class);
    /**
     * 被下载文件的名称
     */
    @Value("${query.file.fileName:W25.bin}")
    private String fileName;

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void findByDeptNO(HttpServletResponse response) throws Exception {
        //被下载的文件在服务器中的路径,
        String downloadFilePath = "./static/"+fileName;
        String url = ResourceUtils.getURL("classpath:").getPath();
        File file = new File(downloadFilePath);
        if (file.exists()) {
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
logger.info("url="+url);
                logger.info(downloadFilePath+"下载成功");
                return;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        logger.info(downloadFilePath+"下载失败");
    }
}
