package me.jibajo.rider_service.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    

    //    @Bean
//    public CommandLineRunner seedDatabase(RiderRepository riderRepository) {
//        return new CommandLineRunner() {
//            @Override
//            public void run(String... args) throws Exception {
//                if (riderRepository.count() == 0) {
//
//                    Rider rider1 = new Rider("rider1@example.com", "9876543210", true, true, false, LocalDateTime.now());
//                    Rider rider2 = new Rider("rider2@example.com", "9876543211", true, false, false, LocalDateTime.now());
//                    Rider rider3 = new Rider("rider3@example.com", "9876543212", false, true, true, LocalDateTime.now());
//
//                    // Save to repository
//                    riderRepository.saveAll(List.of(rider1, rider2, rider3));
//
//                    System.out.println("Database seeded with riders.");
//                } else {
//                    System.out.println("Database already seeded. Skipping initialization.");
//                }
//            }
//        };
//    }
}
