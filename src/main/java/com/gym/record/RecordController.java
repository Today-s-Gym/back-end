package com.gym.record;


import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
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
    @GetMapping("/record/{date}")
    public BaseResponse<RecordGetRes> getRecord(@PathVariable String date) throws BaseException {
        RecordGetRes recordGetRes = recordService.findRecordByDay(date);
        return new BaseResponse<>(recordGetRes);
    }
}
