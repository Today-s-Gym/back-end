package com.gym.report;

import com.gym.config.exception.BaseException;
import com.gym.post.Post;
import com.gym.post.comment.Comment;
import com.gym.user.User;
import com.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.gym.config.exception.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Transactional
    public Integer saveReportUser(User user, Integer reportedUserId) throws BaseException {
        Optional<User> reportedUser = userRepository.findById(reportedUserId);
        if (reportedUser.isEmpty()) {
            throw new BaseException(INVALID_USER);
        }
        Report report = Report.createReportUser(user, reportedUser.get());
        reportRepository.save(report);
        return report.getReportId();
    }

    @Transactional
    public Integer saveReportPost(User reporter, Post reportedPost) throws BaseException {
        validateReportedPost(reporter, reportedPost);
        Report report = Report.createReportPost(reporter, reportedPost);
        reportRepository.save(report);
        return report.getReportId();
    }

    private void validateReportedPost(User reporter, Post reportedPost) throws BaseException {
        if (reporter.equals(reportedPost.getUser())) {
            throw new BaseException(REPORT_POST_SELF);
        }
    }

    @Transactional
    public Integer saveReportComment(User reporter, Comment reportedComment) throws BaseException {
        validateReportedComment(reporter, reportedComment);
        Report report = Report.createReportComment(reporter, reportedComment);
        reportRepository.save(report);
        return report.getReportId();
    }

    private void validateReportedComment(User reporter, Comment reportedComment) throws BaseException {
        if (reporter.equals(reportedComment.getUser())) {
            throw new BaseException(REPORT_COMMENT_SELF);
        }
    }
}