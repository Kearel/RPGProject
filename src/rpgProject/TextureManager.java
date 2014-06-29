package rpgProject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.Texture;

import rpgProject.graphics.RegularSprite;

public class TextureManager {
	private HashMap<String, Texture> textures;

	public TextureManager() {
		textures = new HashMap<String, Texture>();
	}

	public Texture loadTexture(String path) {
		if (!textures.containsKey(path)) {
			Texture t = new Texture();
			try {
				InputStream is = this.getClass().getResourceAsStream(
						"/rpgProject/resources/" + path);
				t.loadFromStream(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			textures.put(path, t);
			return t;
		} else
			return textures.get(path);

	}

	public int loadedTextures() {
		return textures.size();
	}

	public void clearTextures() {
		textures.clear();
	}

	/**
	 * Creates a RegularSprite by detecting the box around the sprites
	 * 
	 * @return a new RegularSprite
	 */
	public RegularSprite createSprite(Texture t) {
		Image image = t.copyToImage();
		int width = 0;
		int height = 0;

		// we find the color of the box
		Color color = image.getPixel(0, 0);

		// we then calculate the width and the height
		while (!image.getPixel(1 + width, 1).equals(color)) {
			width++;
		}

		while (!image.getPixel(1, 1 + height).equals(color)) {
			height++;
		}
		// calculate the origin x
		int originX = (int)Math.floor(width/2);
		
		return new RegularSprite(t,originX,height,width,height);
	}
}
