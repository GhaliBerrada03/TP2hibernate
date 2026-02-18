package com.example.service;

import com.example.model.Salle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class SalleServiceTest {

    private EntityManagerFactory factory;
    private SalleService roomService;

    @Before
    public void init() {
        factory = Persistence.createEntityManagerFactory("gestion-salles");
        roomService = new SalleService(factory);
    }

    @After
    public void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }

    @Test
    public void shouldPerformBasicCrudFlow() {

        // Insert new room
        Salle room = new Salle("Room Test", 25);
        room.setDetails("Temporary test room");
        room.setFloorNumber(2);

        Salle persisted = roomService.save(room);
        assertNotNull(persisted.getSalleId());

        // Retrieve by id
        Optional<Salle> retrieved = roomService.findById(persisted.getSalleId());
        assertTrue(retrieved.isPresent());
        assertEquals("Room Test", retrieved.get().getNomSalle());

        // Modify capacity
        Salle updatedRoom = retrieved.get();
        updatedRoom.setCapacity(40);
        roomService.update(updatedRoom);

        Optional<Salle> afterUpdate = roomService.findById(persisted.getSalleId());
        assertTrue(afterUpdate.isPresent());
        assertEquals(Integer.valueOf(40), afterUpdate.get().getCapacity());

        // Remove entity
        roomService.delete(afterUpdate.get());
        Optional<Salle> afterDelete = roomService.findById(persisted.getSalleId());
        assertFalse(afterDelete.isPresent());
    }

    @Test
    public void shouldFilterRoomsByAvailability() {

        Salle available = new Salle("Free Room", 15);
        available.setAvailable(true);

        Salle busy = new Salle("Busy Room", 30);
        busy.setAvailable(false);

        roomService.save(available);
        roomService.save(busy);

        List<Salle> freeRooms = roomService.findByDisponible(true);
        boolean containsFree = freeRooms.stream()
                .anyMatch(r -> r.getNomSalle().equals("Free Room"));

        boolean containsBusy = freeRooms.stream()
                .anyMatch(r -> r.getNomSalle().equals("Busy Room"));

        assertTrue(containsFree);
        assertFalse(containsBusy);

        roomService.delete(available);
        roomService.delete(busy);
    }

    @Test
    public void shouldReturnRoomsAboveGivenCapacity() {

        Salle small = new Salle("Compact", 10);
        Salle medium = new Salle("Standard", 60);
        Salle large = new Salle("Premium", 120);

        roomService.save(small);
        roomService.save(medium);
        roomService.save(large);

        List<Salle> min60 = roomService.findByCapaciteMinimum(60);
        long count60 = min60.stream().count();
        assertEquals(2, count60);

        List<Salle> min100 = roomService.findByCapaciteMinimum(100);
        long count100 = min100.stream().count();
        assertEquals(1, count100);

        roomService.delete(small);
        roomService.delete(medium);
        roomService.delete(large);
    }
}
