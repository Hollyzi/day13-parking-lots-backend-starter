package org.afs.pakinglot.controller;

import org.afs.pakinglot.domain.Car;
import org.afs.pakinglot.domain.ParkingLotManager;
import org.afs.pakinglot.domain.Ticket;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parkinglotManager")
public class ParkinglotManagerController {

    private ParkingLotManager parkingLotManager;

    public ParkinglotManagerController(ParkingLotManager parkingLotManager) {
        this.parkingLotManager = parkingLotManager;
    }

    @PostMapping("/park")
    public Ticket parkCar(@RequestBody Car car, @RequestParam String strategy) {
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
        return parkingLotManager.parkCar(car, parkingStrategy);
    }

    @GetMapping("/cars")
    public List<Car> getCars() {
        return parkingLotManager.getCars();
    }

    @PostMapping("/fetch")
    public Car fetchCar(@RequestBody Ticket ticket) {
        return parkingLotManager.fetchCar(ticket);
    }
}