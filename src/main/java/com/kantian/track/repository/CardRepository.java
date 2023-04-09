package com.kantian.track.repository;

import com.kantian.track.entity.Card;
import com.kantian.track.jpa.BaseJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author xming
 */
@Repository
public interface CardRepository extends BaseJpaRepository<Card> {

}
