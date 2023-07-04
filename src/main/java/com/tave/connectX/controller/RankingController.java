package com.tave.connectX.controller;

import com.tave.connectX.dto.ranking.ReturnRankingDto;
import com.tave.connectX.dto.ranking.UpdateRankingDto;
import com.tave.connectX.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ranking")
public class RankingController {
    private final RankingService rankingService;

    @Operation(summary = "랭킹 정보 얻기", description = "전체 유저의 랭킹 정보를 얻는 메서드 입니다.")
    @GetMapping()
    public ResponseEntity getRanking() {
        Map<Integer, String> getRankingDto = rankingService.getRanking();
        return ResponseEntity.ok(getRankingDto);
    }

    @Operation(summary = "게임 결과 랭킹 반영", description = "게임 결과를 유저의 랭킹에 업데이트하여 반영하는 메서드 입니다.")
    @RequestMapping("/update")
    public ResponseEntity updateRanking(@ModelAttribute UpdateRankingDto updateRankingDto) {
        ReturnRankingDto resultDto = rankingService.updateRanking(updateRankingDto);
        return ResponseEntity.ok(resultDto);
    }
}
