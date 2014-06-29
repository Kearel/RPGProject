package rpgProject.graphics;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Glyph;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;

public class ColoredText implements Drawable {
	private Font font;
	private Sprite fontSprite;
	private char string[];
	private Vector2f position;
	private int characterSize;
	private boolean findColor = false;

	public ColoredText(Font f, String s) {
		font = f;
		fontSprite = new Sprite(f.getTexture(12));
		string = s.toCharArray();
		position = new Vector2f(0, 0);
		fontSprite.setColor(Color.BLACK);
		characterSize = 12;
	}

	public ColoredText(Font f, String s, Vector2f p) {
		font = f;
		string = s.toCharArray();
		position = p;
		characterSize = 12;
		fontSprite.setColor(Color.BLACK);
	}

	public void setSize(int size) {
		fontSprite = new Sprite(font.getTexture(size));
		characterSize = size;
	}

	public void setPosition(Vector2f pos) {
		position = pos;
	}

	public void setPosition(float x, float y) {
		position = new Vector2f(x, y);
	}

	public void setString(String s) {
		string = s.toCharArray();
	}

	public int getTotalWidth() {
		int advanced = 0;
		for (char a : string) {
			Glyph glyph = font.getGlyph(a, characterSize, false);
			advanced += glyph.advance;
		}

		return advanced;
	}

	@Override
	public void draw(RenderTarget arg0, RenderStates arg1) {
		int pixelWidth = 0;
		fontSprite.setColor(Color.BLACK);
		for (char a : string) {
			if (a != '#') {
				if (findColor) {
					switch (a) {
					case 'r':
						fontSprite.setColor(Color.RED);
						break;
					case 'b':
						fontSprite.setColor(Color.BLACK);
						break;
					case 'B':
						fontSprite.setColor(Color.BLUE);
						break;
					case 'g':
						fontSprite.setColor(Color.GREEN);
						break;
					case 'G':
						fontSprite.setColor(new Color(105, 105, 105));
						break;
					case 'w':
						fontSprite.setColor(Color.WHITE);
						break;
					case 'm':
						fontSprite.setColor(Color.MAGENTA);
						break;
					case 'y':
						fontSprite.setColor(Color.YELLOW);
						break;
					default:
						fontSprite.setColor(Color.BLACK);
						break;
					}
					findColor = false;
				} else {
					Glyph glyph = font.getGlyph(a, characterSize, false);
					fontSprite.setTextureRect(glyph.textureRect);
					fontSprite.setPosition(new Vector2f(position.x + pixelWidth
							+ glyph.bounds.left, position.y + glyph.bounds.top
							+ characterSize));
					fontSprite.draw(arg0, arg1);
					pixelWidth += glyph.advance;

				}
			} else
				findColor = true;
		}
	}

}
