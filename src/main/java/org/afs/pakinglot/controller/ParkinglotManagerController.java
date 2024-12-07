package org.afs.pakinglot.controller;

import org.afs.pakinglot.DTO.CarRequest;
import org.afs.pakinglot.DTO.FetchCarResponse;
import org.afs.pakinglot.domain.Car;
import org.afs.pakinglot.domain.ParkingLotManager;
import org.afs.pakinglot.domain.Ticket;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parkinglotManager")
public class ParkinglotManagerController {

    private ParkingLotManager parkingLotManager;

    public ParkinglotManagerController(ParkingLotManager parkingLotManager) {
        this.parkingLotManager = parkingLotManager;
    }

    @PostMapping("/park")
    public ResponseEntity<?> parkCar(@RequestBody CarRequest carRequest, @RequestParam String strategy) {
        return ResponseEntity.ok(parkingLotManager.parkCar(carRequest.getPlateNumber(), strategy));
    }

    @GetMapping("/cars")
    public Map<String, List<Ticket>> getCars() {
        return parkingLotManager.getCars();
    }

    @PostMapping("/fetch")
    public FetchCarResponse fetchCar(@RequestBody CarRequest carRequest) {
        return parkingLotManager.fetchCar(carRequest.getPlateNumber());
    }
}