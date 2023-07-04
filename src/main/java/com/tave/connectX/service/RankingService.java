package com.tave.connectX.service;

import com.tave.connectX.dao.RankingDao;
import com.tave.connectX.entity.ranking.GetRankingDto;
import com.tave.connectX.entity.ranking.UpdateRankingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final RankingDao rankingDao;

    public Map<Integer, String> getRanking() {
        List<GetRankingDto> getRankingDto = rankingDao.getRanking();
        Map<Integer, String> rankings = new HashMap<>();

        int index = 1;
        for (GetRankingDto dto : getRankingDto) {
            rankings.put(index, dto.getName());
            index++;
        }

        return rankings;
    }


    public void updateRanking(UpdateRankingDto updateRankingDto) {
        rankingDao.updateRanking(updateRankingDto);
    }
}
