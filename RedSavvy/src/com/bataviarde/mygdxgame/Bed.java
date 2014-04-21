package com.bataviarde.mygdxgame;

import com.badlogic.gdx.math.Rectangle;

public class Bed {
    public Rectangle rectangle;

    public Boolean isOccupied = false;
    public int id;

    public Bed(Rectangle r, int i)
    { 
    	rectangle = r;
    	id = i;
    	id = i * r;
    }

    public void Update()
    { 
        
    }        

    public void Draw()
    {
//        spriteBatch.Draw(texture, rectangle, color);
    }
}
