package com.gym.record;


import com.fasterxml.jackson.databind.ser.Serializers;
import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.record.dto.RecordGetRecentRes;
import com.gym.record.dto.RecordGetReq;
import com.gym.record.dto.RecordGetRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;


@RestController
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    /**
     * Record, 사진여러개, 태그여러개 저장
     */
    @PostMapping("/record")
    public BaseResponse<Integer> createRecord(@RequestBody RecordGetReq recordGetReq){
       try{
           return new BaseResponse<>(recordService.saveRecord(recordGetReq));
       } catch (BaseException exception){
           return new BaseResponse<>(exception.getStatus());
       }
    }

    /**
     * Record, 사진, 태그 조회 (날짜 기준)
     */
    @GetMapping("/record/day")
    public BaseResponse<RecordGetRes> getRecordDay(@Param("date") String date){
        try {
            return new BaseResponse<>(recordService.findRecordByDay(date));
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * Record, 사진, 태그 조회 (달 기준)
     */
    @GetMapping("/record/month")
    public BaseResponse<List<RecordGetRes>> getRecordMonth(@Param("month") String month){
        try {
            return new BaseResponse<>(recordService.findRecordByMonth(month));
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 기록 수정하기
     */
    @PostMapping("/record/update")
    public BaseResponse<Integer> updateRecord(@Param("date") String date, @RequestBody RecordGetReq recordGetReq) throws BaseException {
            return new BaseResponse<>(recordService.updateRecord(date, recordGetReq));
    }

    /**
     * 기록 삭제하기
     */
    @PostMapping("/record/delete")
    public BaseResponse<String> deleteRecord(@Param("date") String date){
        try {
            return new BaseResponse<>(recordService.deleteRecord(date));
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 최근 기록 조회하기
     */
    @GetMapping("/record/recent")
    public BaseResponse<Page<RecordGetRecentRes>> findRecentRecord(@Param("page") int page){
        try {
            return new BaseResponse<>(recordService.findAllRecent(page));
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
