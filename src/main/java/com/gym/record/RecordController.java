package com.gym.record;


import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.record.dto.RecordGetReq;
import com.gym.record.dto.RecordGetRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

}
