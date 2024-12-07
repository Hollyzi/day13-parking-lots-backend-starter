package org.afs.pakinglot.DTO;

import org.afs.pakinglot.domain.Car;

import java.time.LocalDateTime;

public class FetchCarResponse {
    private Car car;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private String parkingDuration;
    private double parkingFees;

    public FetchCarResponse(Car car, LocalDateTime entryTime, LocalDateTime exitTime, String parkingDuration, double parkingFees) {
        this.car = car;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.parkingDuration = parkingDuration;
        this.parkingFees = parkingFees;
    }

    public Car getCar() {
        return car;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public String getParkingDuration() {
        return parkingDuration;
    }

    public double getParkingFees() {
        return parkingFees;
    }
}