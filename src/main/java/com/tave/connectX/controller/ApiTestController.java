package com.tave.connectX.controller;

import com.tave.connectX.dto.GameDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ApiTestController {
    @GetMapping
    public ResponseEntity modelResult()
    {
        return ResponseEntity.ok("Connected with ConnectX's Spring boot server");
    }

}
