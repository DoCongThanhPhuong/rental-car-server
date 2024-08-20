package net.codejava.domain.entity;

import java.util.Date;

import jakarta.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feedbacks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Integer id;

    private Integer rating;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date createdAt;

    private String content;

    @JsonIgnore
    @OneToOne(targetEntity = Booking.class, fetch = FetchType.EAGER, orphanRemoval = false)
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
