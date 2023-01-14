package com.gym.record.photo;

import com.gym.record.Record;
import com.gym.user.User;
import com.gym.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordPhotoService {

    private final RecordPhotoRepository recordPhotoRepository;

    @Transactional
    public void saveRecordPhoto(List<RecordPhoto> recordPhotos){
        recordPhotoRepository.saveAll(recordPhotos);
    }

}
