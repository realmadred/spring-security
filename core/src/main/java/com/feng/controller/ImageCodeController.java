package com.feng.controller;

import com.feng.security.validate.ImageCode;
import com.feng.security.validate.ImageCodeGenerate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.feng.security.common.Constant.SESSION_IMAGE_CODE;

@Controller
@RequestMapping("/image")
public class ImageCodeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageCodeController.class);

    @GetMapping("/code")
    public void getImageCode(HttpSession session, HttpServletResponse response) throws IOException {
        final ImageCode imageCode = ImageCodeGenerate.generate();
        session.setAttribute(SESSION_IMAGE_CODE,imageCode);
        // 设置输出为图片
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("image/jpeg");
        final ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(imageCode.getImage(),"jpg", outputStream);
        outputStream.flush();
    }

}
