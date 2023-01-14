package com.gym.record;


import com.gym.record.photo.RecordPhoto;
import com.gym.record.photo.RecordPhotoService;
import com.gym.tag.Tag;
import com.gym.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;
    private final RecordPhotoService recordPhotoService;
    private final RecordRepository recordRepository;
    private final TagService tagService;

    /**
     * Record, 사진여러개, 태그여러개 저장
     */
    @PostMapping("/record")
    public void createRecord(@RequestBody RecordRequestDto recordRequestDto){
        Integer recordId = recordService.saveRecord(recordRequestDto.getContent());
        Record record = recordRepository.findById(recordId).get();
        List<RecordPhoto> recordPhotos = recordRequestDto.getRecordPhotos();
        for (RecordPhoto recordPhoto : recordPhotos) {
            recordPhoto.createRecord(record);
        }
        List<Tag> tags = recordRequestDto.getTags();
        for (Tag tag : tags) {
            tag.createRecord(record);
            tag.createUser(record.getUser());
        }
        recordPhotoService.saveRecordPhoto(recordPhotos);
        tagService.saveTag(tags);
    }





}
