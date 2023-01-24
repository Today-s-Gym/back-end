package com.gym.avatar.avatar;

import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;

@Getter
public enum AvatarStep {
    NONE(0, "none", -1, -1),
    AVATAR1_STEP1(1, "AVATAR1_IMG1", 0, 3),
    AVATAR1_STEP2(2, "AVATAR1_IMG2", 3, 10),
    AVATAR1_STEP3(3, "AVATAR1_IMG3", 10, 17),
    AVATAR1_STEP4(4, "AVATAR1_IMG4", 17, 20),
    AVATAR1_STEP5(5, "AVATAR1_IMG5", 20, 25),
    AVATAR2_STEP1(1, "AVATAR2_IMG1", 25, 30),
    AVATAR2_STEP2(2, "AVATAR2_IMG2", 30, 40),
    AVATAR2_STEP3(3, "AVATAR2_IMG3", 40, 50),
    AVATAR2_STEP4(4, "AVATAR2_IMG4", 50, 65),
    AVATAR2_STEP5(5, "AVATAR2_IMG5", 65, 75),
    AVATAR2_STEP6(6, "AVATAR2_IMG6", 75, 90),
    AVATAR2_STEP7(7, "AVATAR2_IMG7", 90, 100);

    private final int level;
    private final String imgUrl;
    private final int minRecordCount;
    private final int maxRecordCount;

    AvatarStep(int level, String imgUrl, int minRecordCount, int maxRecordCount) {
        this.level = level;
        this.imgUrl = imgUrl;
        this.minRecordCount = minRecordCount;
        this.maxRecordCount = maxRecordCount;
    }

    public int getRemainUpgradeCount(int recordCount) {
        return this.maxRecordCount-recordCount;
    }

    public static String findAvatarImg(AvatarStep avatarStep) {
        return avatarStep.imgUrl;
    }

    public static AvatarStep findByRecordCount(int recordCount) {
        return Arrays.stream(values())
                .filter(step -> step.minRecordCount<=recordCount && step.maxRecordCount>recordCount)
                .findFirst()
                .orElseGet(AvatarStep::getMaxAvatarStep);
    }

    private static AvatarStep getMaxAvatarStep() {
        return Arrays.stream(values())
                .max(Comparator.comparingInt(AvatarStep::getMaxRecordCount))
                .orElse(NONE);
    }
}