package com.algebra.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EquationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("POST /api/equations/store — stores equation and returns 201")
    void storeEquation() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("equation", "3x + 2y - z"));

        mockMvc.perform(post("/api/equations/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Equation stored successfully"))
                .andExpect(jsonPath("$.equationId").isNotEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("POST /api/equations/store — stores second equation")
    void storeSecondEquation() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("equation", "x^2 + y^2 - 4"));

        mockMvc.perform(post("/api/equations/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.equationId").isNotEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("GET /api/equations — returns all stored equations")
    void getAllEquations() throws Exception {
        mockMvc.perform(get("/api/equations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equations").isArray())
                .andExpect(jsonPath("$.equations", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    @Order(4)
    @DisplayName("POST /api/equations/1/evaluate — evaluates equation with variables")
    void evaluateEquation() throws Exception {
        // First store an equation so id 1 exists in this context
        // (depends on Order 1 having run)
        String body = objectMapper.writeValueAsString(
                Map.of("variables", Map.of("x", 2, "y", 3, "z", 1)));

        mockMvc.perform(post("/api/equations/1/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equationId").value("1"))
                .andExpect(jsonPath("$.result").isNumber());
    }

    @Test
    @Order(5)
    @DisplayName("POST /api/equations/999/evaluate — returns 400 for non-existent id")
    void evaluateNotFound() throws Exception {
        String body = objectMapper.writeValueAsString(
                Map.of("variables", Map.of("x", 1)));

        mockMvc.perform(post("/api/equations/999/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("not found")));
    }

    @Test
    @Order(6)
    @DisplayName("POST /api/equations/store — blank equation returns 400")
    void storeBlankEquation() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("equation", ""));

        mockMvc.perform(post("/api/equations/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    @DisplayName("POST /api/equations/store — missing equation field returns 400")
    void storeMissingEquation() throws Exception {
        mockMvc.perform(post("/api/equations/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    @DisplayName("POST /api/equations/1/evaluate — missing variable returns 400")
    void evaluateMissingVariable() throws Exception {
        // Store one that uses x and y
        mockMvc.perform(post("/api/equations/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("equation", "x + y"))));

        // Evaluate with only x — missing y
        String body = objectMapper.writeValueAsString(
                Map.of("variables", Map.of("x", 1)));

        mockMvc.perform(post("/api/equations/1/evaluate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Missing value for variable")));
    }

    @Test
    @Order(9)
    @DisplayName("POST /api/equations/store — invalid syntax returns 400")
    void storeInvalidSyntax() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("equation", "(x + "));

        mockMvc.perform(post("/api/equations/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
