package com.gym.record;

import com.gym.config.exception.BaseException;
import com.gym.record.dto.RecordGetReq;
import com.gym.record.dto.RecordGetRes;
import com.gym.record.photo.RecordPhoto;
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

import static com.gym.config.exception.BaseResponseStatus.RECORD_DATE_EXISTS;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final UtilService utilService;
    private final RecordPhotoService recordPhotoService;
    private final TagService tagService;


    /**
     * Record 저장
     */
    @Transactional
    public Integer saveRecord(RecordGetReq recordGetReq) throws BaseException {
        validateDuplicateRecord();
        //엔티티 조회
        User user = utilService.findByUserIdWithValidation(JwtService.getUserId());
        Record record = Record.createRecord(recordGetReq.getContent(), user);
        recordRepository.save(record);
        //Record 사진 추가
        List<RecordPhoto> recordPhotos = recordGetReq.getRecordPhotos();
        if(recordPhotos.isEmpty()){ //사진 없으면 기본사진 추가
            String str = UtilService.returnRecordBaseImage();
            RecordPhoto recordPhoto = new RecordPhoto(str, record);
            record.addPhotoList(recordPhoto);
        }
        else {
            for (RecordPhoto recordPhoto : recordPhotos) {
                record.addPhotoList(recordPhoto);
            }
        }
        //Tag 추가
        List<Tag> tags = recordGetReq.getTags();
        for (Tag tag : tags) {
            record.addTagList(tag);
            tag.createUser(record.getUser());
        }
        recordPhotoService.saveRecordPhoto(recordPhotos);
        tagService.saveTag(tags);
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
        User user = utilService.findByUserIdWithValidation(JwtService.getUserId());
        Record record = recordRepository.findAllByUserId(user.getUserId(), date);
        RecordGetRes recordGetRes = new RecordGetRes(record,user);
        return recordGetRes;
    }
}
