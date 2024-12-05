package org.afs.pakinglot.controller;

import org.afs.pakinglot.domain.Car;
import org.afs.pakinglot.domain.ParkingLotManager;
import org.afs.pakinglot.domain.Ticket;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@WebMvcTest(ParkinglotManagerController.class)
public class ParkinglotManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingLotManager parkingLotManager;

    private Car car;
    private Ticket ticket;

    @BeforeEach
    public void setup() {
        car = new Car("ABC123");
        ticket = new Ticket("ticket123", 1, 1);
    }

    @Test
    public void shouldReturnTicketWhenParkCarWithStandardStrategy() throws Exception {
        ParkingStrategy strategy = new SequentiallyStrategy();
        Mockito.when(parkingLotManager.parkCar(Mockito.any(Car.class), Mockito.any(ParkingStrategy.class)))
                .thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/parkinglotManager/park")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"licensePlate\":\"ABC123\"}")
                        .param("strategy", "standard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber", is("ticket123")))
                .andExpect(jsonPath("$.position", is(1)))
                .andExpect(jsonPath("$.parkingLot", is(1)));
    }

    @Test
    public void shouldReturnTicketWhenParkCarWithSmartStrategy() throws Exception {
        ParkingStrategy strategy = new MaxAvailableStrategy();
        Mockito.when(parkingLotManager.parkCar(Mockito.any(Car.class), Mockito.any(ParkingStrategy.class)))
                .thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/parkinglotManager/park")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"licensePlate\":\"ABC123\"}")
                        .param("strategy", "smart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber", is("ticket123")))
                .andExpect(jsonPath("$.position", is(1)))
                .andExpect(jsonPath("$.parkingLot", is(1)));
    }

    @Test
    public void shouldReturnTicketWhenParkCarWithSuperSmartStrategy() throws Exception {
        ParkingStrategy strategy = new AvailableRateStrategy();
        Mockito.when(parkingLotManager.parkCar(Mockito.any(Car.class), Mockito.any(ParkingStrategy.class)))
                .thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/parkinglotManager/park")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"licensePlate\":\"ABC123\"}")
                        .param("strategy", "supersmart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber", is("ticket123")))
                .andExpect(jsonPath("$.position", is(1)))
                .andExpect(jsonPath("$.parkingLot", is(1)));
    }

    @Test
    public void shouldReturnAllCarsWhenGetCars() throws Exception {
        Mockito.when(parkingLotManager.getCars()).thenReturn(Arrays.asList(car));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/parkinglotManager/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].plateNumber", is("ABC123")));
    }

    @Test
    public void shouldReturnCarWhenFetchCarWithValidTicket() throws Exception {
        Mockito.when(parkingLotManager.fetchCar(Mockito.any(Ticket.class))).thenReturn(car);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/parkinglotManager/fetch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plateNumber\":\"ticket123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber", is("ABC123")));
    }
}