package com.gym.record;


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
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequiredArgsConstructor
public class RecordController {
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final RecordService recordService;
    private final RecordPhotoService recordPhotoService;

    private final TagService tagService;

    /**
     * Record, 사진여러개, 태그여러개 저장
     */
    @PostMapping("/record")
    public BaseResponse<Integer> createRecord(@RequestBody RecordGetReq recordRequestDto){
        Integer recordId = recordService.saveRecord(recordRequestDto.getContent());
        Record record = recordRepository.findById(recordId).get();
        List<RecordPhoto> recordPhotos = recordRequestDto.getRecordPhotos();
        for (RecordPhoto recordPhoto : recordPhotos) {
            record.addPhotoList(recordPhoto);
        }
        List<Tag> tags = recordRequestDto.getTags();
        for (Tag tag : tags) {
            record.addTagList(tag);
            tag.createUser(record.getUser());
        }
        recordPhotoService.saveRecordPhoto(recordPhotos);
        tagService.saveTag(tags);
        return new BaseResponse<>(record.getUser().getUserId());
    }

    /**
     * Record, 사진, 태그 조회 (날짜 기준)
     */
    @GetMapping("/record")
    public RecordGetRes getRecord(@Param("date") String date){
        User user = userRepository.findById(JwtService.getUserId()).get();
        Record record = recordService.findRecordByDay(date);
        RecordGetRes recordGetRes = new RecordGetRes(record,user);
        return recordGetRes;
    }
}
