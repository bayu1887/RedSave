package com.bataviarde.mygdxgame;

import com.badlogic.gdx.math.Rectangle;

public class Bench {

	public Rectangle rectangle;

	public Boolean isOccupied = false;
	public int id;

	public Bench(Rectangle r, int i) {
		rectangle = r;
		id = i;
	}

	public void Draw() {
		// spriteBatch.Draw(texture, rectangle, color);
	}
}
