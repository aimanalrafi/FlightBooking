package de.thb.Flight.Controller;

import de.thb.Flight.Entity.Airport;
import de.thb.Flight.Entity.Flight;
import de.thb.Flight.Entity.Passenger;
import de.thb.Flight.Service.AircraftService;
import de.thb.Flight.Service.AirportService;
import de.thb.Flight.Service.FlightService;
import de.thb.Flight.Service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class FlightController {

    @Autowired
    AirportService airportService;
    @Autowired
    AircraftService aircraftService;
    @Autowired
    FlightService flightService;
    @Autowired
    PassengerService passengerService;

    @GetMapping("/flight_form")
    public String showNewFlightPage(Model model) {
        model.addAttribute("flight", new Flight());
        model.addAttribute("aircrafts", aircraftService.getAllAircrafts());
        model.addAttribute("airports", airportService.getAllAirports());
        return "newFlight";
    }

    @PostMapping("/flights")
    public String saveFlight(@Valid @ModelAttribute("flight") Flight flight, BindingResult bindingResult,
                             @RequestParam("departureAirport") int departureAirport,
                             @RequestParam("destinationAirport") int destinationAirport,
                             @RequestParam("aircraft") long aircraftId,
                             @RequestParam("arrivalTime") String arrivalTime,
                             @RequestParam("departureTime") String departureTime,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("flight", new Flight());
            model.addAttribute("aircrafts", aircraftService.getAllAircrafts());
            model.addAttribute("airports", airportService.getAllAirports());
            return "newFlight";
        }
        if (departureAirport == destinationAirport) {
            model.addAttribute("sameAirportError", "Departure and destination airport can't be same");
            model.addAttribute("flight", new Flight());
            model.addAttribute("aircrafts", aircraftService.getAllAircrafts());
            model.addAttribute("airports", airportService.getAllAirports());
            return "newFlight";
        }

        flight.setAircraft(aircraftService.getAircraftById(aircraftId));
        flight.setDepartureAirport(airportService.getAirportById(departureAirport));
        flight.setDestinationAirport(airportService.getAirportById(destinationAirport));
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flightService.saveFlight(flight);

        model.addAttribute("flights", flightService.getAllFlightsPaged(0));
        model.addAttribute("currentPage", 0);
        return "flights";
    }

    @GetMapping("/flight/delete")
    public String deleteFlight(@PathParam("flightId") long flightId, Model model) {
        flightService.deleteFlightById(flightId);
        model.addAttribute("flights", flightService.getAllFlightsPaged(0));
        model.addAttribute("currentPage", 0);
        return "flights";
    }

    @GetMapping("/flights")
    public String showFlightsList(@RequestParam(defaultValue = "0") int pageNo, Model model) {
        model.addAttribute("flights", flightService.getAllFlightsPaged(pageNo));
        model.addAttribute("currentPage", pageNo);
        return "flights";
    }

    @GetMapping("/flight/search")
    public String showSearchFlightPage(Model model) {
        model.addAttribute("airports", airportService.getAllAirports());
        model.addAttribute("flights", null);
        return "searchFlight";
    }

    @PostMapping("/flight/search")
    public String searchFlight(@RequestParam("departureAirport") int departureAirport,
                               @RequestParam("destinationAirport") int destinationAirport,
                               @RequestParam("departureTime") String departureTime,
                               Model model) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate deptTime = LocalDate.parse(departureTime, dtf);
        Airport depAirport = airportService.getAirportById(departureAirport);
        Airport destAirport = airportService.getAirportById(destinationAirport);

        if (departureAirport == destinationAirport) {
            model.addAttribute("AirportError", "Departure and destination airport cant be same!");
            model.addAttribute("airports", airportService.getAllAirports());
            return "searchFlight";
        }
        List<Flight> flights = flightService.getAllFlightsByAirportAndDepartureTime(depAirport, destAirport, deptTime);
        if (flights.isEmpty()) {
            model.addAttribute("notFound", "No Record Found!");
        } else {
            model.addAttribute("flights", flights);
        }

        model.addAttribute("airports", airportService.getAllAirports());
        return "searchFlight";
    }

    @GetMapping("/flight/book")
    public String showSearchBookPage(Model model) {
        model.addAttribute("airports", airportService.getAllAirports());
        model.addAttribute("flights", null);
        return "bookFlight";
    }

    @PostMapping("/flight/book")
    public String searchFlightToBook(@RequestParam("departureAirport") int departureAirport,
                                     @RequestParam("destinationAirport") int destinationAirport,
                                     @RequestParam("departureTime") String departureTime,
                                     Model model) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate deptTime = LocalDate.parse(departureTime, dtf);
        Airport depAirport = airportService.getAirportById(departureAirport);
        Airport destAirport = airportService.getAirportById(destinationAirport);

        if (departureAirport == destinationAirport) {
            model.addAttribute("AirportError", "Departure and destination airport cant be same!");
            model.addAttribute("airports", airportService.getAllAirports());
            return "bookFlight";
        }
        List<Flight> flights = flightService.getAllFlightsByAirportAndDepartureTime(depAirport, destAirport, deptTime);
        if (flights.isEmpty()) {
            model.addAttribute("notFound", "No Record Found!");
        } else {
            model.addAttribute("flights", flights);
        }
        model.addAttribute("airports", airportService.getAllAirports());
        return "bookFlight";
    }

    @GetMapping("/flight/book/new")
    public String showCustomerInfoPage(@RequestParam long flightId, Model model) {
        model.addAttribute("flightId", flightId);
        model.addAttribute("passenger", new Passenger());
        return "newPassenger";
    }

    @PostMapping("/flight/book/new")
    public String bookFlight(@Valid @ModelAttribute("passenger") Passenger passenger, BindingResult bindingResult, @RequestParam("flightId") long flightId, Model model) {
        Flight flight = flightService.getFlightById(flightId);
        Passenger passenger1 = passenger;
        passenger1.setFlight(flight);
        passengerService.savePassenger(passenger1);
        model.addAttribute("passenger", passenger1);
        return "confirmationPage";
    }

    @GetMapping("/flight/book/verify")
    public String showVerifyBookingPage() {
        return "verifyBooking";
    }

    @PostMapping("/flight/book/verify")
    public String showVerifyBookingPageResult(@RequestParam("flightId") long flightId,
                                              @RequestParam("passengerId") long passengerId,
                                              Model model) {

        Flight flight = flightService.getFlightById(flightId);
        if (flight != null) {
            model.addAttribute("flight", flight);
            List<Passenger> passengers = flight.getPassengers();
            Passenger passenger = null;
            for (Passenger p : passengers) {
                if (p.getPassengerId() == passengerId) {
                    passenger = passengerService.getPassengerById(passengerId);
                    model.addAttribute("passenger", passenger);
                }
            }
            if (passenger != null) {
                return "verifyBooking";
            } else {
                model.addAttribute("notFound", "Not Found");
                return "verifyBooking";
            }
        } else {
            model.addAttribute("notFound", "Not Found");
            return "verifyBooking";
        }

    }

    @PostMapping("/flight/book/cancel")
    public String cancelTicket(@RequestParam("passengerId") long passengerId, Model model) {
        passengerService.deletePassengerById(passengerId);
        model.addAttribute("flights", flightService.getAllFlightsPaged(0));
        model.addAttribute("currentPage", 0);
        return "searchFlight";
    }
}
