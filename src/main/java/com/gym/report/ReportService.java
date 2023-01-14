package com.gym.report;

import com.gym.config.exception.BaseException;
import com.gym.user.User;
import com.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.gym.config.exception.BaseResponseStatus.INVALID_USER;

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
}