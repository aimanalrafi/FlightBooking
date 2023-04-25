package de.thb.Flight.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="aircraft" )
public class Aircraft {
    @Id
    @Column(name = "aircraft_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long aircraftId;

    private String manufacturer;
    private String model;

    @Column(name = "number_of_seats")
    private Integer numberOfSeats;


    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Flight> flights = new ArrayList<>();

}
