package com.kantian.track.utils;

import com.kantian.track.dto.CardDto;
import com.kantian.track.entity.Card;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author xming
 */
@Component
public class CardConvert {

  /**
   * 主要港口转换
   *
   * @param source entity
   * @return dto
   */
  public CardDto convertCard(Card source) {
    if (source == null) {
      return null;
    }
    CardDto target = new CardDto();
    BeanUtils.copyProperties(source, target);
    target.setId(String.valueOf(source.getId()));
    return target;
  }

}
