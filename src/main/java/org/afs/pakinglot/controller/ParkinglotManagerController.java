package org.afs.pakinglot.controller;

import org.afs.pakinglot.DTO.CarRequest;
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
        if (carRequest == null || !carRequest.getPlateNumber().matches("^[A-Z]{2}-\\d{4}$")) {
            return ResponseEntity.badRequest().body("Invalid car format. Expected format is \"AA-1234\".");
        }

        ParkingStrategy parkingStrategy;
        switch (strategy.toLowerCase()) {
            case "smart":
                parkingStrategy = new MaxAvailableStrategy();
                break;
            case "supersmart":
                parkingStrategy = new AvailableRateStrategy();
                break;
            case "standard":
            default:
                parkingStrategy = new SequentiallyStrategy();
                break;
        }
        return ResponseEntity.ok(parkingLotManager.parkCar(carRequest, parkingStrategy));
    }

    @GetMapping("/cars")
    public Map<String, List<Ticket>> getCars() {
        return parkingLotManager.getCars();
    }

    @PostMapping("/fetch")
    public Car fetchCar(@RequestBody CarRequest carRequest) {
        List<Ticket> tickets = parkingLotManager.getCars().values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        Ticket ticket = tickets.stream()
                .filter(t -> t.plateNumber().equals(carRequest.getPlateNumber()))
                .findFirst()
                .orElse(null);

        return parkingLotManager.fetchCar(ticket);
    }
}