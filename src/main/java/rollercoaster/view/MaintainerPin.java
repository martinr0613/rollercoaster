package rollercoaster.view;

import rollercoaster.model.Maintainer;

class MaintainerPin extends PeoplePin {

	private Maintainer maintainer;

	public MaintainerPin(int offsetX, int posX, int offsetY, int posY, Maintainer maintainer) {
		super(offsetX, posX, offsetY, posY, maintainer, "MAINTAINER", 1);
		this.maintainer = maintainer;
	}

	public Maintainer getMaintainer() {
		return maintainer;
	}
}