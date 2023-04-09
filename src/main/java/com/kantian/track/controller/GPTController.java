package com.kantian.track.controller;

import static com.kantian.track.support.TrackConstants.all;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.chat.Message.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @date 2023-03-01
 */
@Api(tags = "获取情感")
@RestController
@Slf4j
@RequestMapping(value = "/api/emotion")
public class GPTController {

  private final OpenAiStreamClient openAiStreamClient;

  public GPTController(OpenAiStreamClient openAiStreamClient) {
    this.openAiStreamClient = openAiStreamClient;
  }

  @Value("${system.gpt.apiKey}")
  private String apiKey;

  @Value("${system.gpt.apiHost}")
  private String apiHost;

  @ApiOperation("测试")
  @GetMapping("/text-emotion")
  @CrossOrigin
  public ResponseEntity<HashMap<String, Object>> test(@RequestParam("message") String msg) {
    if (log.isDebugEnabled()) {
      log.debug("参数 --> param:{}", msg);
    }
    OpenAiClient openAiClient =
        OpenAiClient.builder()
            .apiKey(Collections.singletonList(apiKey))
            // 自己做了代理就传代理地址，没有可不不传
            .apiHost(apiHost)
            .build();

    // Message systemMessage = Message.builder().role(Role.SYSTEM).content(systemContent).build();
    // Message contrastMessage = Message.builder().role(Role.SYSTEM).content(contrast).build();
    // Message declareMessage = Message.builder().role(Role.SYSTEM).content(declare).build();
    //
    // List<Message> messageList = new ArrayList<>();
    // messageList.add(systemMessage);
    // messageList.add(contrastMessage);
    // messageList.add(declareMessage);

    Message systemMessage = Message.builder().role(Role.SYSTEM).content(all).build();

    List<Message> messageList = new ArrayList<>();
    messageList.add(systemMessage);

    msg = "<" + msg + ">";
    Message message = Message.builder().role(Message.Role.USER).content(msg).build();

    messageList.add(message);

    ChatCompletion chatCompletion =
        ChatCompletion.builder()
            .messages(messageList)
            .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
            .temperature(0.0)
            .stream(false)
            .build();
    long l = System.currentTimeMillis();
    ChatCompletionResponse chatCompletionResponse;
    try {
      chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("API接口调用失败");
    }
    long l1 = System.currentTimeMillis();
    System.out.println("耗时为：" + (l1 - l) / 1000);
    String content = null;
    for (ChatChoice choice : chatCompletionResponse.getChoices()) {
      Message message1 = choice.getMessage();
      content = message1.getContent();
      System.out.println(content);
    }
    HashMap<String, Object> objectObjectHashMap = new HashMap<>();
    try {
      JSONObject entries = JSONUtil.parseObj(content);
      Set<Entry<String, Object>> entries1 = entries.entrySet();

      for (Entry<String, Object> stringObjectEntry : entries1) {
        String key = stringObjectEntry.getKey();

        Object value = stringObjectEntry.getValue();
        JSONArray objects = JSONUtil.parseArray(value);
        ArrayList<String> strings = new ArrayList<>();
        for (Object object : objects) {
          String s = object.toString();
          strings.add(s);
        }
        String musicName = StringUtils.join(strings, ",");
        System.out.println(musicName);
        String s = objects.toString();

        objectObjectHashMap.put("emotion", key);
        objectObjectHashMap.put("musicname", s);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      objectObjectHashMap.put("emotion", "中性");
      objectObjectHashMap.put("musicname", "中性01.mp3");
    }

    return ResponseEntity.ok(objectObjectHashMap);
  }
}
