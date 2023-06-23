package com.tave.connectX.controller;

import com.tave.connectX.api.DeepLearningClient;
import com.tave.connectX.dto.GameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ApiTestController {

    private final DeepLearningClient deepLearningClient;

    // 현재까지의 맵을 넘겨주고 모델이 두는 결과로 맵을 생성해서 반환
    @PostMapping
    public ResponseEntity modelResult(@RequestBody GameDto gameDto)
    {
        GameDto result = deepLearningClient.processResult(gameDto);

        return ResponseEntity.ok(result);
    }

}
