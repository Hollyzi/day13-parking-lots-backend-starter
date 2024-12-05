package org.afs.pakinglot.domain;

import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParkingLotManagerTest {
    @Test
    void should_return_ticket_when_parkCar_then_ticketIsReturned() {
        // Given
        ParkingLotManager manager = new ParkingLotManager();
        Car car = new Car(CarPlateGenerator.generatePlate());

        // When
        Ticket ticket = manager.parkCar(car, new AvailableRateStrategy());

        // Then
        assertNotNull(ticket);
    }

    @Test
    void should_return_car_when_fetchCar_then_carIsReturned() {
        // Given
        ParkingLotManager manager = new ParkingLotManager();
        Car car = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket = manager.parkCar(car, new AvailableRateStrategy());

        // When
        Car fetchedCar = manager.fetchCar(ticket);

        // Then
        assertNotNull(fetchedCar);
        assertEquals(car, fetchedCar);
    }

    @Test
    void should_return_all_parked_cars_when_getCars_then_all_parked_cars_are_returned() {
        // Given
        ParkingLotManager manager = new ParkingLotManager();
        Car car1 = new Car(CarPlateGenerator.generatePlate());
        Car car2 = new Car(CarPlateGenerator.generatePlate());
        manager.parkCar(car1, new AvailableRateStrategy());
        manager.parkCar(car2, new AvailableRateStrategy());

        // When
        List<Car> cars = manager.getCars();

        // Then
        assertEquals(2, cars.size());
        assertTrue(cars.contains(car1));
        assertTrue(cars.contains(car2));
    }
}