package net.codejava.domain.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.codejava.domain.enums.CarTransimission;
import net.codejava.domain.enums.FuelType;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Integer id;

    private String name;
    private String licensePlate;
    private String brand;
    private String color;
    private String model;
    private Integer numberOfSeats;
    private Integer productionYear;

    @Enumerated(EnumType.STRING)
    private CarTransimission carTransimission;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    private Integer mileage;
    private Float fuelConsumption;
    private String address;
    private String description;
    private String additionalFunctions;
    private Double basePrice;
    private Double deposit;
    private String termsOfUse;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date updatedAt;

    private Double rating;
    private Boolean isActive;
    private Boolean isAvailable;
    private Boolean isStopped;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_owner_id", referencedColumnName = "user_id")
    private User carOwner;

    @JsonIgnore
    @OneToMany(
            mappedBy = "car",
            targetEntity = Booking.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Booking> booking = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            mappedBy = "car",
            targetEntity = Image.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public void addImage(Image image) {
        images.add(image);
        image.setCar(this);
    }
}
