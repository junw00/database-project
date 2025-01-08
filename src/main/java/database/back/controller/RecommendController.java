package database.back.controller;

import database.back.repository.RecommendProgramRepository;
import database.back.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendRepository recommendRepository;
    private final RecommendProgramRepository recommendProgramRepository;

}

