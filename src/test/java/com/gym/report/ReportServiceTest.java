package com.gym.report;

import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.user.User;
import com.gym.user.UserRepository;
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

    @Test
    @DisplayName("유저 신고 report 확인 테스트")
    @Transactional
    void saveReportUserTest() throws BaseException {
        //given
        Integer reporterId = 1;
        Integer reportedUserId = 2;
        User user = userRepository.findById(reporterId).get();

        //when
        Integer reportId = reportService.saveReportUser(user, reportedUserId);

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
        int beforeReportCount = userRepository.findById(reportedUserId).get().getReport();
        User user = userRepository.findById(reporterId).get();

        //when
        reportService.saveReportUser(user, reportedUserId);

        //then
        int afterReporterCount = userRepository.findById(reportedUserId).get().getReport();
        Assertions.assertThat(afterReporterCount).isEqualTo(beforeReportCount+1);
    }
}