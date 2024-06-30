package com.db8.popupcoffee.reservation.service.dto;

import com.db8.popupcoffee.contract.domain.MerchantContract;
import com.db8.popupcoffee.global.domain.Contact;
import com.db8.popupcoffee.merchant.domain.BusinessType;
import com.db8.popupcoffee.reservation.controller.dto.request.CreateFlexibleReservationRequest;
import com.db8.popupcoffee.reservation.domain.FlexibleReservation;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateFlexibleReservationDto(
    long merchantId,
    LocalDate availabilityStartDate,
    LocalDate availabilityEndDate,
    Long duration,
    LocalDate deadline,
    Contact contact,
    List<LocalDate> desiredDates,
    Long businessTypeId
) {

    public static CreateFlexibleReservationDto of(long merchantId,
        CreateFlexibleReservationRequest form) {
        return CreateFlexibleReservationDto.builder()
            .merchantId(merchantId)
            .availabilityStartDate(form.availabilityStartDate())
            .availabilityEndDate(form.availabilityEndDate())
            .duration(form.duration())
            .deadline(form.deadline())
            .desiredDates(form.desiredDates())
            .businessTypeId(form.businessTypeId())
            .build();
    }

    public FlexibleReservation toEntity(MerchantContract contract, BusinessType businessType) {
        return FlexibleReservation.builder()
            .merchantContract(contract)
            .availabilityStartDate(availabilityStartDate)
            .availabilityEndDate(availabilityEndDate)
            .duration(duration)
            .deadline(deadline)
            .businessType(businessType)
            .build();
    }
}
