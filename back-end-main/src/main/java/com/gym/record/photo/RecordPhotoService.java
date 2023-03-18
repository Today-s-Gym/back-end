package com.gym.record.photo;

import com.gym.record.Record;
import com.gym.utils.S3Service;
import com.gym.utils.dto.getS3Res;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordPhotoService {

    private final RecordPhotoRepository recordPhotoRepository;
    private final S3Service s3Service;

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
    public void saveAllRecordPhotoByRecord(List<getS3Res> getS3ResList, Record record) {
        List<RecordPhoto> recordPhotos = new ArrayList<>();
        for(getS3Res getS3Res : getS3ResList){
            RecordPhoto recordPhoto = RecordPhoto.builder().imgUrl(getS3Res.getImgUrl()).fileName(getS3Res.getFileName()).build();
            recordPhotos.add(recordPhoto);
            record.addPhotoList(recordPhoto);
        }
        saveRecordPhoto(recordPhotos);
    }

    /**
     * 기록과 연관된 모든 recordPhoto 삭제
     */
    @Transactional
    public void deleteAllRecordPhotos(List<RecordPhoto> recordPhotos){
        for (RecordPhoto recordPhoto : recordPhotos) {
            s3Service.deleteFile(recordPhoto.getFileName());
        }
    }

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
