package me.jibajo.captain_service.config;

import me.jibajo.captain_service.entities.Captain;
import me.jibajo.captain_service.entities.Vehicle;
import me.jibajo.captain_service.repositories.CaptainRepository;
import me.jibajo.captain_service.repositories.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedDatabase(CaptainRepository captainRepository, VehicleRepository vehicleRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                if (captainRepository.count() == 0) {
                    // Creating Vehicles
                    Vehicle vehicle1 = new Vehicle("Honda City", "UP16AB1234", "http://example.com/reg1");
                    Vehicle vehicle2 = new Vehicle("Maruti Swift", "DL8C5678", "http://example.com/reg2");
                    Vehicle vehicle3 = new Vehicle("Hyundai i20", "KA05M4321", "http://example.com/reg3");

                    // Saving Vehicles
                    vehicle1 = vehicleRepository.save(vehicle1);
                    vehicle2 = vehicleRepository.save(vehicle2);
                    vehicle3 = vehicleRepository.save(vehicle3);

                    // Creating Captains
                    Captain captain1 = new Captain("captain1@example.com", "9876543210", "password123", vehicle1,
                            "http://example.com/dl1", true, true, false, false, LocalDateTime.now());
                    Captain captain2 = new Captain("captain2@example.com", "9876543220", "password123", vehicle2,
                            "http://example.com/dl2", true, true, false, false, LocalDateTime.now());
                    Captain captain3 = new Captain("captain3@example.com", "9876543230", "password123", vehicle3,
                            "http://example.com/dl3", true, true, false, false, LocalDateTime.now());

                    // Saving Captains
                    captainRepository.save(captain1);
                    captainRepository.save(captain2);
                    captainRepository.save(captain3);

                    System.out.println("Database seeded with captains and vehicles.");
                } else {
                    System.out.println("Database already seeded. Skipping initialization.");
                }
            }
        };
    }
}
