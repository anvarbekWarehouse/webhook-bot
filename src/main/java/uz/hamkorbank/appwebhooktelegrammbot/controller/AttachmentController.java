package uz.hamkorbank.appwebhooktelegrammbot.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.StringJoiner;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

    String currentDir = System.getProperty("user.dir");
    StringJoiner joiner = new StringJoiner("");
    String path = joiner.add(currentDir).add("/upload").toString().trim();

    @GetMapping
    public void getFileByName(@RequestParam String fileName, HttpServletResponse response) throws IOException {
        File file = new File(path+"/"+fileName);
        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control", "max-age=8640000");
        response.setHeader("Content-disposition", "attachment; filename=\""+file.getName()+"\"");
        InputStream in = new FileInputStream(file);
        FileCopyUtils.copy(in, response.getOutputStream());
    }
}
