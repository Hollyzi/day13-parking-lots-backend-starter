package org.afs.pakinglot.DTO;

import org.afs.pakinglot.domain.Car;

public class CarRequest {
    private String plateNumber;

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Car toCar(CarRequest carRequest) {
        return new Car(carRequest.getPlateNumber());
    }
}
