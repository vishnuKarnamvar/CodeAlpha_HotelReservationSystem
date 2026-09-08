package com.staynestsystem.hotel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

import java.util.LinkedHashMap;
import java.util.Map;


@RestController
public class HotelController {


    private final HotelService service;


    public HotelController(
            HotelService service
    ) {

        this.service = service;
    }


    // ==========================================
    // HOME PAGE
    // ==========================================

    @GetMapping("/")
    public ModelAndView home() {

        return new ModelAndView(
                "index"
        );
    }


    // ==========================================
    // SEARCH ROOMS
    // ==========================================

    @GetMapping("/api/rooms")
    public ResponseEntity<?> rooms(

            @RequestParam String checkIn,

            @RequestParam String checkOut,

            @RequestParam(
                    defaultValue = "ALL"
            )
            String type

    ) {

        try {

            LocalDate in =
                    LocalDate.parse(checkIn);

            LocalDate out =
                    LocalDate.parse(checkOut);


            return ResponseEntity.ok(

                    service.availableRooms(
                            in,
                            out,
                            type
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }


    // ==========================================
    // BOOK ROOM
    // ==========================================

    @PostMapping("/api/bookings")
    public ResponseEntity<?> book(

            @RequestBody
            BookingRequest request

    ) {

        try {

            Reservation reservation =
                    service.book(

                            request.name(),

                            request.phone(),

                            request.email(),

                            request.roomId(),

                            LocalDate.parse(
                                    request.checkIn()
                            ),

                            LocalDate.parse(
                                    request.checkOut()
                            ),

                            PaymentMethod.valueOf(
                                    request.paymentMethod()
                                            .toUpperCase()
                            )
                    );


            Map<String, Object> response =
                    new LinkedHashMap<>();


            response.put(
                    "bookingId",
                    reservation.getBookingId()
            );


            response.put(
                    "roomNumber",
                    reservation.getRoom()
                            .getRoomNumber()
            );


            response.put(
                    "roomType",
                    reservation.getRoom()
                            .getRoomType()
            );


            response.put(
                    "name",
                    reservation.getCustomer()
                            .getName()
            );


            response.put(
                    "phone",
                    reservation.getCustomer()
                            .getPhone()
            );


            response.put(
                    "email",
                    reservation.getCustomer()
                            .getEmail()
            );


            response.put(
                    "checkIn",
                    reservation.getCheckIn()
            );


            response.put(
                    "checkOut",
                    reservation.getCheckOut()
            );


            response.put(
                    "nights",
                    reservation.getNights()
            );


            response.put(
                    "total",
                    reservation.getTotalAmount()
            );


            response.put(
                    "status",
                    reservation.getStatus()
            );


            response.put(
                    "paymentMethod",
                    reservation.getPaymentMethod()
            );


            response.put(
                    "transactionId",
                    reservation.getTransactionId()
            );


            return ResponseEntity
                    .status(
                            HttpStatus.CREATED
                    )
                    .body(response);


        } catch (Exception e) {

            return ResponseEntity
                    .status(
                            HttpStatus.CONFLICT
                    )
                    .body(
                            Map.of(
                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }


    // ==========================================
    // VIEW BOOKING
    // ==========================================

    @GetMapping(
            "/api/bookings/{bookingId}"
    )
    public ResponseEntity<?> booking(

            @PathVariable
            String bookingId

    ) {

        try {

            Reservation reservation =
                    service.findBooking(
                            bookingId
                    );


            return ResponseEntity.ok(
                    convertReservation(
                            reservation
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(
                            HttpStatus.NOT_FOUND
                    )
                    .body(
                            Map.of(
                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }


    // ==========================================
    // CANCEL BOOKING
    // ==========================================

    @PatchMapping(
            "/api/bookings/{bookingId}/cancel"
    )
    public ResponseEntity<?> cancel(

            @PathVariable
            String bookingId

    ) {

        try {

            service.cancel(
                    bookingId
            );


            return ResponseEntity.ok(

                    Map.of(
                            "message",
                            "Reservation cancelled successfully."
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }


    // ==========================================
    // STATISTICS
    // ==========================================

    @GetMapping("/api/stats")
    public Map<String, Long> stats() {

        return Map.of(

                "rooms",
                service.roomCount(),

                "bookings",
                service.bookingCount(),

                "customers",
                service.customerCount()
        );
    }


    // ==========================================
    // CONVERT RESERVATION
    // ==========================================

    private Map<String, Object>
    convertReservation(
            Reservation reservation
    ) {

        Map<String, Object> data =
                new LinkedHashMap<>();


        data.put(
                "bookingId",
                reservation.getBookingId()
        );


        data.put(
                "roomNumber",
                reservation.getRoom()
                        .getRoomNumber()
        );


        data.put(
                "roomType",
                reservation.getRoom()
                        .getRoomType()
        );


        data.put(
                "name",
                reservation.getCustomer()
                        .getName()
        );


        data.put(
                "phone",
                reservation.getCustomer()
                        .getPhone()
        );


        data.put(
                "email",
                reservation.getCustomer()
                        .getEmail()
        );


        data.put(
                "checkIn",
                reservation.getCheckIn()
        );


        data.put(
                "checkOut",
                reservation.getCheckOut()
        );


        data.put(
                "nights",
                reservation.getNights()
        );


        data.put(
                "total",
                reservation.getTotalAmount()
        );


        data.put(
                "status",
                reservation.getStatus()
        );


        data.put(
                "paymentMethod",
                reservation.getPaymentMethod()
        );


        data.put(
                "transactionId",
                reservation.getTransactionId()
        );


        return data;
    }


    // ==========================================
    // BOOKING REQUEST DTO
    // ==========================================

    public record BookingRequest(

            String name,

            String phone,

            String email,

            Long roomId,

            String checkIn,

            String checkOut,

            String paymentMethod

    ) {
    }
}