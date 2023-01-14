package com.gym.record;

import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.utils.JwtService;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;


    /**
     * Record 저장
     */
    @Transactional
    public Integer saveRecord(String content){
        //엔티티 조회
        User user = userRepository.findById(JwtService.getUserId()).get();
        Record record = Record.createRecord(content, user);
        recordRepository.save(record);
        return record.getRecordId();
    }

    public Record findRecordByDay(String date){
        User user = userRepository.findById(JwtService.getUserId()).get();
        Record record = recordRepository.findAllByUserId(user.getUserId(), date);
        return record;
    }
}
