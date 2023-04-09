package com.kantian.track.service;

import com.kantian.track.dto.CardDto;
import com.kantian.track.entity.Card;
import com.kantian.track.repository.CardRepository;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @author xming */
@Service
@Transactional(rollbackFor = Exception.class)
public class CardServiceImpl implements CardService {
  private final CardRepository cardRepository;

  @Autowired
  public CardServiceImpl(CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  @Override
  public List<Card> listCard() {
    return cardRepository.findAll();
  }

  @Override
  public Card saveCard(CardDto dto) {
    Card card = new Card();
    BeanUtils.copyProperties(dto,card);
    return cardRepository.save(card);
  }
}
