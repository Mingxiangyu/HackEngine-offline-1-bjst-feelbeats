package com.kantian.track.service;

import com.kantian.track.dto.CardDto;
import com.kantian.track.entity.Card;
import java.util.List;

/** @author xming */
public interface CardService {

  /**
   * 获取卡片列表
   *
   * @return 卡片集合
   */
  List<Card> listCard();

  /**
   * 获取卡片详情
   *
   * @return 卡片详情
   */
  Card saveCard(CardDto dto);

}
