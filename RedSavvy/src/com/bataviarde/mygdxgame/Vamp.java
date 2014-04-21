package com.bataviarde.mygdxgame;

import java.util.Random;

import com.badlogic.gdx.math.Rectangle;
import com.bataviarde.mygdxgame.Donor.DonorLocationState;

public class Vamp {
	public Rectangle recta;
	public Direction direction;
	public int speed;
	public Boolean drinkWill = false;

	public Vault vault;
	public VampLocation Location;
	
	public int hp;

	public Vamp(Rectangle r, Direction d, int s, int h) {
		recta = r;
		direction = d;
		speed = s;		
		hp = h;

		drinkWill = true;
		Location = VampLocation.FREEFLY;
	}

	public void update() {
		switch (Location) {
		case FREEFLY:
			BehaveFreeFlying();
			break;
		case FLYTOVAULT:
			BehaveFlyToVault();
			break;

		case BRINGPACK:
			BehaveEscape();
			break;
			
		case DIE:
			BehaveDying();
			break;
		}

	}

	private void BehaveDying() {
		if(recta.y > 10){
			recta.y -= 2 * speed;
		}
	}

	private void BehaveEscape() {
		if (direction == Direction.LEFT) {
			recta.x += speed / 2;
		} 
		if (direction == Direction.RIGHT) {
			recta.x -= speed / 2;
		}

	}

	private void BehaveFlyToVault() {

		if (recta.x - vault.recta.x < 0) {
			recta.x += speed * 2;
		}
		if (recta.x - vault.recta.x > 0) {
			recta.x -= speed * 2;
		}		
		if (recta.y - vault.recta.y > 0) {
			recta.y -= speed;
		}
		

		if (vault.recta.y > recta.y || recta.y < MyGdxGame.h/2) {
			Location = VampLocation.BRINGPACK;
		}

	}

	private void BehaveFreeFlying() {
		if (recta.x > -60
				&& direction == com.bataviarde.mygdxgame.Direction.LEFT)
			recta.x -= speed;
		if (recta.x <= -60
				&& direction == com.bataviarde.mygdxgame.Direction.LEFT)
			direction = com.bataviarde.mygdxgame.Direction.RIGHT;
		if (recta.x < MyGdxGame.w + 20
				&& direction == com.bataviarde.mygdxgame.Direction.RIGHT)
			recta.x += speed;
		if (recta.x >= MyGdxGame.w + 20
				&& direction == com.bataviarde.mygdxgame.Direction.RIGHT) {
			direction = com.bataviarde.mygdxgame.Direction.LEFT;
			drinkWill = true;
		}

	}

	public Boolean shouldIDrink() {
		Boolean donate = false;
		Random r = new Random();
		if (r.nextInt(1000) > 300) {
			// state = DonorLocationState.WALKTOBENCH;
			donate = true;
		}
		drinkWill = false;

		return donate;
	}

	public enum VampLocation {
		FREEFLY, FLYTOVAULT, BRINGPACK, DIE, CAPTURE
	}
}
