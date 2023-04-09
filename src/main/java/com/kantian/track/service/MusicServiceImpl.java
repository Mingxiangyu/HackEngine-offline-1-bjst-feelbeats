package com.kantian.track.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @author xming */
@Service
@Transactional(rollbackFor = Exception.class)
public class MusicServiceImpl implements MusicService {
}
