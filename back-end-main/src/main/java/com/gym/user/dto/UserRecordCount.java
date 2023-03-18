package com.gym.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserRecordCount {
    private int thisMonthRecord;
    private int remainUpgradeCount;
    private int cumulativeCount;

    public UserRecordCount(int thisMonthRecord, int remainUpgradeCount, int cumulativeCount) {
        this.thisMonthRecord = thisMonthRecord;
        this.remainUpgradeCount = remainUpgradeCount;
        this.cumulativeCount = cumulativeCount;
    }
}
