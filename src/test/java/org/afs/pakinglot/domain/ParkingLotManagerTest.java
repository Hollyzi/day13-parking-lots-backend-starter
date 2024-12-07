package org.afs.pakinglot.domain;

import org.afs.pakinglot.DTO.CarRequest;
import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParkingLotManagerTest {
    @Test
    void should_return_ticket_when_parkCar_then_ticketIsReturned() {
        // Given
        ParkingLotManager manager = new ParkingLotManager();
        String plateNumber = CarPlateGenerator.generatePlate();
        Ticket ticket = manager.parkCar(plateNumber, "supersmart");

        // Then
        assertNotNull(ticket);
    }

    @Test
    void should_return_car_when_fetchCar_then_carIsReturned() {
        // Given
        ParkingLotManager manager = new ParkingLotManager();
        String plateNumber = CarPlateGenerator.generatePlate();
        Ticket ticket = manager.parkCar(plateNumber, "supersmart");

        // When
        Car fetchedCar = manager.fetchCar(ticket.plateNumber()).getCar();

        // Then
        assertNotNull(fetchedCar);
        assertEquals(plateNumber, fetchedCar.plateNumber());
    }

    @Test
    void should_return_all_parked_cars_when_getCars_then_all_parked_cars_are_returned() {
        // Given
        ParkingLotManager manager = new ParkingLotManager();
        String plateNumber1 = CarPlateGenerator.generatePlate();
        String plateNumber2 = CarPlateGenerator.generatePlate();
        manager.parkCar(plateNumber1, "supersmart");
        manager.parkCar(plateNumber2, "supersmart");

        // When
        Map<String, List<Ticket>> carsMap = manager.getCars();

        // Then
        assertEquals(3, carsMap.size());
        assertTrue(carsMap.values().stream().flatMap(List::stream).anyMatch(ticket -> ticket.plateNumber().equals(plateNumber1)));
        assertTrue(carsMap.values().stream().flatMap(List::stream).anyMatch(ticket -> ticket.plateNumber().equals(plateNumber2)));
    }
}