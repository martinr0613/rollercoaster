package rollercoaster.view;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.ImageIcon;

public class ResourceLoader {

	private static final Map<String, Image> textures = new HashMap<>();

	static {
		for (Resource r : Resource.values()) {
			ResourceLoader.loadTexture(r);
		}
	}

	private static void loadTexture(Resource resource) {
		textures.put(resource.getName(),
				new ImageIcon(Objects
						.requireNonNull(ResourceLoader.class.getClassLoader().getResource(resource.getFilename())))
						.getImage());
	}

	public static Image getTexture(Resource r) {
		return textures.get(r.getName());
	}
}
