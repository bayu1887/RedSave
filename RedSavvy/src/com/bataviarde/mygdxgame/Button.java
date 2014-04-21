package com.bataviarde.mygdxgame;

import com.badlogic.gdx.math.Rectangle;
import com.bataviarde.mygdxgame.Nurse.bloodtype;

public class Button {
	public Rectangle recta;
	public bloodtype type;
	
	public Button(Rectangle r, bloodtype b){
		recta = r;
		type = b;
	}
}
