package com.gym.report;

import com.gym.post.Post;
import com.gym.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;

    private Integer reporterId; // 신고하는 유저의 ID
    private Integer reportedId; // 신고당하는 것의 ID

    @Enumerated(EnumType.STRING)
    private ReportType type;        // 신고 당하는 것이 무엇인지 구별 (USER, POST, COMMENT 중 1개)
    private String content;     // 신고 내용

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    private void setReporter(Integer userId) {
        this.reporterId = userId;
    }

    private void setReported(Integer reportedId) {
        this.reportedId = reportedId;
    }

    private void setType (ReportType type) {
        this.type = type;
    }

    //==객체 생성 메서드==//

    public static Report createReportUser(User user, User reportedUser) {
        Report report = new Report();
        report.setReporter(user.getUserId());
        report.setReported(reportedUser.getUserId());
        report.setType(ReportType.USER);
        reportedUser.addReportCount();
        return report;
    }

    public static Report createReportPost(User reporter, Post reportedPost) {
        Report report = new Report();
        report.setReporter(reporter.getUserId());
        report.setReported(reportedPost.getPostId());
        report.setType(ReportType.POST);
        reportedPost.addReportCount();
        return report;
    }
}