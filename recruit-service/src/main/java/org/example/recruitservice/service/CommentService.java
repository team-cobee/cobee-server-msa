package org.example.recruitservice.service;

import lombok.RequiredArgsConstructor;
import org.example.recruitservice.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
}
