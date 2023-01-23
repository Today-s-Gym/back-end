package com.gym.report;

import com.gym.tag.config.exception.BaseException;
import com.gym.tag.config.exception.BaseResponse;
import com.gym.post.Post;
import com.gym.post.comment.Comment;
import com.gym.report.dto.ReportReq;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.utils.JwtService;
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

    @PostMapping("/report/user")
    public BaseResponse<Integer> reportUser(@RequestBody ReportReq reportReq) throws BaseException {
        User reporter = utilService.findByUserIdWithValidation(JwtService.getUserId());
        User reportedUser = utilService.findByUserIdWithValidation(reportReq.getReportedId());
        return new BaseResponse<>(reportService.saveReportUser(reporter, reportedUser));
    }

    @PostMapping("/report/post")
    public BaseResponse<Integer> reportPost(@RequestBody ReportReq reportReq) throws BaseException {
        User reporter = utilService.findByUserIdWithValidation(JwtService.getUserId());
        Post reportedPost = utilService.findByPostIdWithValidation(reportReq.getReportedId());
        return new BaseResponse<>(reportService.saveReportPost(reporter, reportedPost));
    }

    @PostMapping("/report/comment")
    public BaseResponse<Integer> reportComment(@RequestBody ReportReq reportReq) throws BaseException {
        User reporter = utilService.findByUserIdWithValidation(JwtService.getUserId());
        Comment reportedComment = utilService.findByCommentIdWithValidation(reportReq.getReportedId());
        return new BaseResponse<>(reportService.saveReportComment(reporter, reportedComment));
    }
}