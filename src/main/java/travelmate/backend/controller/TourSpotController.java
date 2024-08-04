package travelmate.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import travelmate.backend.dto.*;
import travelmate.backend.service.TourSpotService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TourSpotController {

    private final TourSpotService tourSpotService;

    @GetMapping("/tourspot/regions")
    public List<TourRegionDto> getRegions() {
        List<TourRegionDto> regions = tourSpotService.findAllRegions();
        return regions;
    }

    @GetMapping("/tourspot/districts")
    public List<TourDistrictDto> getDistricts() {
        List<TourDistrictDto> districts = tourSpotService.findAllDistricts();
        return districts;
    }

    @GetMapping("/tourspot/regions/{regionId}/districts")
    public List<TourDistrictDto> getDistrictsInRegion(@PathVariable Long regionId) {
        List<TourDistrictDto> districts = tourSpotService.findDistrictsByRegionId(regionId);
        return districts;
    }

    @GetMapping("/tourspot/themes")
    public List<TourSpotThemeDto> getThemes() {
        List<TourSpotThemeDto> themes = tourSpotService.findAllTourSpotThemes();
        return themes;
    }

    @GetMapping("/tourspots")
    public Page<TourSpotDto> getTourSpots(@ModelAttribute TourSpotQueryRequest queryRequest) {
        Page<TourSpotDto> tourSpotPage = tourSpotService.findTourSpotsByQuery(queryRequest);
        return tourSpotPage;
    }

    @GetMapping("/tourspots/{tourSpotId}")
    public TourSpotDetailDto getTourSpotDetail(@PathVariable Long tourSpotId) {
        TourSpotDetailDto tourSpotDetail = tourSpotService.findTourSpotDetailById(tourSpotId);
        return tourSpotDetail;
    }

}
