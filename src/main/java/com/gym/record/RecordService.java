package com.gym.record;

import com.gym.record.dto.RecordGetRecentRes;
import com.gym.config.exception.BaseException;
import com.gym.record.dto.RecordGetReq;
import com.gym.record.dto.RecordGetRes;
import com.gym.record.photo.RecordPhoto;
import com.gym.record.photo.RecordPhotoService;
import com.gym.tag.TagService;
import com.gym.user.User;
import com.gym.user.UserService;
import com.gym.utils.JwtService;
import com.gym.utils.S3Service;
import com.gym.utils.UtilService;
import com.gym.utils.dto.getS3Res;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    private final JwtService jwtService;

    private final S3Service s3Service;
    private final UserService userService;



    /**
     * Record, photo, tag 저장
     */
    @Transactional
    public Integer saveRecord(List<MultipartFile> multipartFiles, RecordGetReq recordGetReq) throws BaseException {
        validateDuplicateRecord();
        //엔티티 조회
        User user = utilService.findByUserIdWithValidation(jwtService.getUserIdx());
        user.updateRecordCheck();
        Record record = Record.createRecord(recordGetReq.getContent(), user);
        recordRepository.save(record);
        //Record 사진 추가
        if(multipartFiles != null) {
            List<getS3Res> imgUrls = s3Service.uploadFile(multipartFiles);
            recordPhotoService.saveAllRecordPhotoByRecord(imgUrls, record);
        }
        //Tag 추가
        tagService.saveAllTagByRecord(recordGetReq, record);
        if(user.isRecordCheck()==false) {
            user.addRecordCount();
        }
        userService.checkAndMyAvatarLevelUp(jwtService.getUserIdx());
        return record.getRecordId();
    }

    /**
     * 하루에 하나만 기록 추가
     */
    private void validateDuplicateRecord() throws BaseException {
        User user = utilService.findByUserIdWithValidation(jwtService.getUserIdx());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Integer count = recordRepository.findByRecordDate(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter).toString() , user.getUserId());
        if(count > 0){
            throw new BaseException(RECORD_DATE_EXISTS);
        }
    }

    /**
     * y-m-d에 따라 기록 조회
     */
    public RecordGetRes findRecordByDay(String date) throws BaseException {
        try {
            User user = utilService.findByUserIdWithValidation(jwtService.getUserIdx());
            Record record = recordRepository.findAllByDay(user.getUserId(), date);
            record = utilService.findByRecordIdWithValidation(record.getRecordId());
            RecordGetRes recordGetRes = new RecordGetRes(record, user);
            return recordGetRes;
        } catch (NullPointerException e) {
            throw new BaseException(EMPTY_RECORD);
        }
    }

    /**
     * y-m에 따라 기록 조회
     */
    public List<RecordGetRes> findRecordByMonth(String month) throws BaseException {
        try {
            User user = utilService.findByUserIdWithValidation(jwtService.getUserIdx());
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
    public Integer updateRecord(String date, RecordGetReq recordGetReq, List<MultipartFile> multipartFiles) throws BaseException {
        //User, Record 조회 및 update
        User user = utilService.findByUserIdWithValidation(jwtService.getUserIdx());
        Record record = recordRepository.findAllByDay(user.getUserId(), date);
        record = utilService.findByRecordIdWithValidation(record.getRecordId());
        record.updateRecord(recordGetReq.getContent());
        //record 사진 삭제
        List<RecordPhoto> recordPhotos = recordPhotoService.findByRecordId(record.getRecordId());
        recordPhotoService.deleteAllRecordPhotos(recordPhotos);
        List<Integer> ids = recordPhotoService.findAllId(record.getRecordId());
        recordPhotoService.deleteAllRecordPhotoByRecord(ids);
        //Record 사진 추가
        if(multipartFiles != null) {
            List<getS3Res> imgUrls = s3Service.uploadFile(multipartFiles);
            recordPhotoService.saveAllRecordPhotoByRecord(imgUrls, record);
        }
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
    public String deleteRecord(String date) throws BaseException {
        try {
        User user = utilService.findByUserIdWithValidation(jwtService.getUserIdx());
        Record record = recordRepository.findAllByDay(user.getUserId(), date);
        //recordPhoto 삭제
        List<RecordPhoto> recordPhotos = recordPhotoService.findByRecordId(record.getRecordId());
        recordPhotoService.deleteAllRecordPhotos(recordPhotos);
        List<Integer> ids = recordPhotoService.findAllId(record.getRecordId());
        recordPhotoService.deleteAllRecordPhotoByRecord(ids);
        //태그 삭제
        List<Integer> tIds = tagService.findAllId(record.getRecordId());
        tagService.deleteAllTagByRecord(tIds);
        //Record 삭제
        recordRepository.deleteAllByRecordId(record.getRecordId());
        return "기록을 삭제했습니다.";
        }catch(NullPointerException e){
            throw new BaseException(EMPTY_RECORD);
        }
    }

    /**
     * 최근 기록 조회
     */
    public  Page<RecordGetRecentRes> findAllRecent(int page) throws BaseException {
        User user = utilService.findByUserIdWithValidation(jwtService.getUserIdx());
        PageRequest pageRequest = PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Record> records = recordRepository.findAllByUserId(user.getUserId(), pageRequest);
        if (records.getTotalElements() == 0) {
            throw new BaseException(EMPTY_RECORD);
        }
        Page<RecordGetRecentRes> results = records.map(r -> new RecordGetRecentRes(r.getContent(), r.getCreatedAt(), r.getPhotoList()));
        return results;
    }

    /**
     * 기록 카운트
     */
    public Integer findCount() throws BaseException {
        User user = utilService.findByUserIdWithValidation(jwtService.getUserIdx());
        return recordRepository.countByUserId(user.getUserId());
    }

    /**
     * 기록 카운트 달기준
     */
    public Integer findCountMonth(String month) throws BaseException {
        User user = utilService.findByUserIdWithValidation(jwtService.getUserIdx());
        return recordRepository.countByUserIdMonth(user.getUserId(), month);
    }

    /**
     * 기록 신고하기
     */
    @Transactional
    public String reportRecord(String date) throws BaseException {
        User user = utilService.findByUserIdWithValidation(jwtService.getUserIdx());
        Record record = recordRepository.findAllByDay(user.getUserId(), date);
        record.addReport();
        return "신고가 접수되었습니다";
    }



}
