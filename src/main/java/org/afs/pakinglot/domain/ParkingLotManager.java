package org.afs.pakinglot.domain;

import org.afs.pakinglot.domain.Car;
import org.afs.pakinglot.domain.ParkingBoy;
import org.afs.pakinglot.domain.ParkingLot;
import org.afs.pakinglot.domain.Ticket;
import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;

import java.util.ArrayList;
import java.util.List;

public class ParkingLotManager {

    private List<ParkingLot> parkingLots;
    private List<ParkingBoy> parkingBoys;

    public ParkingLotManager() {
        parkingLots = new ArrayList<>();
        parkingLots.add(new ParkingLot("The Plaza Park", 9));
        parkingLots.add(new ParkingLot("City Mall Garage", 12));
        parkingLots.add(new ParkingLot("Office Tower Parking", 9));

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

    public List<Car> getCars() {
        List<Car> cars = new ArrayList<>();
        for (ParkingLot parkingLot : parkingLots) {
            cars.addAll(parkingLot.getCars());
        }
        return cars;
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
