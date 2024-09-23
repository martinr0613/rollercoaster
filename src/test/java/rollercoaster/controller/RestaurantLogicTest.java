package rollercoaster.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import rollercoaster.model.*;
import org.junit.jupiter.api.Test;

public class RestaurantLogicTest {

    @Test
    void startRound() {
        Restaurant restaurant = new Restaurant(null, 0, 1, 0, 0);
        // Guest g1 = new Guest(0,0,0,r);
        // Guest g2 = new Guest(0,0,0,r);
        Guest g1 = new Guest(0, 0, 0, restaurant);
        Guest g2 = new Guest(0, 0, 0, restaurant);

        restaurant.addToQueue(g1);
        restaurant.addToQueue(g2);

        RestaurantService.startRound(restaurant, 5L);
        assertEquals(1, restaurant.getQueue().size());
        assertEquals(GuestState.EATING, g1.getState());
        assertEquals(GuestState.ARRIVED, g2.getState());
        assertEquals(1, restaurant.getCurrentUsing().size());
        assertEquals(Status.IN_USE, restaurant.getStatus());
    }

    @Test
    void finishRound() {
        Restaurant restaurant = new Restaurant(null, 0, 2, 0, 0);
        Guest g1 = new Guest(0, 0, 0, restaurant);
        Guest g2 = new Guest(0, 0, 0, restaurant);
        g1.setState(GuestState.EATING);
        g2.setState(GuestState.EATING);
        restaurant.getCurrentUsing().add(g1);
        restaurant.getCurrentUsing().add(g2);
        restaurant.setStartActionAt(0L);
        restaurant.setStatus(Status.IN_USE);

        RestaurantService.finishRound(restaurant, 5L);

        assertEquals(null, restaurant.getStartActionAt());
        assertEquals(Status.AVAILABLE, restaurant.getStatus());
        assertEquals(GuestState.NONE, g1.getState());
        assertEquals(GuestState.NONE, g2.getState());
        assertEquals(0, restaurant.getCurrentUsing().size());

    }
}