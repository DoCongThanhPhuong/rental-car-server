package net.codejava.domain.entity;

import java.util.Date;

import jakarta.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer id;

    private String name;

    private String imageUrl;

    private String imagePublicId;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date whenCreated;

    @ManyToOne(targetEntity = Car.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;
}
