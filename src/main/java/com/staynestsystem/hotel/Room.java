package com.staynestsystem.hotel;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(
            name = "room_number",
            nullable = false,
            unique = true
    )
    private String roomNumber;


    @Enumerated(EnumType.STRING)
    @Column(
            name = "room_type",
            nullable = false
    )
    private RoomType roomType;


    @Column(
            name = "price_per_night",
            nullable = false
    )
    private double pricePerNight;


    private String description;


    private String amenities;


    @Enumerated(EnumType.STRING)
    private RoomStatus status = RoomStatus.AVAILABLE;


    // Default constructor
    public Room() {
    }


    public Room(
            String roomNumber,
            RoomType roomType,
            double pricePerNight,
            String description,
            String amenities
    ) {

        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.description = description;
        this.amenities = amenities;
        this.status = RoomStatus.AVAILABLE;
    }


    // Getters

    public Long getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public String getDescription() {
        return description;
    }

    public String getAmenities() {
        return amenities;
    }

    public RoomStatus getStatus() {
        return status;
    }


    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}