package com.gym.tag;

import com.gym.config.exception.BaseException;
import com.gym.record.dto.RecordGetReq;
import com.gym.tag.dto.TagGetRes;
import com.gym.user.User;
import com.gym.utils.JwtService;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gym.record.Record;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final UtilService utilService;

    /**
     * 태크 여러개 저장
     */
    @Transactional
    public void saveTag(List<Tag> tags){
        tagRepository.saveAll(tags);
    }


    /**
     * 최근 사용한 태그 조회(Paging 처리 10개 탐색)
     */
    public Page<String> findRecentTag() throws BaseException {
        User user = utilService.findByUserIdWithValidation(JwtService.getUserId());
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<String> pages = tagRepository.findByRecord(user.getUserId(), pageRequest);
        return pages;
    }

    /**
     * Record 연관된 태그 모두 저장
     */
    @Transactional
    public void saveAllTagByRecord(RecordGetReq recordGetReq, Record record) {
        List<Tag> tags = recordGetReq.getTags();
        for (Tag tag : tags) {
            record.addTagList(tag);
            tag.createUser(record.getUser());
        }
        saveTag(tags);
    }

    /**
     * 태그 record 연관 전체 삭제
     */
    @Transactional
    public void deleteAllTagByRecord(Record record){
        tagRepository.deleteAllByRecord(record);
    }

}
