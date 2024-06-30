package com.db8.popupcoffee.merchant.domain;

import static com.db8.popupcoffee.global.util.Policy.*;

import lombok.Getter;

@Getter
public enum Grade {
    WHITE(0, WHITE_SHARE_PERCENTAGE, WHITE_DISCOUNT_PERCENTAGE),
    GREEN(GREEN_MIN_SCORE, GREEN_SHARE_PERCENTAGE, GREEN_DISCOUNT_PERCENTAGE),
    PURPLE(PURPLE_MIN_SCORE, PURPLE_SHARE_PERCENTAGE, PURPLE_DISCOUNT_PERCENTAGE),
    VIP(VIP_MIN_SCORE, VIP_SHARE_PERCENTAGE, VIP_DISCOUNT_PERCENTAGE);

    @Getter
    private final int minimumScore;
    private final double sharingPercentage;
    private final double discountPercentage;

    Grade(int minimumScore, double sharingPercentage, double discountPercentage) {
        this.minimumScore = minimumScore;
        this.sharingPercentage = sharingPercentage;
        this.discountPercentage = discountPercentage;
    }

    public static Grade from(int gradeScore) {
        if (gradeScore > VIP.minimumScore) {
            return VIP;
        }
        if (gradeScore > PURPLE.minimumScore) {
            return PURPLE;
        }
        if (gradeScore > GREEN.minimumScore) {
            return GREEN;
        }
        return WHITE;
    }

    public Grade nextGrade() {
        return switch (this) {
            case VIP -> null;
            case PURPLE -> VIP;
            case GREEN -> PURPLE;
            case WHITE -> GREEN;
        };
    }

    public int scoreForNextGrade(int gradeScore) {
        Grade nextGrade = nextGrade();
        if (nextGrade == null) {
            return 0;
        }
        return switch (nextGrade) {
            case VIP -> VIP_MIN_SCORE - gradeScore;
            case PURPLE -> PURPLE.minimumScore - gradeScore;
            case GREEN -> GREEN.minimumScore - gradeScore;
            case WHITE -> throw new IllegalArgumentException("다음 등급이 WHITE 가 될 수 없습니다.");
        };
    }

    public long getDaysForNextGrade(int gradeScore) {
        if (this == VIP) {
            return 0;
        }
        int rest = scoreForNextGrade(gradeScore) % SCORE_CHANGES_PER_DAY;

        if (rest > 0) {
            return scoreForNextGrade(gradeScore) / SCORE_CHANGES_PER_DAY + 1L;
        }
        return scoreForNextGrade(gradeScore) / SCORE_CHANGES_PER_DAY;
    }

    public long revenueForNextGrade(int gradeScore) {
        if (this == VIP) {
            return 0;
        }
        return (long) scoreForNextGrade(gradeScore) * REVENUE_FOR_ONE_SCORE;
    }

    public int getNextGradeMinScore() {
        if (this.nextGrade() != null) {
            return this.nextGrade().getMinimumScore();
        } else {
            return this.getMinimumScore();
        }
    }

}
