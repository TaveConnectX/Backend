package com.tave.connectX.service;

import com.tave.connectX.dao.RankingDao;
import com.tave.connectX.dto.ranking.GetRankingDto;
import com.tave.connectX.dto.ranking.ReturnRankingDto;
import com.tave.connectX.dto.ranking.UpdateRankingDto;
import com.tave.connectX.entity.User;
import com.tave.connectX.provider.JwtProvider;
import com.tave.connectX.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RankingDao rankingDao;

    public List<GetRankingDto> getRankings() {
        return rankingDao.getRankings();
    }

    public GetRankingDto getRanking(HttpServletRequest request) {
        User user = loadUser(request);
        return rankingDao.getRanking(user);
    }


    public ReturnRankingDto updateRanking(UpdateRankingDto updateRankingDto) {
        return rankingDao.updateRanking(updateRankingDto);
    }

    private User loadUser(HttpServletRequest request) {
        // 현재 유저 조회
        String token = jwtProvider.resolveToken(request);
        String account = jwtProvider.getAccount(token.split(" ")[1].trim());
        User user = userRepository.findByOauthId(account).get();
        return user;
    }
}
