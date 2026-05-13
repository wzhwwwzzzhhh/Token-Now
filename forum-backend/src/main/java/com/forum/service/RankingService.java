package com.forum.service;

import com.forum.common.PageResult;
import com.forum.vo.LikeRankVO;
import com.forum.vo.PostRankVO;

import java.util.List;

public interface RankingService {
    PageResult<PostRankVO> getPostRanking(String type, Integer page, Integer size);

    List<LikeRankVO> getLikeRanking(String period, Integer limit);
}
