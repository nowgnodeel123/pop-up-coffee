package com.db8.popupcoffee.seasonality.service;

import com.db8.popupcoffee.rental.service.RentalService;
import com.db8.popupcoffee.seasonality.controller.dto.request.CreateNormalDatesRequest;
import com.db8.popupcoffee.seasonality.controller.dto.request.InputDateInfoRequest;
import com.db8.popupcoffee.seasonality.controller.dto.response.DateInfoResponse;
import com.db8.popupcoffee.seasonality.domain.DateInfo;
import com.db8.popupcoffee.seasonality.repository.DateInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DateInfoService {

    private final DateInfoRepository dateInfoRepository;
    private final RentalService rentalService;

    @Transactional(readOnly = true)
    public List<DateInfoResponse> findDateInfos() {
        return dateInfoRepository.findAll().stream().map(dateInfo -> DateInfoResponse.of(dateInfo,
            rentalService.countAvailableSpaces(dateInfo.getDate()))).toList();
    }

    @Transactional
    public void createDatesOfYear(CreateNormalDatesRequest request) {
        List<DateInfo> dateInfosOfYear = dateInfoRepository.findAll().stream()
            .filter(dateInfo -> dateInfo.getDate().getYear() == request.year()).toList();
        dateInfoRepository.deleteAll(dateInfosOfYear);
        dateInfoRepository.saveAll(
            getAllDatesInYear(request.year()).stream().map(DateInfo::new).toList());
    }

    @Transactional
    public void inputDateInfos(InputDateInfoRequest request) {
        dateInfoRepository.deleteByDateBetween(request.startDate(), request.endDate());
        dateInfoRepository.saveAll(request.startDate().datesUntil(request.endDate().plusDays(1L)).map(
            date -> request.holiday() == null ? DateInfo.builder().date(date)
                .seasonalityLevel(request.seasonalityLevel())
                .build()
                : DateInfo.builder().date(date).seasonalityLevel(request.seasonalityLevel())
                    .holiday(request.holiday())
                    .build()).toList());
    }

    private List<LocalDate> getAllDatesInYear(int year) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);
        return startOfYear.datesUntil(endOfYear.plusDays(1L)).toList();
    }
}
