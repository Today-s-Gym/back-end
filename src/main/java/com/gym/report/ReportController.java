package com.gym.report;

import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.post.Post;
import com.gym.post.comment.Comment;
import com.gym.report.dto.ReportReq;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.login.jwt.JwtService;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final UserRepository userRepository;
    private final UtilService utilService;
    private final JwtService jwtService;

    @PostMapping("/report/user")
    public BaseResponse<Integer> reportUser(@RequestBody ReportReq reportReq) {
        try {
            User reporter = utilService.findByUserIdWithValidation(jwtService.getUserIdx());
            User reportedUser = utilService.findByUserIdWithValidation(reportReq.getReportedId());
            return new BaseResponse<>(reportService.saveReportUser(reporter, reportedUser));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PostMapping("/report/post")
    public BaseResponse<Integer> reportPost(@RequestBody ReportReq reportReq) {
        try {
            User reporter = utilService.findByUserIdWithValidation(jwtService.getUserIdx());
            Post reportedPost = utilService.findByPostIdWithValidation(reportReq.getReportedId());
            return new BaseResponse<>(reportService.saveReportPost(reporter, reportedPost));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    @PostMapping("/report/comment")
    public BaseResponse<Integer> reportComment(@RequestBody ReportReq reportReq) {
        try {
            User reporter = utilService.findByUserIdWithValidation(jwtService.getUserIdx());
            Comment reportedComment = utilService.findByCommentIdWithValidation(reportReq.getReportedId());
            return new BaseResponse<>(reportService.saveReportComment(reporter, reportedComment));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}