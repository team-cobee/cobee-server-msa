package org.example.alarmservice.service;

import lombok.RequiredArgsConstructor;
import org.example.alarmservice.repository.AlarmNoticeRepository;
import org.example.alarmservice.repository.AlarmRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmNoticeService {
    private final AlarmRepository alarmRepository;
    private final AlarmNoticeRepository alarmNoticeRepository;
}
