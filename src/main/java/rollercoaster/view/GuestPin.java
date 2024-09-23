package rollercoaster.view;

import rollercoaster.model.Guest;

class GuestPin extends PeoplePin {

	private Guest guest;

	public GuestPin(int offsetX, int posX, int offsetY, int posY, Guest guest) {
		super(offsetX, posX, offsetY, posY, guest, "GUEST_", 2);
		this.guest = guest;
	}

	public Guest getGuest() {
		return guest;
	}
}