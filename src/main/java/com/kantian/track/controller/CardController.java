package com.kantian.track.controller;

import com.kantian.track.dto.CardDto;
import com.kantian.track.entity.Card;
import com.kantian.track.service.CardService;
import com.kantian.track.utils.CardConvert;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xming
 */
@Api(tags = {"保存卡片"})
@RestController
@Slf4j
public class CardController {

  @Value("${system.imgpath.win:}")
  private String winImgPath;
  @Value("${system.imgpath.linux:}")
  private String linuxImgPath;

  /**
   * 港口图片文件夹名称
   */
  String primaryHarbour;

  private final CardService cardService;
  private final CardConvert cardConvert;

  @Autowired
  public CardController(CardService cardService, CardConvert cardConvert) {
    this.cardService = cardService;
    this.cardConvert = cardConvert;
  }

  @Operation(summary = "卡片列表")
  @GetMapping(value = "/cards", name = "卡片列表")
  public ResponseEntity<List<CardDto>> listCard() {
    List<Card> cards = cardService.listCard();
    List<CardDto> dtoList =
        cards.stream().map(cardConvert::convertCard).collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.OK).body(dtoList);
  }

  @Operation(summary = "保存卡片")
  @PostMapping(value = "/card", name = "保存卡片")
  public ResponseEntity<CardDto> saveCard(CardDto dto) {
    Card card = cardService.saveCard(dto);
    return ResponseEntity.status(HttpStatus.OK).body(cardConvert.convertCard(card));
  }

  @Operation(summary = "获取图片")
  @GetMapping(value = "/img", name = "获取图片")
  public ResponseEntity<CardDto> getImg(String imgPath, HttpServletResponse response) {
    if (log.isDebugEnabled()) {
      log.debug("参数 --> param:{}", imgPath);
    }
    if (imgPath.isEmpty()) {
      System.out.println("请选择图片");
    }
    String imgFolder = null;
    //Windows操作系统
    String os = System.getProperty("os.name");
    if (os != null && os.toLowerCase().startsWith("windows")) {
      imgFolder = winImgPath;
      System.out.printf("当前系统版本是:%s%n", os);
      //Linux操作系统
    } else if (os != null && os.toLowerCase().startsWith("linux")) {
      imgFolder = linuxImgPath;
      System.out.printf("当前系统版本是:%s%n", os);
    } else { //其它操作系统
      System.out.printf("当前系统版本是:%s%n", os);
    }

    imgPath = imgFolder + File.separator + imgPath;
    File imgFile = new File(imgPath);
    if (!imgFile.exists()) {
      throw new RuntimeException("文件不存在！");
    }

    try (InputStream inputStream = new FileInputStream(imgFile);
        BufferedOutputStream bufferedOutputStream =
            new BufferedOutputStream(response.getOutputStream());) {

      byte[] buff = new byte[1024];
      int n;
      while ((n = inputStream.read(buff)) != -1) {
        bufferedOutputStream.write(buff, 0, n);
      }
      bufferedOutputStream.flush();
    } catch (IOException e) {
      throw new RuntimeException("获取音乐失败：" + e.getMessage());
    }
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping(value = "/fileUpload")
  public String fileUpload(@RequestPart(value = "file") MultipartFile file) {
    if (file.isEmpty()) {
      System.out.println("请选择图片");
    }
    // 文件名
    String fileName = file.getOriginalFilename();
    // 后缀名
    String suffixName = fileName.substring(fileName.lastIndexOf("."));
    String os = System.getProperty("os.name");
    String imgPath = null;
    //Windows操作系统
    if (os != null && os.toLowerCase().startsWith("windows")) {
      imgPath = winImgPath;
      System.out.printf("当前系统版本是:%s%n", os);
      //Linux操作系统
    } else if (os != null && os.toLowerCase().startsWith("linux")) {
      imgPath = linuxImgPath;
      System.out.printf("当前系统版本是:%s%n", os);
    } else { //其它操作系统
      System.out.printf("当前系统版本是:%s%n", os);
    }

    File filePath = new File(imgPath);
    if (!filePath.exists()) {
      boolean mkdirs = filePath.mkdirs();
      System.out.println("图片路径不存在，创建：" + mkdirs + " ,路径为：" + filePath.getAbsolutePath());
    }
    // 新文件名
    fileName = UUID.randomUUID() + suffixName;
    File dest = new File(filePath + File.separator + fileName);
    if (!dest.getParentFile().exists()) {
      dest.getParentFile().mkdirs();
    }
    try {
      file.transferTo(dest);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // 返回图片名称
    return fileName;
  }
}
