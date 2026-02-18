package com.example;

import com.example.model.Salle;
import com.example.model.User;
import com.example.service.SalleService;
import com.example.service.UserService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class App{

    public static void main(String[] args) {

        // Initialisation de la persistence unit
        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("gestion-salles");

        UserService userService = new UserService(factory);
        SalleService roomService = new SalleService(factory);

        try {
            System.out.println("\n===== USER CRUD DEMO =====");
            demoUserOperations(userService);

            System.out.println("\n===== ROOM CRUD DEMO =====");
            demoRoomOperations(roomService);

        } finally {
            factory.close();
        }
    }

    private static void demoUserOperations(UserService service) {

        System.out.println("Adding new users...");

        User user1 = new User("Jean", "Dupont", "jean.dupont@example.com");
        user1.setBirthDate(LocalDate.of(1985, 5, 15));
        user1.setPhoneNumber("+33612345678");

        User user2 = new User("Sophie", "Martin", "sophie.martin@example.com");
        user2.setBirthDate(LocalDate.of(1990, 10, 20));
        user2.setPhoneNumber("+33687654321");

        service.save(user1);
        service.save(user2);

        System.out.println("\nAll users in database:");
        service.findAll().forEach(System.out::println);

        System.out.println("\nFind user by ID:");
        Optional<User> foundUser = service.findById(1L);
        foundUser.ifPresent(System.out::println);

        System.out.println("\nFind user by email:");
        service.findByEmail("sophie.martin@example.com")
                .ifPresent(System.out::println);

        System.out.println("\nUpdating user phone number...");
        foundUser.ifPresent(u -> {
            u.setPhoneNumber("+33699887766");
            service.update(u);
            System.out.println("Updated user: " + u);
        });

        System.out.println("\nDeleting user with ID 2...");
        service.deleteById(2L);

        System.out.println("\nRemaining users:");
        service.findAll().forEach(System.out::println);
    }

    private static void demoRoomOperations(SalleService service) {

        System.out.println("Creating rooms...");

        Salle r1 = new Salle("Room A101", 30);
        r1.setDetails("Meeting room with projector");
        r1.setFloorNumber(1);

        Salle r2 = new Salle("Conference Hall B201", 150);
        r2.setDetails("Large hall for seminars");
        r2.setFloorNumber(2);

        Salle r3 = new Salle("Room C305", 10);
        r3.setDetails("Small interview room");
        r3.setFloorNumber(3);
        r3.setAvailable(false);

        service.save(r1);
        service.save(r2);
        service.save(r3);

        System.out.println("\nAll rooms:");
        service.findAll().forEach(System.out::println);

        System.out.println("\nSearch room by ID:");
        Optional<Salle> selectedRoom = service.findById(2L);
        selectedRoom.ifPresent(System.out::println);

        System.out.println("\nAvailable rooms:");
        service.findByDisponible(true).forEach(System.out::println);

        System.out.println("\nRooms with capacity >= 50:");
        service.findByCapaciteMinimum(50).forEach(System.out::println);

        System.out.println("\nUpdating room capacity...");
        selectedRoom.ifPresent(room -> {
            room.setCapacity(200);
            service.update(room);
            System.out.println("Room updated: " + room);
        });

        System.out.println("\nDeleting room with ID 3...");
        service.deleteById(3L);

        System.out.println("\nRooms after deletion:");
        service.findAll().forEach(System.out::println);
    }
}
