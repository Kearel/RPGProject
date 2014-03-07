package rpgProject;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2f;

public class RegularSprite implements Drawable {
	private Sprite sprite;
	private int spriteWidth, spriteHeight, spritesW;
	private boolean flipped;

	public RegularSprite() {
		sprite = new Sprite();
		flipped = false;
	}

	public RegularSprite(Texture t) {
		sprite = new Sprite(t);
		sprite.setOrigin(50, 90);
		spriteWidth = 100;
		spriteHeight = 100;
		spritesW = t.getSize().x / spriteWidth;
		setFrame(0);
		sprite.setScale(new Vector2f(100, 100));
		flipped = false;
	}

	public RegularSprite(Texture t, int ox, int oy, int sW, int sH) {
		sprite = new Sprite(t);
		spriteWidth = sW;
		spriteHeight = sH;
		spritesW = t.getSize().x / spriteWidth;
		sprite.setOrigin(ox, oy);
		setFrame(0);
	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		sprite.draw(target, states);
	}

	public void setFrame(int frame) {
		int w = spriteWidth * (frame % spritesW);
		int h = spriteHeight * (frame / spritesW);
		if (h >= sprite.getTexture().getSize().y)
			frame = 0;
		if (!flipped)
			sprite.setTextureRect(new IntRect(w, h, spriteWidth, spriteHeight));
		else
			sprite.setTextureRect(new IntRect(w + spriteWidth, h, -spriteWidth,
					spriteHeight));
	}

	public void setFlipped(boolean flip) {
		int f = getFrame();
		flipped = flip;
		setFrame(f);
	}
	
	public boolean getFlipped()
	{
		return flipped;
	}

	public int getFrame() {
		int f = 0;
		f += spritesW * (sprite.getTextureRect().top / spriteHeight);
		f += sprite.getTextureRect().left / spriteWidth;
		if(flipped)
		{
			f -= 1;
		}
		return f;
	}

	public void nextFrame() {
		setFrame(getFrame() + 1);
	}

	public void setPosition(Vector2f p) {
		sprite.setPosition(p);
	}
	
	public void setColor(Color c)
	{
		sprite.setColor(c);
	}
}
