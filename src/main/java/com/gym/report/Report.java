package com.gym.report;

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
    private String type;        // 신고 당하는 것이 무엇인지 구별 (USER, POST, COMMENT 중 1개)
    private String content;     // 신고 내용

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();
}
