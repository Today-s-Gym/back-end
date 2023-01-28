package com.gym.record;


import com.gym.record.dto.RecordGetRecentRes;
import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;

import com.gym.record.dto.RecordGetReq;
import com.gym.record.dto.RecordGetRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    /**
     * Record, 사진여러개, 태그여러개 저장
     */
    @PostMapping("/record")
    public BaseResponse<Integer> createRecord(@RequestPart(value = "image", required = false) List<MultipartFile> multipartFiles,
                                              @RequestPart(value = "recordGetReq") RecordGetReq recordGetReq){
       try{
           return new BaseResponse<>(recordService.saveRecord(multipartFiles, recordGetReq));
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

    /**
     * 기록 count
     * Test 할려고 만듬
     */
    @GetMapping("/record/count")
    public BaseResponse<Integer> findRecordCount() throws BaseException {
        return new BaseResponse<>(recordService.findCount());
    }

    /**
     * 기록 count Month
     * Test 할려고 만듬
     */
    @GetMapping("/record/count/month")
    public BaseResponse<Integer> findRecordCountMonth(@Param("month") String month) throws BaseException {
        return new BaseResponse<>(recordService.findCountMonth(month));
    }
}
