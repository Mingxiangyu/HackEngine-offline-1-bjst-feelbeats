package com.kantian.track.controller;

import com.kantian.track.dto.CardDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xming
 */
@Api(tags = {"响应音乐文件"})
@Slf4j
@RestController
public class MusicController {

  @Operation(summary = "音乐流")
  @GetMapping(value = "/music", name = "音乐流")
  public ResponseEntity<CardDto> getHarbourImage(
      @RequestParam String fileName, HttpServletResponse response) {
    if (log.isDebugEnabled()) {
      log.debug("参数 --> param:{}", fileName);
    }
    // String ext = fileName.substring(fileName.indexOf(".")).toLowerCase();
    // if ("JPG".equals(ext)) {
    //   response.setContentType("image/jpeg");
    // } else if ("PNG".equals(ext)) {
    //   response.setContentType("image/png");
    // }
    getMusic(fileName, response);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  public static void getMusic(String fileName, HttpServletResponse response) {
    String imageFileName =
        File.separator + "music" + File.separator + fileName;
    try {
      String decode = URLDecoder.decode(imageFileName, "UTF-8");
      ClassPathResource resource = new ClassPathResource(decode);
      InputStream inputStream = resource.getInputStream();
      BufferedOutputStream bufferedOutputStream =
          new BufferedOutputStream(response.getOutputStream());
      byte[] buff = new byte[1024];
      int n;
      while ((n = inputStream.read(buff)) != -1) {
        bufferedOutputStream.write(buff, 0, n);
      }
      bufferedOutputStream.flush();
    } catch (IOException e) {
      throw new RuntimeException("获取音乐失败：" + e.getMessage());
    }
  }
}
