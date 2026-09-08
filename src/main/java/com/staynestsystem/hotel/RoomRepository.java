package com.staynestsystem.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository
        extends JpaRepository<Room, Long> {


    @Query("""
        SELECT r
        FROM Room r
        WHERE r.status =
        com.staynestsystem.hotel.RoomStatus.AVAILABLE

        AND
        (
            :type = 'ALL'
            OR r.roomType = :roomType
        )

        AND NOT EXISTS
        (
            SELECT res
            FROM Reservation res

            WHERE res.room.id = r.id

            AND res.status =
            com.staynestsystem.hotel.ReservationStatus.CONFIRMED

            AND res.checkIn < :checkOut

            AND res.checkOut > :checkIn
        )

        ORDER BY r.id
    """)
    List<Room> findAvailableRooms(

            @Param("checkIn")
            LocalDate checkIn,

            @Param("checkOut")
            LocalDate checkOut,

            @Param("type")
            String type,

            @Param("roomType")
            RoomType roomType
    );
}