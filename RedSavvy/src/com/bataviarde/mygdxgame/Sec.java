package com.bataviarde.mygdxgame;

import com.badlogic.gdx.math.Rectangle;

public class Sec {
	public Rectangle recta;
	public Direction direction;
	public int speed;
	
	public Sec(Rectangle r, Direction d, int s){
		recta = r;
		direction = d;
		speed = s;
	}
}
