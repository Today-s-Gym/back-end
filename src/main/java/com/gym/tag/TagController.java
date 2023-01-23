package com.gym.tag;


import com.gym.tag.config.exception.BaseException;
import com.gym.tag.config.exception.BaseResponse;
import com.gym.tag.dto.TagGetRecentRes;
import com.gym.tag.dto.TagGetRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * 최근 사용한 태그 조회
     */
    @GetMapping("tag/recent")
    public BaseResponse<Page<TagGetRecentRes>> findRecentTag(@Param("page") int page) throws BaseException {
        return new BaseResponse<>(tagService.findRecentTag(page));
    }
}
