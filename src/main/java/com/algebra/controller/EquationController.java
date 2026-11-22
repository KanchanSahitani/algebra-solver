package com.algebra.controller;

import com.algebra.dto.*;
import com.algebra.model.Equation;
import com.algebra.service.EquationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing endpoints to store, list, and evaluate algebraic equations.
 */
@RestController
@RequestMapping("/api/equations")
public class EquationController {

    private final EquationService equationService;

    public EquationController(EquationService equationService) {
        this.equationService = equationService;
    }

    /**
     * POST /api/equations/store — parse and store an algebraic equation.
     */
    @PostMapping("/store")
    public ResponseEntity<StoreEquationResponse> store(
            @Valid @RequestBody StoreEquationRequest request) {

        Equation stored = equationService.store(request.getEquation());
        StoreEquationResponse response = new StoreEquationResponse(
                "Equation stored successfully", stored.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/equations — retrieve all stored equations.
     */
    @GetMapping
    public ResponseEntity<EquationListResponse> getAll() {
        List<Equation> equations = equationService.getAll();
        List<EquationDto> dtos = equations.stream()
                .map(eq -> new EquationDto(eq.getId(), eq.getOriginalEquation()))
                .toList();
        return ResponseEntity.ok(new EquationListResponse(dtos));
    }

    /**
     * POST /api/equations/{equationId}/evaluate — evaluate a stored equation
     * with the provided variable values.
     */
    @PostMapping("/{equationId}/evaluate")
    public ResponseEntity<EvaluateResponse> evaluate(
            @PathVariable String equationId,
            @Valid @RequestBody EvaluateRequest request) {

        Equation equation = equationService.getById(equationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Equation not found with id: " + equationId));

        double result = equationService.evaluate(equationId, request.getVariables());

        EvaluateResponse response = new EvaluateResponse(
                equation.getId(),
                equation.getOriginalEquation(),
                request.getVariables(),
                result);
        return ResponseEntity.ok(response);
    }
}
