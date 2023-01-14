package com.gym.category;

import com.gym.config.exception.BaseResponse;
import com.gym.user.dto.AccountPrivacyReq;
import com.gym.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 게시판 카테고리 목록 조회
    * [GET] /categories
     */
    @GetMapping("/categories")
    public BaseResponse<List<String>> getCategories() {
        return new BaseResponse<>(categoryService.getCategories());
    }

}
