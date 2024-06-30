package com.db8.popupcoffee.space.controller;

import com.db8.popupcoffee.reservation.service.ReservationService;
import com.db8.popupcoffee.space.controller.dto.request.ReservationIdDto;
import com.db8.popupcoffee.space.controller.dto.request.UpdateAssignmentRequest;
import com.db8.popupcoffee.space.controller.dto.response.SpaceInfo;
import com.db8.popupcoffee.space.service.SpaceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final ReservationService reservationService;
    private final SpaceService spaceService;

    private static final String ASSIGNMENT_VIEW_REQUEST = "redirect:/spaces/assignment";

    @ModelAttribute("spaces")
    public List<SpaceInfo> findAllSpaces() {
        return spaceService.findAllSpaces();
    }

    @GetMapping("/assignment")
    public String getAssignmentForm(Model model) {
        model.addAttribute("nonFixedFlexibles",
            reservationService.findNonFixedFlexibleRepositories());
        model.addAttribute("spaceInfos", spaceService.getReservationInfosOfSpaces());
        return "admins/spaceManagement";
    }

    @DeleteMapping("/assignment")
    public String unAssignReservation(ReservationIdDto request) {
        spaceService.unAssignSpace(request);
        return ASSIGNMENT_VIEW_REQUEST;
    }

    @PatchMapping("/assignment")
    public String updateAssignment(UpdateAssignmentRequest request) {
        spaceService.updateAssignment(request);
        return ASSIGNMENT_VIEW_REQUEST;
    }

    @PatchMapping("/assignment/status/fixed")
    public String updateStatusToSpaceFixed(ReservationIdDto dto) {
        reservationService.updateStatusToSpaceFixed(dto);
        return ASSIGNMENT_VIEW_REQUEST;
    }
}
