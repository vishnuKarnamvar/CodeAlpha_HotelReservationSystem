package com.staynestsystem.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {


    Optional<Reservation>
    findByBookingId(String bookingId);


    @Query("""
        SELECT COUNT(r)
        FROM Reservation r

        WHERE r.room.id = :roomId

        AND r.status =
        com.staynestsystem.hotel.ReservationStatus.CONFIRMED

        AND r.checkIn < :checkOut

        AND r.checkOut > :checkIn
    """)
    long countOverlapping(

            @Param("roomId")
            Long roomId,

            @Param("checkIn")
            LocalDate checkIn,

            @Param("checkOut")
            LocalDate checkOut
    );


    long countByStatus(
            ReservationStatus status
    );
}