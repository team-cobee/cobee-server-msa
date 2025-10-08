package org.example.recruitservice.service;

import lombok.RequiredArgsConstructor;
import org.example.recruitservice.RecruitRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final RecruitRepository recruitRepository;

}
