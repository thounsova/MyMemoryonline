package com.project.mymemory.controllers;

import com.project.mymemory.dto.response.ApiResponse;
import com.project.mymemory.repository.CategoryRepository;
import com.project.mymemory.services.impl.MemoryServiceImpl;
import com.project.mymemory.entitys.Memory;
import com.project.mymemory.entitys.Category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.project.mymemory.dto.response.ErrorsException.notFound;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memories")
@SuppressWarnings("unused")
public class MemoryController {

    private final MemoryServiceImpl memoryServiceImpl;
    private final CategoryRepository categoryRepository;

    // GET ALL
    @GetMapping
    public ApiResponse<List<Memory>> getAllMemory() {
        List<Memory> memories = memoryServiceImpl.getAll();

        if (memories.isEmpty()) {
            throw notFound("Memory not found.");
        }

        return new ApiResponse<>(
                "Get memory successfully",
                memories
        );
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<Memory>> create(
            @PathVariable Long userId,
            @RequestBody Memory memory
    ) {
        // 1. Get category name from JSON
        String categoryName = memory.getCategory();

        if (categoryName == null || categoryName.isBlank()) {
            throw new RuntimeException("Category name is required");
        }

        // 2. Find by name
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 3. Set the category ID before saving
        memory.setCategoryId(category.getId());

        // 4. Save memory
        Memory createdMemory = memoryServiceImpl.create(userId, memory);

        ApiResponse<Memory> response = new ApiResponse<>(
                "Create memory successfully",
                createdMemory
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
