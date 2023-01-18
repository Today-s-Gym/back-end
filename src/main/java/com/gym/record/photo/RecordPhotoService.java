package com.gym.record.photo;

import com.gym.record.Record;
import com.gym.record.dto.RecordGetReq;
import com.gym.user.User;
import com.gym.utils.JwtService;
import com.gym.utils.UtilService;
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

    public List<RecordPhoto> findByRecordId(int recordId){
        return recordPhotoRepository.findAllByRecord(recordId);
    }

    /**
     *여러개의 recordPhoto 저장
     */
    @Transactional
    public void saveAllRecordPhotoByRecord(RecordGetReq recordGetReq, Record record) {
        List<RecordPhoto> recordPhotos = recordGetReq.getRecordPhotos();
        if (recordPhotos.isEmpty()) { //사진 없으면 기본사진 추가
            String str = UtilService.returnRecordBaseImage();
            RecordPhoto recordPhoto = new RecordPhoto(str, record);
            record.addPhotoList(recordPhoto);
        } else {
            for (RecordPhoto recordPhoto : recordPhotos) {
                record.addPhotoList(recordPhoto);
            }
        }
        saveRecordPhoto(recordPhotos);
    }

    /**
     * 기록과 연관된 모든 recordPhoto 삭제
     */
    @Transactional
    public void deleteAllRecordPhotoByRecord(List<Integer> ids){
        recordPhotoRepository.deleteAllByRecord(ids);
    }

    /**
     * record와 연관된 모든 id 조회
     */
    public List<Integer> findAllId(int recordId){
        return recordPhotoRepository.findAllId(recordId);
    }

}
