package org.afs.pakinglot.controller;

import org.afs.pakinglot.DTO.FetchCarResponse;
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

import java.time.Duration;
import java.time.LocalDateTime;
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
        ticket = new Ticket(plateNumber, 1, 1, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    public void shouldReturn_Ticket_when_ParkCarWithStandardStrategy_then_Success() throws Exception {
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
    public void shouldReturn_Ticket_when_ParkCarWithSmartStrategy_then_Success() throws Exception {
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
    public void shouldReturn_Ticket_when_ParkCarWithSuperSmartStrategy_then_Success() throws Exception {
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
    public void shouldReturn_AllCars_when_GetCars_then_Success() throws Exception {
        Mockito.when(parkingLotManager.getCars()).thenReturn(Map.of("The Plaza Park", Arrays.asList(ticket)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/parkinglotManager/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['The Plaza Park']", hasSize(1)))
                .andExpect(jsonPath("$.['The Plaza Park'][0].plateNumber", is("AB-1123")));
    }

    @Test
    public void shouldReturn_Car_when_FetchCarWithValidTicket_then_Success() throws Exception {
        Mockito.when(parkingLotManager.fetchCar(Mockito.any(String.class))).thenReturn(new FetchCarResponse(car, LocalDateTime.now(), LocalDateTime.now(), "0 hours 1 minutes", 4.0));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/parkinglotManager/fetch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plateNumber\":\"AB-1123\"}"))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.car.plateNumber", is("AB-1123")))
                .andExpect(jsonPath("$.parkingFees", is(4.0)));
    }

    @Test
    public void shouldCalculate_ParkingFeesCorrectly_when_GivenDuration_then_Success() {
        LocalDateTime entryTime = LocalDateTime.now().minusMinutes(1);
        LocalDateTime exitTime = LocalDateTime.now();
        Duration duration = Duration.between(entryTime, exitTime);
        ParkingLotManager.Result result = new ParkingLotManager.Result(entryTime, exitTime, duration, new StringBuilder("0 hours 16 minutes"));

        double parkingFees = ParkingLotManager.getParkingFees(result);

        assertEquals(4.0, parkingFees);
    }
}