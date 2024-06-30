package com.db8.popupcoffee.reservation.domain;

import com.db8.popupcoffee.contract.domain.MerchantContract;
import com.db8.popupcoffee.global.domain.BaseTimeEntity;
import com.db8.popupcoffee.global.domain.CreditCard;
import com.db8.popupcoffee.merchant.domain.BusinessType;
import com.db8.popupcoffee.rental.domain.SpaceRentalAgreement;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FixedReservation extends BaseTimeEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MerchantContract merchantContract;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SpaceRentalAgreement spaceRentalAgreement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FixedReservationStatus status = FixedReservationStatus.FIXED;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private long rentalFee;

    @Column(nullable = false)
    private long rentalDeposit;

    @Column(nullable = false)
    private boolean fromFlexibleReservation = false;

    @Embedded
    private CreditCard creditCard;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private BusinessType businessType;

    @Builder
    @SuppressWarnings("java:S107")
    public FixedReservation(
        MerchantContract merchantContract,
        LocalDate startDate,
        LocalDate endDate,
        long rentalFee,
        long rentalDeposit,
        CreditCard creditCard,
        BusinessType businessType,
        boolean fromFlexibleReservation
    ) {
        this.merchantContract = merchantContract;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentalFee = rentalFee;
        this.rentalDeposit = rentalDeposit;
        this.creditCard = creditCard;
        this.businessType = businessType;
        this.fromFlexibleReservation = fromFlexibleReservation;
    }

    public static FixedReservation of(
        FlexibleReservation flexibleReservation,
        CreditCard creditCard,
        long rentalFee,
        long rentalDeposit
    ) {
        return FixedReservation.builder()
            .merchantContract(flexibleReservation.getMerchantContract())
            .startDate(flexibleReservation.getTemporalRentalStartDate())
            .endDate(flexibleReservation.getTemporalRentalEndDate())
            .rentalFee(rentalFee)
            .rentalDeposit(rentalDeposit)
            .creditCard(creditCard)
            .businessType(flexibleReservation.getBusinessType())
            .fromFlexibleReservation(true)
            .build();
    }
}
