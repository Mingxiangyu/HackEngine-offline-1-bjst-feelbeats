package com.kantian.track.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author xming
 */
@Data
public class CardDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  /**
   * 输入的文本
   */
  private String text;

  /**
   * 音频名称
   */
  private String musicName;

  /**
   * 情感标签
   */
  private String emotionTag;

  /**
   * 图片路劲
   */
  private String imgPath;

  /**
   * 创建时间
   */
  private Date createDate;

  /**
   * 修改时间
   */
  private Date updateDate;
}
