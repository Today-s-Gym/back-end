package com.gym.record;

import com.gym.tag.config.exception.BaseException;
import com.gym.record.dto.RecordGetReq;
import com.gym.record.dto.RecordGetRes;
import com.gym.record.photo.RecordPhotoService;
import com.gym.tag.TagService;
import com.gym.user.User;
import com.gym.utils.JwtService;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.gym.tag.config.exception.BaseResponseStatus.EMPTY_RECORD;
import static com.gym.tag.config.exception.BaseResponseStatus.RECORD_DATE_EXISTS;

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
    @Modifying
    public Integer updateRecord(String date, RecordGetReq recordGetReq) throws BaseException {
        //User, Record 조회 및 update
        User user = utilService.findByUserIdWithValidation(JwtService.getUserId());
        Record record = recordRepository.findAllByDay(user.getUserId(), date);
        record = utilService.findByRecordIdWithValidation(record.getRecordId());
        record.updateRecord(recordGetReq.getContent());
        //RecordPhoto update
        List<Integer> phIds = recordPhotoService.findAllId(record.getRecordId());
        recordPhotoService.deleteAllRecordPhotoByRecord(phIds);
        recordPhotoService.saveAllRecordPhotoByRecord(recordGetReq, record);
        //Tag update
        List<Integer> tIds = tagService.findAllId(record.getRecordId());
        tagService.deleteAllTagByRecord(tIds);
        tagService.saveAllTagByRecord(recordGetReq,record);
        return record.getRecordId();
    }

    /**
     * 기록 삭제하기 연관된 사진, 태그도 모두 삭제
     * (태그를 삭제하면 최근 사용한 태그를 조회 불가)
     */
    @Transactional
    @Modifying
    public String deleteRecord(Integer recordId){
        Record record = recordRepository.findById(recordId).get();
        //recordPhoto 삭제
        List<Integer> ids = recordPhotoService.findAllId(record.getRecordId());
        recordPhotoService.deleteAllRecordPhotoByRecord(ids);
        //태그 삭제
        List<Integer> tIds = tagService.findAllId(record.getRecordId());
        tagService.deleteAllTagByRecord(tIds);
        //Record 삭제
        recordRepository.deleteAllByRecordId(record.getRecordId());
        return "기록을 삭제했습니다.";
    }

}
