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
@Table(name="airport")
public class Airport {
    @Id
    @Column(name = "airport_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int airportId;

    @Column(name = "airport_code")
    private String airportCode;

    @Column(name = "airport_name")
    private String airportName;

    private String city;
    private String state;
    private String country;

    @OneToMany(mappedBy = "departureAirport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Flight> departingFlights = new ArrayList<Flight>();

    @OneToMany(mappedBy = "destinationAirport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Flight> arrivingFlights = new ArrayList<Flight>();


}
