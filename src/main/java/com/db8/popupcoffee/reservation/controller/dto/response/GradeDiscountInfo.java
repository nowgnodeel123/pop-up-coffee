package com.db8.popupcoffee.reservation.controller.dto.response;

import com.db8.popupcoffee.merchant.domain.Grade;
import lombok.Builder;

@Builder
public record GradeDiscountInfo(
    Double discountPercentage,
    Double nextGradeDiscountPercentage,
    Double revenueSharingPercentage,
    Double nextGradeRevenueSharingPercentage,
    Long daysForNextGrade,
    Integer scoreForNextGrade,
    Long revenueForNextGrade,
    Grade currentGrade
) {

    public static GradeDiscountInfo from(int gradeScore) {
        Grade grade = com.db8.popupcoffee.merchant.domain.Grade.from(gradeScore);
        Grade nextGrade = grade.nextGrade();
        GradeDiscountInfoBuilder builder = GradeDiscountInfo.builder()
            .discountPercentage(grade.getDiscountPercentage())
            .revenueSharingPercentage(grade.getSharingPercentage())
            .currentGrade(grade);

        if (nextGrade != null) {
            builder
                .nextGradeDiscountPercentage(nextGrade.getDiscountPercentage())
                .nextGradeRevenueSharingPercentage(nextGrade.getSharingPercentage())
                .daysForNextGrade(grade.getDaysForNextGrade(gradeScore))
                .scoreForNextGrade(grade.scoreForNextGrade(gradeScore))
                .revenueForNextGrade(grade.revenueForNextGrade(gradeScore));
        }

        return builder.build();
    }
}
