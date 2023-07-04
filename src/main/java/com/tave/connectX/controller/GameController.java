package com.tave.connectX.controller;

import com.tave.connectX.dto.GameDto;
import com.tave.connectX.dto.GameEndDto;
import com.tave.connectX.dto.ReviewResponseDto;
import com.tave.connectX.dto.ranking.ReturnRankingDto;
import com.tave.connectX.dto.ranking.UpdateRankingDto;
import com.tave.connectX.entity.User;
import com.tave.connectX.entity.game.Difficulty;
import com.tave.connectX.service.GameService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class GameController {

    private final GameService gameService;

    // 게임 시작
    @GetMapping("/games")
    public ResponseEntity createGame(HttpServletRequest request, @RequestParam Difficulty difficulty) {
        GameDto gameDto = gameService.startGame(request, difficulty);
        return ResponseEntity.ok(gameDto);
    }

    // 게임 진행
    @PostMapping("/games")
    public ResponseEntity playGame(@RequestBody GameDto gameDto) {
        GameDto modelResponse = gameService.processGame(gameDto);

        return ResponseEntity.ok(modelResponse);
    }

    // 게임 종료
    @PostMapping("/games/results")
    public ReturnRankingDto endGame(@RequestBody GameEndDto gameEndDto, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        ReturnRankingDto returnRankingDto = gameService.endGame(gameEndDto);
        return returnRankingDto;
    }


    @GetMapping("/games/review")
    public ResponseEntity getRecentReview(HttpServletRequest request) {
        List<ReviewResponseDto> userReview = gameService.findReview(request);

        if (userReview == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userReview);
    }

}
