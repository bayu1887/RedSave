package com.bataviarde.mygdxgame;

import com.badlogic.gdx.math.Rectangle;

public class Nurse {
	public Rectangle rectangle;
	public int id;
	public bloodtype type;
	public Boolean active;

	public Nurse(Rectangle r, int i, bloodtype b) {
active = false;
		rectangle = r;
		id = i;
		type = b;
	}

	public void Update() {

	}

	public void Draw() {
		// spriteBatch.Draw(texture, rectangle, color);
	}
	
	public enum bloodtype{
		Ap, Bp, Op, ABp, Am, Bm, Om, ABm,
	}
}
