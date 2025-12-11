package com.project.mymemory.controllers;

import com.project.mymemory.dto.response.ApiResponse;
import com.project.mymemory.entitys.Memory;
import com.project.mymemory.services.MemoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memories")
public class MemoryController {

    private final MemoryService memoryService;

    @GetMapping
    public ApiResponse<List<Memory>> getAll() {
        return new ApiResponse<>("Get memories successfully", memoryService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Memory> getById(@PathVariable Long id) {
        return new ApiResponse<>("Get memory successfully", memoryService.getById(id));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<Memory>> create(
            @PathVariable Long userId,
            @RequestBody Memory memory
    ) {
        Memory created = memoryService.create(userId, memory);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Create memory successfully", created));
    }

    @PutMapping("/{userId}/{memoryId}")
    public ApiResponse<Memory> update(
            @PathVariable Long userId,
            @PathVariable Long memoryId,
            @RequestBody Memory memory
    ) {
        return new ApiResponse<>("Update memory successfully", memoryService.update(userId, memoryId, memory));
    }

    @DeleteMapping("/{userId}/{memoryId}")
    public ApiResponse<String> delete(
            @PathVariable Long userId,
            @PathVariable Long memoryId
    ) {
        return new ApiResponse<>("Delete memory successfully", memoryService.delete(userId, memoryId));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Memory>> getAllByUser(@PathVariable Long userId) {
        return new ApiResponse<>("Get user memories successfully", memoryService.getAllByUser(userId));
    }
}
