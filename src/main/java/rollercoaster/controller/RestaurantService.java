package rollercoaster.controller;

import rollercoaster.model.Guest;
import rollercoaster.model.GuestState;
import rollercoaster.model.Restaurant;
import rollercoaster.model.Status;

public class RestaurantService {

	public static void startRound(Restaurant restaurant, Long currentSecond) {
		while (restaurant.getCapacity() > restaurant.getCurrentUsing().size() && restaurant.getQueue().size() > 0) {
			Guest guest = restaurant.getQueue().remove(0);
			guest.setState(GuestState.EATING);
			BuildingService.payService(restaurant, guest);
			restaurant.getCurrentUsing().add(guest);
		}
		restaurant.setStartActionAt(currentSecond);
		restaurant.setStatus(Status.IN_USE);
	}

	public static void finishRound(Restaurant restaurant, Long currentSecond) {
		restaurant.setStartActionAt(null);
		restaurant.setStatus(Status.AVAILABLE);
		for (Guest guest : restaurant.getCurrentUsing()) {
			guest.setState(GuestState.NONE);
		}
		restaurant.getCurrentUsing().clear();
	}
}
