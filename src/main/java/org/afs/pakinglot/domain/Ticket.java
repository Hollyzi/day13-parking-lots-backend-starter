package org.afs.pakinglot.domain;

import java.time.LocalDateTime;

public record Ticket(String plateNumber, int position, int parkingLot, LocalDateTime entryTime, LocalDateTime exitTime) {
}
