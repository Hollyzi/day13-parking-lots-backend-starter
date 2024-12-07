package org.afs.pakinglot.domain;

import org.afs.pakinglot.DTO.FetchCarResponse;
import org.afs.pakinglot.Exception.ExistException;
import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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

    public FetchCarResponse fetchCar(String  plateNumber) {
        Ticket fetchTicket=null;
        Car car=null;
        for (ParkingLot parkingLot : parkingLots) {
            for (Ticket ticket : parkingLot.getTickets()) {
                if (ticket.plateNumber().equals(plateNumber)) {
                    fetchTicket=ticket;
                }
            }
        }
        for (ParkingBoy parkingBoy : parkingBoys) {
            car = parkingBoy.fetch(fetchTicket);
            if (car != null) {
                break;
            }
            return null;
        }
        Result parkingTimeInformation = getParkingTimeInformation(fetchTicket);
        // Calculate parking fees
        double parkingFees = getParkingFees(parkingTimeInformation);
        return new FetchCarResponse(car, parkingTimeInformation.entryTime(), parkingTimeInformation.exitTime(), parkingTimeInformation.durationString().toString(),parkingFees);
    }

    public static double getParkingFees(Result parkingTimeInformation) {
        long totalMinutes = parkingTimeInformation.duration().toMinutes();
        double parkingFees = Math.ceil(totalMinutes / 15.0) * 4;
        return parkingFees;
    }

    public static Result getParkingTimeInformation(Ticket fetchTicket) {
        LocalDateTime entryTime = fetchTicket.entryTime();
        LocalDateTime exitTime = LocalDateTime.now();
        Duration duration = Duration.between(entryTime, exitTime);

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;

        StringBuilder durationString = new StringBuilder();
        if (days > 0) {
            durationString.append(days).append(" days + ");
        }
        durationString.append(hours).append(" hours ").append(minutes).append(" minutes");
        Result result = new Result(entryTime, exitTime, duration, durationString);
        return result;
    }

    public record Result(LocalDateTime entryTime, LocalDateTime exitTime, Duration duration, StringBuilder durationString) {
    }
}
