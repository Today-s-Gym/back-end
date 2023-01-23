package com.gym.tag;

import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.record.dto.RecordGetReq;
import com.gym.tag.dto.TagGetRecentRes;
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
import java.util.stream.Collectors;

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
    public Page<TagGetRecentRes> findRecentTag(int page) throws BaseException {
        User user = utilService.findByUserIdWithValidation(JwtService.getUserId());
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<String> pages = tagRepository.findByRecord(user.getUserId(), pageRequest);
        if(pages.getTotalElements() == 0){
            throw new BaseException(BaseResponseStatus.EMPTY_TAG);
        }
        Page<TagGetRecentRes> tagList = pages.map(s -> new TagGetRecentRes(s));
        return tagList;
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
    public void deleteAllTagByRecord(List<Integer> ids){
        tagRepository.deleteAllByRecord(ids);
    }

    /**
     * record와 연관된 모든 id 조회
     */
    public List<Integer> findAllId(int recordId){
        return tagRepository.findAllId(recordId);
    }

}
