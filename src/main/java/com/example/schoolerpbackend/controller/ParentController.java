package com.example.schoolerpbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.schoolerpbackend.entity.Parent;
import com.example.schoolerpbackend.repository.ParentRepository;

@RestController
@RequestMapping("/parents")
public class ParentController {

    @Autowired
    private ParentRepository parentRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAllParents() {
        try {
            List<Parent> parents = parentRepository.findAll();
            return ResponseEntity.ok(parents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching parents: " + e.getMessage());
        }
    }
}