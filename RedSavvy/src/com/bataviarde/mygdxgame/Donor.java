package com.bataviarde.mygdxgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.bataviarde.mygdxgame.Nurse.bloodtype;

public class Donor {

	public int PositionX, PositionY, speed;
	public Direction Direction;

	public Direction direction;
	public DonorLocationState state;

	public int donationChance;
	public Boolean donationWill;

	public BloodPack bloodpack;
	public Bench bench;
	public Bed bed;

	public Boolean isPickable = false;
	public Boolean isPicking = false;
	public Boolean isFinish = false;

	public TextureRegion currTimerFrames;

	public Rectangle recta;
	public int id;
	public bloodtype type;

	public Boolean active;
	public RESIPIEN resipienstate = RESIPIEN.WAIT;

	float selfTimer, injectTimer = 0f;
	int SpritePointer;

	Button Ap, Am, Bp, Bm, Op, Om, ABp, ABm;
	List<Button> BloodTypes;

	public Donor(int x, int y, Direction d, int s) {
		PositionX = x;
		PositionY = y;
		Direction = Direction.RIGHT;
		speed = s;

		state = DonorLocationState.SIDEWALK;
		donationChance = 150;
		donationWill = true;

		bloodpack = new BloodPack();
		currTimerFrames = new TextureRegion();

		active = false;
	}

	public Donor(Rectangle r, int i, bloodtype b) {
		recta = r;
		id = i;
		type = b;

		active = false;

		Op = new Button(new Rectangle(r.x - 36, r.y - 36 - 10, 36, 36), bloodtype.Op);
		Om = new Button(new Rectangle(r.x + r.width, r.y - 36 - 10, 36, 36), bloodtype.Om);
		ABp = new Button(new Rectangle(r.x - 36 - 10, r.y, 36, 36), bloodtype.ABp);
		ABm = new Button(new Rectangle(r.x + r.width + 10, r.y, 36, 36), bloodtype.ABm);
		Bp = new Button(new Rectangle(r.x - 36 - 10, r.y + 36 + 10, 36, 36), bloodtype.Bp);
		Bm = new Button(new Rectangle(r.x + r.width + 10, r.y + 36 + 10, 36, 36), bloodtype.Bm);
		Ap = new Button(new Rectangle(r.x - 36, r.y + 2 * (36 + 10), 36, 36), bloodtype.Ap);
		Am = new Button(new Rectangle(r.x + r.width, r.y + 2 * (36 + 10), 36, 36), bloodtype.Am);

		BloodTypes = new ArrayList<Button>();
		BloodTypes.add(Ap);
		BloodTypes.add(Bp);
		BloodTypes.add(Op);
		BloodTypes.add(ABp);
		BloodTypes.add(Am);
		BloodTypes.add(Bm);
		BloodTypes.add(Om);
		BloodTypes.add(ABm);
		
	}

	public void Update() {

		switch (state) {
		case SIDEWALK:
			BehaveWalkingOnSidewalk();
			break;

		case WALKTOBENCH:
			BehaveWalkingToBench();
			break;

		case BENCH:
			BehaveSittingDuckOnBench();
			break;

		case BED:
			BehaveSleepInBed();
			break;

		case WALKOUT:
			BehaveWalkOut();
			break;
		}
	}

	private void BehaveWalkingToBench() {
		if (PositionY < 140 && state == DonorLocationState.WALKTOBENCH) {
			if (PositionY - bench.rectangle.y < 0) {
				PositionY += speed;
			}
		} else {
			if (PositionX - bench.rectangle.x < 0) {
				PositionX += speed;
			}
			if (PositionY - bench.rectangle.y < 0) {
				PositionY += speed;
			}

			if (PositionX - bench.rectangle.x >= 0
					&& PositionY - bench.rectangle.y >= 0) {
				state = DonorLocationState.BENCH;
			}
		}

	}

	private void BehaveWalkingOnSidewalk() {
		if (PositionX > -60
				&& Direction == com.bataviarde.mygdxgame.Direction.LEFT)
			PositionX -= speed;
		if (PositionX <= -60
				&& Direction == com.bataviarde.mygdxgame.Direction.LEFT)
			Direction = com.bataviarde.mygdxgame.Direction.RIGHT;
		if (PositionX < MyGdxGame.w + 20
				&& Direction == com.bataviarde.mygdxgame.Direction.RIGHT)
			PositionX += speed;
		if (PositionX >= MyGdxGame.w + 20
				&& Direction == com.bataviarde.mygdxgame.Direction.RIGHT) {
			donationWill = true;
			Direction = com.bataviarde.mygdxgame.Direction.LEFT;
		}

	}

	private void BehaveSittingDuckOnBench() {
		direction = com.bataviarde.mygdxgame.Direction.LEFT;
		isPickable = true;

		Rectangle rectangle = new Rectangle(PositionX, PositionY, 54, 64);
		// Rectangle re = new Rectangle(MyGdxGame.SingleTap.x,
		// MyGdxGame.SingleTap.y, 2, 2);

		if (MyGdxGame.isTouch
				&& rectangle.contains(MyGdxGame.SingleTap.x,
						Gdx.graphics.getHeight() - MyGdxGame.SingleTap.y)
				&& MyGdxGame.CurrentDraggedDonor == null) {
			isPicking = true;
			MyGdxGame.CurrentDraggedDonor = this;
		}

		if (isPicking && !MyGdxGame.isTouch) {
			isPicking = false;
		}

		if (!isPicking && !MyGdxGame.isTouch) {
			if (PositionX - bench.rectangle.x < 0) {
				PositionX += 4 * speed;
			}
			if (PositionY - bench.rectangle.y < 0) {
				PositionY += 4 * speed;
			}
			if (PositionY - bench.rectangle.y >= 0) {
				PositionY -= 4 * speed;
			}
		}

	}

	private void BehaveSleepInBed() {

		if (injectTimer >= 10f) {
			state = DonorLocationState.WALKOUT;
			bed.isOccupied = false;
			injectTimer = 0;
		}

	}

	private void BehaveWalkOut() {
		if (PositionX - Gdx.graphics.getWidth() / 2 > 5) {
			PositionX -= 2 * speed;
		} else if (PositionX - Gdx.graphics.getWidth() / 2 < -5) {
			PositionX += 2 * speed;
		} else if (PositionX - Gdx.graphics.getWidth() / 2 < 5
				|| PositionX - Gdx.graphics.getWidth() / 2 > -5) {
			if (PositionY > 0) {
				PositionY -= 2 * speed;
			}
		}

		// Bloodpack Store
		if (bloodpack.rectangle.x < 760) {
			bloodpack.rectangle.x += 2 * speed;
		}
		if (bloodpack.rectangle.y < 430) {
			bloodpack.rectangle.y += 1 * speed;
		}

		// TO SIDEWALK
		if (PositionY <= 0) {
			PositionY = 0;
			state = DonorLocationState.SIDEWALK;
			MyGdxGame.TodayScore++;
		}
	}

	public Boolean shouldIDonate() {
		Boolean donate = false;
		Random r = new Random();
		if (r.nextInt(1000) > donationChance) {
			state = DonorLocationState.WALKTOBENCH;
			donate = true;
		}
		donationWill = false;

		return donate;
	}

	public enum DonorLocationState {
		SIDEWALK, WALKTOBENCH, BENCH, BED, WALKOUT
	}

	public void cancelDonation() {
		state = DonorLocationState.SIDEWALK;
	}
	
	public enum RESIPIEN{
		WAIT, TRUE, FALSE
	}
}
