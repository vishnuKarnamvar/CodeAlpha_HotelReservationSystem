package com.staynestsystem.hotel;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class HotelService {

    private final RoomRepository roomRepository;

    private final CustomerRepository customerRepository;

    private final ReservationRepository reservationRepository;


    public HotelService(
            RoomRepository roomRepository,
            CustomerRepository customerRepository,
            ReservationRepository reservationRepository
    ) {

        this.roomRepository = roomRepository;

        this.customerRepository = customerRepository;

        this.reservationRepository =
                reservationRepository;
    }


    // ==========================================
    // SEARCH AVAILABLE ROOMS
    // ==========================================

    public List<Room> availableRooms(

            LocalDate checkIn,

            LocalDate checkOut,

            String type
    ) {

        if (
                checkIn == null ||
                checkOut == null ||
                !checkOut.isAfter(checkIn)
        ) {

            throw new IllegalArgumentException(
                    "Please select valid check-in and check-out dates."
            );
        }


        RoomType roomType = null;


        if (!"ALL".equalsIgnoreCase(type)) {

            try {

                roomType =
                        RoomType.valueOf(
                                type.toUpperCase()
                        );

            } catch (IllegalArgumentException e) {

                throw new IllegalArgumentException(
                        "Invalid room type."
                );
            }
        }


        return roomRepository.findAvailableRooms(

                checkIn,

                checkOut,

                type.toUpperCase(),

                roomType
        );
    }


    // ==========================================
    // BOOK ROOM
    // ==========================================

    @Transactional
    public Reservation book(

            String name,

            String phone,

            String email,

            Long roomId,

            LocalDate checkIn,

            LocalDate checkOut,

            PaymentMethod paymentMethod
    ) {


        // Validate dates

        if (
                checkIn == null ||
                checkOut == null ||
                !checkOut.isAfter(checkIn)
        ) {

            throw new IllegalArgumentException(
                    "Invalid check-in or check-out dates."
            );
        }


        // Find room

        Room room =
                roomRepository.findById(roomId)

                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Room not found."
                                )
                        );


        // Check overlapping reservations

        long conflicts =
                reservationRepository.countOverlapping(

                        roomId,

                        checkIn,

                        checkOut
                );


        if (conflicts > 0) {

            throw new IllegalStateException(
                    "This room is already booked for the selected dates."
            );
        }


        // Find existing customer

        Customer customer =
                customerRepository
                        .findByEmail(email)

                        .orElseGet(
                                () ->
                                        customerRepository.save(
                                                new Customer(
                                                        name,
                                                        phone,
                                                        email
                                                )
                                        )
                        );


        // Update customer details

        customer.setName(name);

        customer.setPhone(phone);

        customerRepository.save(customer);


        // Calculate nights

        int nights =
                (int)
                        (
                                checkOut.toEpochDay()
                                        -
                                checkIn.toEpochDay()
                        );


        // Calculate total

        double total =
                nights *
                room.getPricePerNight();


        // Create reservation

        Reservation reservation =
                new Reservation();


        // Generate booking ID

        String bookingId =
                "HB-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();


        reservation.setBookingId(
                bookingId
        );


        reservation.setCustomer(
                customer
        );


        reservation.setRoom(
                room
        );


        reservation.setCheckIn(
                checkIn
        );


        reservation.setCheckOut(
                checkOut
        );


        reservation.setNights(
                nights
        );


        reservation.setTotalAmount(
                total
        );


        reservation.setStatus(
                ReservationStatus.CONFIRMED
        );


        reservation.setPaymentMethod(
                paymentMethod
        );


        // Simulated transaction ID

        String transactionId =
                "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 10)
                        .toUpperCase();


        reservation.setTransactionId(
                transactionId
        );


        return reservationRepository.save(
                reservation
        );
    }


    // ==========================================
    // CANCEL BOOKING
    // ==========================================

    @Transactional
    public void cancel(
            String bookingId
    ) {

        Reservation reservation =
                reservationRepository
                        .findByBookingId(
                                bookingId
                        )

                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Booking not found."
                                        )
                        );


        if (
                reservation.getStatus()
                        ==
                ReservationStatus.CANCELLED
        ) {

            throw new IllegalStateException(
                    "Booking is already cancelled."
            );
        }


        reservation.setStatus(
                ReservationStatus.CANCELLED
        );


        reservationRepository.save(
                reservation
        );
    }


    // ==========================================
    // FIND BOOKING
    // ==========================================

    public Reservation findBooking(
            String bookingId
    ) {

        return reservationRepository
                .findByBookingId(
                        bookingId
                )

                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "Booking not found."
                                )
                );
    }


    // ==========================================
    // STATISTICS
    // ==========================================

    public long roomCount() {

        return roomRepository.count();
    }


    public long bookingCount() {

        return reservationRepository
                .countByStatus(
                        ReservationStatus.CONFIRMED
                );
    }


    public long customerCount() {

        return customerRepository.count();
    }
}