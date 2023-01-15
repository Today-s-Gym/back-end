package com.gym.report;

import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.post.Post;
import com.gym.report.dto.ReportReq;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.utils.JwtService;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.gym.config.exception.BaseResponseStatus.REPORT_USER_SELF;

@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final UserRepository userRepository;
    private final UtilService utilService;

    @PostMapping("/report/user")
    public BaseResponse<Integer> reportUser(@RequestBody ReportReq reportReq) throws BaseException {
        User user = userRepository.findById(JwtService.getUserId()).get();
        validateReportedUser(JwtService.getUserId(), reportReq.getReportedId());
        return new BaseResponse<>(reportService.saveReportUser(user, reportReq.getReportedId()));
    }

    @PostMapping("/report/post")
    public BaseResponse<Integer> reportPost(@RequestBody ReportReq reportReq) throws BaseException {
        User reporter = utilService.findByUserIdWithValidation(JwtService.getUserId());
        Post reportedPost = utilService.findByPostIdWithValidation(reportReq.getReportedId());
        return new BaseResponse<>(reportService.saveReportPost(reporter, reportedPost));
    }

    private void validateReportedUser(Integer userId, Integer reportedId) throws BaseException {
        if (userId==reportedId) {
            throw new BaseException(REPORT_USER_SELF);
        }
    }
}