package com.gym.report;

import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.post.Post;
import com.gym.post.comment.Comment;
import com.gym.user.User;
import com.gym.user.UserRepository;
import com.gym.utils.UtilService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReportServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    ReportService reportService;
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UtilService utilService;

    @Test
    @DisplayName("유저 신고 report 확인 테스트")
    @Transactional
    void saveReportUserTest() throws BaseException {
        //given
        Integer reporterId = 1;
        Integer reportedUserId = 2;
        User reporter = utilService.findByUserIdWithValidation(reporterId);
        User reportedUser = utilService.findByUserIdWithValidation(reportedUserId);

        //when
        Integer reportId = reportService.saveReportUser(reporter, reportedUser);

        //then
        Report report = reportRepository.findById(reportId).get();
        Assertions.assertThat(report).extracting("reporterId", "reportedId", "type")
                        .contains(reporterId, reportedUserId, ReportType.USER);
    }

    @Test
    @DisplayName("유저 신고 당한 유저의 report 카운트 증가 테스트")
    @Transactional
    void saveReportUser_ReportCountTest() throws BaseException {
        //given
        Integer reporterId = 1;
        Integer reportedUserId = 2;
        User reporter = utilService.findByUserIdWithValidation(reporterId);
        User reportedUser = utilService.findByUserIdWithValidation(reportedUserId);
        int beforeReportCount = reportedUser.getReport();

        //when
        reportService.saveReportUser(reporter, reportedUser);

        //then
        int afterReporterCount = utilService.findByUserIdWithValidation(reportedUserId).getReport();
        Assertions.assertThat(afterReporterCount).isEqualTo(beforeReportCount+1);
    }

    @Test
    @DisplayName("게시글 신고 report 확인 테스트")
    @Transactional
    void saveReportPostTest() throws BaseException {
        // given
        Integer reporterId = 1;
        Integer reportedPostId = 2;
        User reporter = utilService.findByUserIdWithValidation(reporterId);
        Post reportedPost = utilService.findByPostIdWithValidation(reportedPostId);

        // when
        Integer reportId = reportService.saveReportPost(reporter, reportedPost);

        // then
        Report report = reportRepository.findById(reportId).get();
        Assertions.assertThat(report).extracting("reporterId", "reportedId", "type")
                .contains(reporterId, reportedPostId, ReportType.POST);
    }

    @Test
    @DisplayName("게시글 신고 당한 게시글의 report 카운트 증가 테스트")
    @Transactional
    void saveReportPost_ReportCountTest() throws BaseException {
        //given
        Integer reporterId = 1;
        Integer reportedPostId = 2;
        User reporter = utilService.findByUserIdWithValidation(reporterId);
        Post reportedPost = utilService.findByPostIdWithValidation(reportedPostId);
        int beforeReportCount = reportedPost.getReport();

        //when
        reportService.saveReportPost(reporter, reportedPost);

        //then
        int afterReporterCount = utilService.findByPostIdWithValidation(reportedPostId).getReport();
        Assertions.assertThat(afterReporterCount).isEqualTo(beforeReportCount+1);
    }

    @Test
    @DisplayName("자신의 글을 신고할 경우 예외처리한다.")
    @Transactional
    void saveReportPost_self_exception() throws BaseException {
        //given
        Integer reporterId = 1;
        Integer reportedPostId = 1;
        User reporter = utilService.findByUserIdWithValidation(reporterId);
        Post reportedPost = utilService.findByPostIdWithValidation(reportedPostId);

        // when, then
        Assertions.assertThatThrownBy(() -> reportService.saveReportPost(reporter, reportedPost))
                .isInstanceOf(BaseException.class)
                .extracting("status").isEqualTo(BaseResponseStatus.REPORT_POST_SELF);
    }

    @Test
    @DisplayName("댓글 신고 report 확인 테스트")
    @Transactional
    void saveReportCommentTest() throws BaseException {
        // given
        Integer reporterId = 1;
        Integer reportedCommentId = 1;
        User reporter = utilService.findByUserIdWithValidation(reporterId);
        Comment reportedComment = utilService.findByCommentIdWithValidation(reportedCommentId);

        // when
        Integer reportId = reportService.saveReportComment(reporter, reportedComment);

        // then
        Report report = reportRepository.findById(reportId).get();
        Assertions.assertThat(report).extracting("reporterId", "reportedId", "type")
                .contains(reporterId, reportedCommentId, ReportType.COMMENT);
    }

    @Test
    @DisplayName("댓글 신고 당한 댓글의 report 카운트 증가 테스트")
    @Transactional
    void saveReportComment_ReportCountTest() throws BaseException {
        //given
        Integer reporterId = 1;
        Integer reportedCommentId = 1;
        User reporter = utilService.findByUserIdWithValidation(reporterId);
        Comment reportedComment = utilService.findByCommentIdWithValidation(reportedCommentId);
        int beforeReportCount = reportedComment.getReport();

        //when
        reportService.saveReportComment(reporter, reportedComment);

        //then
        int afterReporterCount = utilService.findByCommentIdWithValidation(reportedCommentId).getReport();
        Assertions.assertThat(afterReporterCount).isEqualTo(beforeReportCount+1);
    }

    @Test
    @DisplayName("자신의 댓글을 신고할 경우 예외처리한다.")
    @Transactional
    void saveReportComment_self_exception() throws BaseException {
        //given
        Integer reporterId = 1;
        Integer reportedCommentId = 2;
        User reporter = utilService.findByUserIdWithValidation(reporterId);
        Comment reportedComment = utilService.findByCommentIdWithValidation(reportedCommentId);

        // when, then
        Assertions.assertThatThrownBy(() -> reportService.saveReportComment(reporter, reportedComment))
                .isInstanceOf(BaseException.class)
                .extracting("status").isEqualTo(BaseResponseStatus.REPORT_COMMENT_SELF);
    }
}