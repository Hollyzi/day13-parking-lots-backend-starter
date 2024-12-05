package org.afs.pakinglot.domain;

import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ParkingLotManager {

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

    public Ticket parkCar(Car car, ParkingStrategy strategy) {
        for (ParkingBoy parkingBoy : parkingBoys) {
            if (parkingBoy.getParkingStrategy().getClass().equals(strategy.getClass())) {
                Ticket ticket = parkingBoy.park(car);
                if (ticket != null) {
                    return ticket;
                }
            }
        }
        return null; // No available parking space
    }

//    public List<Car> getCars() {
//        List<Car> cars = new ArrayList<>();
//        for (ParkingLot parkingLot : parkingLots) {
//            cars.addAll(parkingLot.getCars());
//        }
//        return cars;
//    }
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
