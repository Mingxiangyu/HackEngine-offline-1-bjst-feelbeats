package com.kantian.track;

import com.unfbx.chatgpt.OpenAiStreamClient;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/** @author xming */
@SpringBootApplication
@EnableOpenApi
@EnableJpaAuditing
public class EmotionApplication {

  @Value("${system.gpt.apiKey}")
  private String apiKey;
  @Value("${system.gpt.apiHost}")
  private String apiHost;

  public static void main(String[] args) {
    SpringApplication.run(EmotionApplication.class, args);
  }

  /**
   * 屏蔽swaggerUI中显示的basicerror
   *
   * @return 空
   */
  @Bean
  public Docket demoApi() {
    return new Docket(DocumentationType.OAS_30)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.regex("(?!/error.*).*"))
        .paths(PathSelectors.regex("(?!/profile.*).*"))
        .build();
  }

  @Bean
  public OpenAiStreamClient openAiStreamClient() {
    return OpenAiStreamClient.builder().apiHost(apiHost).apiKey(Arrays.asList(apiKey)).build();
  }
}
