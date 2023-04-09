package com.kantian.track.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 主要港口
 *
 * @author xming
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "card")
public class Card extends AbstractEntity {
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

}
