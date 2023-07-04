package com.tave.connectX.controller;

import com.tave.connectX.dto.ranking.ReturnRankingDto;
import com.tave.connectX.dto.ranking.UpdateRankingDto;
import com.tave.connectX.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ranking")
public class RankingController {
    private final RankingService rankingService;

    @GetMapping()
    public ResponseEntity getRanking() {
        Map<Integer, String> getRankingDto = rankingService.getRanking();
        return ResponseEntity.ok(getRankingDto);
    }

    @PostMapping()
    public ResponseEntity updateRanking(@RequestBody UpdateRankingDto updateRankingDto) {
        ReturnRankingDto resultDto = rankingService.updateRanking(updateRankingDto);
        return ResponseEntity.ok(resultDto);
    }
}
