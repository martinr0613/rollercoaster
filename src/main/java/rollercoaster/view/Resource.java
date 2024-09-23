package rollercoaster.view;

public enum Resource {

	// games
	DODGEM("dodgem", "dodgem2.png"), //
	OBSERVATION_WHEEL("observation_wheel", "observation_wheel.png"), //
	CAROUSEL_1("carousel_1", "carousel_1.png"), //
	CAROUSEL_2("carousel_2", "carousel_2.png"), //
	HAMMER("hammer", "hammer.png"), //

	// plants
	GRASS("grass", "densegrass.png"), //
	TREE_1("tree_1", "tree_1.png"), //
	TREE_2("tree_2", "tree_2.png"), //
	FLOWER("flower", "flower.png"), //

	// others
	GATE("gate", "gate.png"), //
	ELECTRICITY("electricity", "gen.png"), //

	// restaurant
	HOTDOG("hotdog", "hotdog.png"), //
	RESTAURANT("restaurant", "restaurant.png"), //

	// roads
	ROAD_0("road_0", "road_0.png"), //
	ROAD_1("road_1", "road_1.png"), //
	ROAD_2("road_2", "road_2.png"), //
	ROAD_3("road_3", "road_3.png"), //
	ROAD_4("road_4", "road_4.png"), //
	ROAD_5("road_5", "road_5.png"), //
	ROAD_6("road_6", "road_6.png"), //
	ROAD_7("road_7", "road_7.png"), //
	ROAD_8("road_8", "road_8.png"), //
	ROAD_9("road_9", "road_9.png"), //
	ROAD_10("road_10", "road_10.png"), //
	ROAD_11("road_11", "road_11.png"), //
	ROAD_12("road_12", "road_12.png"), //
	ROAD_13("road_13", "road_13.png"), //
	ROAD_14("road_14", "road_14.png"), //
	ROAD_15("road_15", "road_15.png"), //

	// people
	GUEST_1("guest_1", "guest_1.png"), //
	GUEST_2("guest_2", "guest_2.png"), //
	MAINTAINER("maintainer", "maintainer.png"), //

	// bin
	BIN("bin", "bin.png"), //

	BACKGROUND("background", "bg4.png")//
	;

	private String name;
	private String filename;

	private Resource(String name, String filename) {
		this.name = name;
		this.filename = filename;
	}

	public String getName() {
		return this.name;
	}

	public String getFilename() {
		return this.filename;
	}

}
