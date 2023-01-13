package com.gym.record;


import com.gym.record.photo.RecordPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    /**
     * Record 저장
     */
    @PostMapping("/record")
    public Integer createRecord(@RequestBody RecordRequestDto recordRequestDto){
        System.out.println(recordRequestDto.getContent());
        return recordService.saveRecord(recordRequestDto.getContent());
    }





}
