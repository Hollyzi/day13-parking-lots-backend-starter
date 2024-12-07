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
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@WebMvcTest(ParkinglotManagerController.class)
@AutoConfigureJsonTesters
public class ParkinglotManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingLotManager parkingLotManager;

    @Autowired
    private JacksonTester<Car> carJacksonTester;


    private Car car;
    private Ticket ticket;
    private String plateNumber = "AB-1123";

    @BeforeEach
    public void setup() {
        car = new Car(plateNumber);
        ticket = new Ticket(plateNumber, 1, 1);
    }

    @Test
    public void shouldReturnTicketWhenParkCarWithStandardStrategy() throws Exception {
        ParkingStrategy strategy = new SequentiallyStrategy();
        Mockito.when(parkingLotManager.parkCar(Mockito.any(String.class), Mockito.any(String.class)))
                .thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/parkinglotManager/park")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plateNumber\":\"AB-1123\"}")
                        .param("strategy", "standard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber", is("AB-1123")))
                .andExpect(jsonPath("$.position", is(1)))
                .andExpect(jsonPath("$.parkingLot", is(1)));
    }

    @Test
    public void shouldReturnTicketWhenParkCarWithSmartStrategy() throws Exception {
        ParkingStrategy strategy = new MaxAvailableStrategy();
        Mockito.when(parkingLotManager.parkCar(Mockito.any(String.class), Mockito.any(String.class)))
                .thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/parkinglotManager/park")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plateNumber\":\"AB-1123\"}")
                        .param("strategy", "smart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber", is("AB-1123")))
                .andExpect(jsonPath("$.position", is(1)))
                .andExpect(jsonPath("$.parkingLot", is(1)));
    }

    @Test
    public void shouldReturnTicketWhenParkCarWithSuperSmartStrategy() throws Exception {
        ParkingStrategy strategy = new AvailableRateStrategy();
        Mockito.when(parkingLotManager.parkCar(Mockito.any(String.class), Mockito.any(String.class)))
                .thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/parkinglotManager/park")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plateNumber\":\"AB-1123\"}")
                        .param("strategy", "supersmart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber", is("AB-1123")))
                .andExpect(jsonPath("$.position", is(1)))
                .andExpect(jsonPath("$.parkingLot", is(1)));
    }

    @Test
    public void shouldReturnAllCarsWhenGetCars() throws Exception {
        Mockito.when(parkingLotManager.getCars()).thenReturn(Map.of("The Plaza Park", Arrays.asList(ticket)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/parkinglotManager/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['The Plaza Park']", hasSize(1)))
                .andExpect(jsonPath("$.['The Plaza Park'][0].plateNumber", is("AB-1123")));
    }
    @Test
    public void shouldReturnCarWhenFetchCarWithValidTicket() throws Exception {
        Mockito.when(parkingLotManager.fetchCar(Mockito.any(Ticket.class))).thenReturn(car);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/parkinglotManager/fetch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plateNumber\":\"AB-1123\"}"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                });
    }
}