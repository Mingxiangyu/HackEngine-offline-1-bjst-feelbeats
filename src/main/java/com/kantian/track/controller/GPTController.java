package com.kantian.track.controller;

import static com.kantian.track.support.TrackConstants.systemContent;

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

    Message systemMessage = Message.builder().role(Role.SYSTEM).content(
        systemContent).build();
    // ChatCompletion systemChatCompletion =
    //     ChatCompletion.builder().messages(Arrays.asList(systemMessage)).model(ChatCompletion.Model.GPT_3_5_TURBO.getName()).build();
    // ChatCompletionResponse systemCompletionResponse = openAiClient.chatCompletion(systemChatCompletion);

    List<Message> messageList = new ArrayList<>();
    messageList.add(systemMessage);

    msg = "<" + msg + ">";
    Message message = Message.builder().role(Message.Role.USER).content(msg).build();

    messageList.add(message);

    ChatCompletion chatCompletion =
        ChatCompletion.builder().messages(messageList)
            .model(ChatCompletion.Model.GPT_3_5_TURBO.getName()).build();
    long l = System.currentTimeMillis();
    ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
    long l1 = System.currentTimeMillis();
    System.out.println("耗时为："+(l1 - l)/1000);
    String content = null;
    for (ChatChoice choice : chatCompletionResponse.getChoices()) {
      Message message1 = choice.getMessage();
      content = message1.getContent();
      System.out.println(content);
    }
    JSONObject entries = JSONUtil.parseObj(content);
    Set<Entry<String, Object>> entries1 = entries.entrySet();

    HashMap<String, Object> objectObjectHashMap = new HashMap<>();
    for (Entry<String, Object> stringObjectEntry : entries1) {
      String key = stringObjectEntry.getKey();
      Object value = stringObjectEntry.getValue();
      objectObjectHashMap.put("emotion", key);
      objectObjectHashMap.put("musicname", value);
    }

    return ResponseEntity.ok(objectObjectHashMap);
  }

}
