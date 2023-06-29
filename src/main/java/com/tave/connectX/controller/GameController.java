package com.tave.connectX.controller;

import com.tave.connectX.dto.GameDto;
import com.tave.connectX.dto.ReviewResponseDto;
import com.tave.connectX.service.GameService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GameController {

    private final GameService gameService;

    // 게임 시작
    @GetMapping("/game")
    public ResponseEntity createGame(HttpServletRequest request) {
        GameDto gameDto = gameService.startGame(request);
        return ResponseEntity.ok(gameDto);
    }

    // 게임 진행
    @PostMapping("/game")
    public ResponseEntity playGame(@RequestBody GameDto gameDto) {
        GameDto modelResponse = gameService.processGame(gameDto);

        return ResponseEntity.ok(modelResponse);
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
