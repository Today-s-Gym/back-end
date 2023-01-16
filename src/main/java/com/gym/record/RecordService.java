package com.gym.record;

import com.gym.config.exception.BaseException;
import com.gym.record.dto.RecordGetReq;
import com.gym.record.dto.RecordGetRes;
import com.gym.record.photo.RecordPhoto;
import com.gym.record.photo.RecordPhotoRepository;
import com.gym.record.photo.RecordPhotoService;
import com.gym.tag.Tag;
import com.gym.tag.TagService;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.utils.JwtService;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.gym.config.exception.BaseResponseStatus.EMPTY_RECORD;
import static com.gym.config.exception.BaseResponseStatus.RECORD_DATE_EXISTS;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final UtilService utilService;
    private final RecordPhotoService recordPhotoService;
    private final TagService tagService;



    /**
     * Record, photo, tag 저장
     */
    @Transactional
    public Integer saveRecord(RecordGetReq recordGetReq) throws BaseException {
        validateDuplicateRecord();
        //엔티티 조회
        User user = utilService.findByUserIdWithValidation(JwtService.getUserId());
        Record record = Record.createRecord(recordGetReq.getContent(), user);
        record = utilService.findByRecordIdWithValidation(record.getRecordId());
        recordRepository.save(record);
        //Record 사진 추가
        recordPhotoService.saveAllRecordPhotoByRecord(recordGetReq, record);
        //Tag 추가
        tagService.saveAllTagByRecord(recordGetReq, record);
        return record.getRecordId();
    }

    /**
     * 하루에 하나만 기록 추가
     */
    private void validateDuplicateRecord() throws BaseException {
        Integer count = recordRepository.findByRecordDate(String.valueOf(LocalDate.now()));
        if(count > 0){
            throw new BaseException(RECORD_DATE_EXISTS);
        }
    }

    /**
     * y-m-d에 따라 기록 조회
     */
    public RecordGetRes findRecordByDay(String date) throws BaseException {
        try {
            User user = utilService.findByUserIdWithValidation(JwtService.getUserId());
            Record record = recordRepository.findAllByDay(user.getUserId(), date);
            record = utilService.findByRecordIdWithValidation(record.getRecordId());
            RecordGetRes recordGetRes = new RecordGetRes(record, user);
            return recordGetRes;
        } catch (NullPointerException e){
            throw new BaseException(EMPTY_RECORD);
        }
    }

    /**
     * y-m에 따라 기록 조회
     */
    public List<RecordGetRes> findRecordByMonth(String month) throws BaseException {
        try {
            User user = utilService.findByUserIdWithValidation(JwtService.getUserId());
            List<Record> records = recordRepository.findAllByMonth(user.getUserId(), month);
            List<RecordGetRes> recordGetRes = records.stream()
                    .map(record-> new RecordGetRes(record, user))
                    .collect(Collectors.toList());
            return recordGetRes;
        }catch(NullPointerException e){
            throw new BaseException(EMPTY_RECORD);
        }
    }

    /**
     * 기록 업데이트
     */
    @Transactional
    public Integer updateRecord(String date, RecordGetReq recordGetReq) throws BaseException {
        //User, Record 조회 및 update
        User user = utilService.findByUserIdWithValidation(JwtService.getUserId());
        Record record = recordRepository.findAllByDay(user.getUserId(), date);
        record = utilService.findByRecordIdWithValidation(record.getRecordId());
        record.updateRecord(recordGetReq.getContent());
        //RecordPhoto update
        recordPhotoService.deleteAllRecordPhotoByRecord(record);
        recordPhotoService.saveAllRecordPhotoByRecord(recordGetReq, record);
        //Tag update
        tagService.deleteAllTagByRecord(record);
        tagService.saveAllTagByRecord(recordGetReq,record);
        return record.getRecordId();
    }

}
