package com.tave.connectX.controller;

import com.tave.connectX.dto.GameDto;
import com.tave.connectX.service.GameService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    // 게임 시작
    @GetMapping
    public ResponseEntity createGame(HttpServletRequest request) {
        GameDto gameDto = gameService.startGame(request);
        return ResponseEntity.ok(gameDto);
    }

    // 게임 진행
    @PostMapping
    public ResponseEntity playGame(@RequestBody GameDto gameDto) {
        GameDto modelResponse = gameService.processGame(gameDto);
        return ResponseEntity.ok(modelResponse);
    }


}
