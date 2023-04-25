package de.thb.Flight.Controller;

import de.thb.Flight.Service.AircraftService;
import de.thb.Flight.Service.AirportService;
import de.thb.Flight.Service.FlightService;
import de.thb.Flight.Service.PassengerService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@AllArgsConstructor
public class MainController {

    @GetMapping("/")
    public String showHomePage(){
        return "master-page";
    }

}