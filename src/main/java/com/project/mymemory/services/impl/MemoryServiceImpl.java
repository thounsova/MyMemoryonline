package com.project.mymemory.services.impl;

import com.project.mymemory.entitys.Category;
import com.project.mymemory.entitys.Memory;
import com.project.mymemory.repository.CategoryRepository;
import com.project.mymemory.repository.MemoryRepository;
import com.project.mymemory.repository.UserRepository;
import com.project.mymemory.services.MemoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.project.mymemory.dto.response.ErrorsException.*;

@Service
@RequiredArgsConstructor
public class MemoryServiceImpl implements MemoryService {

    private final MemoryRepository memoryRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    // ===================== GET ALL ===================== //
    @Override
    public List<Memory> getAll() {
        try {
            return memoryRepository.findAll();
        } catch (Exception e) {
            throw internal("Failed to fetch memories: " + e.getMessage());
        }
    }

    // ===================== GET BY ID ===================== //
    @Override
    public Memory getById(Long memoryId) {
        return memoryRepository.findById(memoryId)
                .orElseThrow(() -> notFound("Memory with ID " + memoryId + " not found."));
    }

    // ===================== CREATE ===================== //
    @Override
    public Memory create(Long userId, Memory memory) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> notFound("User not found."));

        if (memory.getTitle() == null || memory.getTitle().isBlank()) {
            throw validation("Memory title cannot be empty.");
        }

        if (memory.getCategory() != null) {
            Category category = categoryRepository.findByName(memory.getCategory())
                    .orElseThrow(() -> notFound("Category not found."));
            memory.setCategoryId(category.getId());
        }

        memory.setUser(user);

        try {
            return memoryRepository.save(memory);
        } catch (Exception e) {
            throw internal("Failed to create memory: " + e.getMessage());
        }
    }

    // ===================== UPDATE ===================== //
    @Override
    public Memory update(Long userId, Long memoryId, Memory updated) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> notFound("Memory with ID " + memoryId + " not found."));

        if (!memory.getUser().getId().equals(userId)) {
            throw forbidden("You are not allowed to update this memory.");
        }

        if (updated.getTitle() == null || updated.getTitle().isBlank()) {
            throw validation("Memory title cannot be empty.");
        }

        memory.setTitle(updated.getTitle());
        memory.setContent(updated.getContent());

        if (updated.getCategory() != null) {
            Category category = categoryRepository.findByName(updated.getCategory())
                    .orElseThrow(() -> notFound("Category not found."));
            memory.setCategory(updated.getCategory());
            memory.setCategoryId(category.getId());
        }

        try {
            return memoryRepository.save(memory);
        } catch (Exception e) {
            throw internal("Failed to update memory: " + e.getMessage());
        }
    }

    // ===================== DELETE ===================== //
    @Override
    public String delete(Long userId, Long memoryId) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> notFound("Memory not found."));

        if (!memory.getUser().getId().equals(userId)) {
            throw forbidden("You are not allowed to delete this memory.");
        }

        try {
            memoryRepository.delete(memory);
            return "Memory deleted successfully.";
        } catch (Exception e) {
            throw internal("Failed to delete memory: " + e.getMessage());
        }
    }

    // ===================== GET ALL BY USER ===================== //
    @Override
    public List<Memory> getAllByUser(Long userId) {
        try {
            List<Memory> memories = memoryRepository.findByUserId(userId);

            // Fill category name from categoryId
            for (Memory m : memories) {
                if (m.getCategoryId() != null) {
                    categoryRepository.findById(m.getCategoryId())
                            .ifPresent(category -> m.setCategory(category.getName()));
                }
            }

            return memories;
        } catch (Exception e) {
            throw internal("Failed to fetch user memories: " + e.getMessage());
        }
    }
}
