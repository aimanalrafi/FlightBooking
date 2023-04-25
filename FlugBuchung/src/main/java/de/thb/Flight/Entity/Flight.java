package de.thb.Flight.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="flight" )
public class Flight {
    @Id
    @Column(name = "flight_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long flightId;

    @Column(name = "flight_number")
    private String flightNumber;

    @Column(name = "departure_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;

    @Column(name = "arrival_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;

    @Column(name = "departure_time")
    private String departureTime;

    @Column(name = "arrival_time")
    private String arrivalTime;

    @Column( name = "flight_charge")
    private float flightCharge;

    /**
     * @JoinColumn: binds foreign key from parent class airport to child class
     * --> name = "name of the foreign key"
     * Parent: Airport
     * Child: Flight
     *
     *
     */
    @ManyToOne
    @JoinColumn(name = "departure_airport_airport_id")
    private Airport departureAirport;

    /**
     * Parent: Airport
     * Child: Flight
     */
    @ManyToOne
    @JoinColumn(name = "destination_airport_airport_id")
    private Airport destinationAirport;

    /**
     * Parent: Aircraft
     * Child: Flight
     */
    @ManyToOne
    @JoinColumn(name = "aircraft_aircraft_id")
    Aircraft aircraft;

    /**
     * parent class: flight
     * child class: passenger
     */
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Passenger> passengers = new ArrayList<>();


}