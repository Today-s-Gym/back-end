package com.gym.tag;

import com.gym.config.exception.BaseException;
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

    public List<Tag> findByRecordId(int recordId){
        return tagRepository.findAllByRecord(recordId);
    }

    /**
     * 최근 사용한 태그 조회
     */
    public Page<String> findRecentTag() throws BaseException {
        User user = utilService.findByUserIdWithValidation(JwtService.getUserId());
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<String> pages = tagRepository.findByRecord(user.getUserId(), pageRequest);
        return pages;
    }

}
