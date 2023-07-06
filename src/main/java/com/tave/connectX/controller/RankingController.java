package com.tave.connectX.controller;

import com.tave.connectX.dto.ranking.GetRankingDto;
import com.tave.connectX.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ranking")
public class RankingController {
    private final RankingService rankingService;

    @Operation(summary = "전체 랭킹 정보 얻기", description = "전체 유저의 랭킹 정보를 얻는 메서드 입니다.")
    @GetMapping("/users")
    public ResponseEntity getRankings() {
        List<GetRankingDto> getRankingDto = rankingService.getRankings();
        return ResponseEntity.ok(getRankingDto);
    }

    @Operation(summary = "랭킹 정보 얻기", description = "개인 랭킹 정보를 얻는 메서드 입니다.")
    @GetMapping()
    public ResponseEntity getRanking(HttpServletRequest request) {
        GetRankingDto getRankingDto = rankingService.getRanking(request);
        return ResponseEntity.ok(getRankingDto);
    }
}
