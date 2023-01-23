package com.gym.tag;

import com.gym.tag.config.exception.BaseException;
import com.gym.tag.config.exception.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("tag/recent")
    public BaseResponse<Page<String>> findRecentTag() throws BaseException {
        return new BaseResponse<>(tagService.findRecentTag());
    }



}
