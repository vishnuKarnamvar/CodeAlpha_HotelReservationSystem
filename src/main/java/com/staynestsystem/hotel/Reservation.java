package com.staynestsystem.hotel;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(
            name = "booking_id",
            unique = true,
            nullable = false
    )
    private String bookingId;


    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private Room room;


    @Column(
            name = "check_in",
            nullable = false
    )
    private LocalDate checkIn;


    @Column(
            name = "check_out",
            nullable = false
    )
    private LocalDate checkOut;


    private int nights;


    @Column(name = "total_amount")
    private double totalAmount;


    @Enumerated(EnumType.STRING)
    private ReservationStatus status =
            ReservationStatus.CONFIRMED;


    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;


    @Column(name = "transaction_id")
    private String transactionId;


    public Reservation() {
    }


    // Getters

    public Long getId() {
        return id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public int getNights() {
        return nights;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }


    // Setters

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setPaymentMethod(
            PaymentMethod paymentMethod
    ) {
        this.paymentMethod = paymentMethod;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}