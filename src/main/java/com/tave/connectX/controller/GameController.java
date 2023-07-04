package com.tave.connectX.controller;

import com.tave.connectX.dto.GameDto;
import com.tave.connectX.dto.GameEndDto;
import com.tave.connectX.dto.ReviewResponseDto;
import com.tave.connectX.dto.ranking.ReturnRankingDto;
import com.tave.connectX.dto.ranking.UpdateRankingDto;
import com.tave.connectX.entity.User;
import com.tave.connectX.entity.game.Difficulty;
import com.tave.connectX.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
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
    @Operation(summary = "게임 시작 API" , description = "게임 시작 시 난이도를 담아 요청하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "선공을 정하여 시작 list를 담아 반환하며, 모델 선공시 첫 결과를 반영하여 응답합니다.")
    @GetMapping("/games")
    public ResponseEntity createGame(HttpServletRequest request, @Parameter(description = "난이도" , name = "difficulty") @RequestParam(name = "difficulty") Difficulty difficulty) {
        GameDto gameDto = gameService.startGame(request, difficulty);
        return ResponseEntity.ok(gameDto);
    }

    // 게임 진행
    @Operation(summary = "게임 턴 API", description = "승부가 나기 전까지 턴을 주고받는 API입니다.")
    @PostMapping("/games")
    public ResponseEntity playGame(@Parameter(description = "변경되는 값은 turn과 유저가 입력한 돌 반영한 list이고, 나머지는 그대로 다시 담아 보내야합니다.") @RequestBody GameDto gameDto) {
        GameDto modelResponse = gameService.processGame(gameDto);

        return ResponseEntity.ok(modelResponse);
    }

    // 게임 종료
    @Operation(summary = "게임 종료 API", description = "승리, 패배, 무승부가 결정되면 호출하는 API입니다.")
    @PostMapping("/games/results")
    public ReturnRankingDto endGame(@Parameter(required = true, description = "winner 필드값 ex) 유저 승리 : 1, 모델 승리 : 2, 비김 : 0")
                                        @RequestBody GameEndDto gameEndDto, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        ReturnRankingDto returnRankingDto = gameService.endGame(gameEndDto);
        return returnRankingDto;
    }


    @Operation(summary = "리뷰 API", description = "최근 게임 정보를 반환합니다. { 턴수, 상태 }")
    @ApiResponse(responseCode = "404", description = "NOT FOUND")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/games/review")
    public ResponseEntity getRecentReview(HttpServletRequest request) {
        List<ReviewResponseDto> userReview = gameService.findReview(request);

        if (userReview == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userReview);
    }

}
