package org.afs.pakinglot.domain;

import org.afs.pakinglot.DTO.CarRequest;
import org.afs.pakinglot.Exception.ExistException;
import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ParkingLotManager {

    public static final String CAR_EXISTS = "Car with this plate number already exists";
    private List<ParkingLot> parkingLots;
    private List<ParkingBoy> parkingBoys;

    public ParkingLotManager() {
        parkingLots = new ArrayList<>();
        ParkingLot thePlazaPark = new ParkingLot(1,"The Plaza Park", 9);
        parkingLots.add(thePlazaPark);
        ParkingLot cityMallGarage = new ParkingLot(2,"City Mall Garage", 12);
        parkingLots.add(cityMallGarage);
        ParkingLot officeTowerParking = new ParkingLot(3,"Office Tower Parking", 9);
        parkingLots.add(officeTowerParking);
        // Initialize some cars in the parking lots
        Ticket ticket1 = thePlazaPark.park(new Car("AB-1123"));
        Ticket ticket2 = cityMallGarage.park(new Car("XY-1789"));
        Ticket ticket3 = officeTowerParking.park(new Car("LM-1456"));

        parkingBoys = new ArrayList<>();
        parkingBoys.add(new ParkingBoy(parkingLots, new AvailableRateStrategy()));
        parkingBoys.add(new ParkingBoy(parkingLots, new MaxAvailableStrategy()));
        parkingBoys.add(new ParkingBoy(parkingLots, new SequentiallyStrategy()));
    }

    public Ticket parkCar(String plateNumber, String strategy) {
        if (plateNumber == null || !plateNumber.matches("^[A-Z]{2}-\\d{4}$")) {
            throw new IllegalArgumentException("Invalid plate number");
        }
        // Check if the car's plate number already exists in the tickets
        for (ParkingLot parkingLot : parkingLots) {
            for (Ticket ticket : parkingLot.getTickets()) {
                if (ticket.plateNumber().equals(plateNumber)) {
                    throw new ExistException(CAR_EXISTS);
                }
            }
        }
        Car car = new Car(plateNumber);
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
        for (ParkingBoy parkingBoy : parkingBoys) {
            if (parkingBoy.getParkingStrategy().getClass().equals(parkingStrategy.getClass())) {
                Ticket ticket = parkingBoy.park(car);
                if (ticket != null) {
                    return ticket;
                }
            }
        }
        return null; // No available parking space
    }

    public Map<String, List<Ticket>> getCars() {
        Map<String, List<Ticket>> carsMap = new HashMap<>();
        for (ParkingLot parkingLot : parkingLots) {
            carsMap.put(parkingLot.getName(),parkingLot.getTickets());
        }
        return carsMap;
    }


    public Car fetchCar(Ticket ticket) {
        for (ParkingBoy parkingBoy : parkingBoys) {
            Car car = parkingBoy.fetch(ticket);
            if (car != null) {
                return car;
            }
        }
        return null; // Car not found
    }
}
