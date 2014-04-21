package com.bataviarde.mygdxgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.bataviarde.mygdxgame.Donor.DonorLocationState;
import com.bataviarde.mygdxgame.Donor.RESIPIEN;
import com.bataviarde.mygdxgame.Nurse.bloodtype;
import com.bataviarde.mygdxgame.Vamp.VampLocation;

public class MyGdxGame implements ApplicationListener, InputProcessor {
	static public Rectangle SingleTap = new Rectangle(0, 0, 1, 1);
	static public Boolean isTouch = false;
	static public int w = 0, h = 0, LEVEL1SCORE = 4, level2scoreTrue = 0, level2scoreFalse = 0;
	static public SCREEN_STATE SCREENSTATE = SCREEN_STATE.SPLASH;
	static public PAUSE_STATE PAUSESTATE = PAUSE_STATE.PLAY;
	static public int Money = 0, Blood = 0;

	static public Boolean isWin = false;
	
	

	// SCREEN
	private Texture mm_exit, mm_option, mm_pedia, mm_credit, mm_fb, mm_tw,
			mm_mainmenu, scr_option, scr_pedia, scr_credit, scr_fb, scr_tw,
			scr_pause, scr_confirm, scr_splash, scr_mainmenu, scr_tutorial, scr_blank,
			backicon, scorescr, board;

	// / START BUTTON
	private Animation start;
	private Texture startTexture;
	private TextureRegion[] startFrames;
	private TextureRegion currStartFrames;

	// ETC

	public static Donor CurrentDraggedDonor;
	public static Rectangle DonationArea = new Rectangle();
	public static int TodayScore = 0, TotalScore = 0;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	BitmapFont font;

	Rectangle next, prev, pause, sweep, back;
	Texture sweepvamp;
	Sec sec01;

	// / debug area
	String flinger = "fling";
	FlingState flingState = FlingState.Idle;

	Scene SceneState = Scene.DONORCENTER;

	// MAIN MENU

	// BACKGROUND
	// / BG
	private Texture bg01Texture, bg02Texture, bg03Texture, nextt, prevt,
			pauset;
	private Texture bg01doorTexture, bg02doorTexture, bg03doorTexture;

	// / BG Point
	private int bg01x = 0, bg02x = 800, bg03x = 1600;

	// DONOR CENTER

	// / ENTITIES
	ArrayList<Donor> Donors;

	// / DONOR
	private Animation donorWalk, donorWalk2;
	private Texture donorWalkTexture, donorWalkTexture2;
	private TextureRegion[] donorWalkFrames, donorWalkFrames2;
	private TextureRegion currDonorWalkFrames, currDonorWalkFrames2;

	// / NURSE
	private Animation nurseWalk;
	private Texture nurseWalkTexture;
	private TextureRegion[] nurseWalkFrames;
	private TextureRegion currNurseWalkFrames;

	// / INTERIOR DONOR-CENTER
	private List<Bench> Benchs;
	private List<Bed> BedsDNC;

	private Texture benchTexture, bedTexture, bloodpackTexture, lockTexture;

	private Animation timerAnimation;
	private Texture timerTexture;
	private TextureRegion[] timerFrames;
	// private TextureRegion currTimerFrames;

	// EMERGENCY
	private Texture ambulanceTexture;
	// / INTERIOR EMERGENCY
	private List<Bed> BedsEM;
	private List<Nurse> Nurses;
	private List<Donor> Donors2;

	Texture am, ap, op, om, abm, abp, bm, bp, donorSleep, bloodtypeTexture,
			x_happy, x_cry, x_vamplol, x_vampdie;
	TextureRegion[] bloodtypesReg;

	private Animation donorSleepB;
	private Texture donorSleepBTexture;
	private TextureRegion[] donorSleepBFrames;
	private TextureRegion currDonorSleepBFrames;

	// BLOODVAULT

	float stateTime = 0f;
	Texture vaultTexture;

	private Animation vamp;
	private Texture vampTexture;
	private TextureRegion[] vampFrames;
	private TextureRegion currVampFrames;

	private Animation sec;
	private Texture secTexture;
	private TextureRegion[] secFrames;
	private TextureRegion currSecFrames;

	public List<Vault> vaults;
	public List<Vamp> vamps;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);

		// font = new
		// BitmapFont(Gdx.files.internal("data/KOMIKAX.ttf"),Gdx.files.internal("data/KOMIKAX.png"),
		// false);
		font = new BitmapFont();
		scorescr = new Texture(Gdx.files.internal("data/prop/scorescr.png"));

		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		batch = new SpriteBatch();

		backicon = new Texture(Gdx.files.internal("data/prop/back.png"));
		back = new Rectangle(20, h - 20 - 48, 48, 48);

		// SCREEN
		createScreens();

		// DONOR CENTER
		createBGprop();

		initDonorCenterEntities();

		// EMERGENCY

		initEmergencyEntities();

		// BLOODBANK

		initBloodBankEntities();

		// SPRITE & SODA
		createDonorWalkAnimation();
		createNurseWalkAnimation();
		createTimerAnimation();

		// HUD
		next = new Rectangle(Gdx.graphics.getWidth() - 96 - 5, 5, 96, 32);
		prev = new Rectangle(5, 5, 96, 32);
		pause = new Rectangle(10, h - 80 - 10, 72, 72);

		board = new Texture(Gdx.files.internal("data/prop/board.png"));
	}

	private void initBloodBankEntities() {
		vaultTexture = new Texture(
				Gdx								.files.internal("data/prop/propvault.png"));

		sweepvamp = new Texture(Gdx.files.internal("data/prop/secu.png"));
		sweep = new Rectangle(w - 80, 100, 48, 48);

		// vamp
		vampTexture = new Texture(Gdx.files.internal("data/karakter/vamp.png"));
		vampTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion[][] tmp = TextureRegion.split(vampTexture,
				vampTexture.getWidth() / 2, vampTexture.getHeight() / 1);
		vampFrames = new TextureRegion[2];
		int index = 0;
		for (int i = 0; i < 2; i++) {
			vampFrames[index++] = tmp[0][i];
		}
		vamp = new Animation(0.1f, vampFrames);

		// sec
		secTexture = new Texture(
				Gdx.files.internal("data/karakter/sec_run.png"));
		secTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tmp = TextureRegion.split(secTexture, secTexture.getWidth() / 2,
				secTexture.getHeight() / 1);
		secFrames = new TextureRegion[2];
		index = 0;
		for (int i = 0; i < 2; i++) {
			secFrames[index++] = tmp[0][i];
		}
		sec = new Animation(0.25f, secFrames);

		sec01 = new Sec(new Rectangle(-200, 10, secTexture.getWidth() / 2,
				secTexture.getHeight()), Direction.LEFT, 4);

		vamps = new ArrayList<Vamp>();
		vaults = new ArrayList<Vault>();

		vaults.add(new Vault(new Rectangle(w / 2 - 3 * vaultTexture.getWidth(),
				h / 2 + vaultTexture.getHeight() - 20, vaultTexture.getWidth(),
				vaultTexture.getHeight())));
		vaults.add(new Vault(new Rectangle(w / 2 - vaultTexture.getWidth(), h
				/ 2 + vaultTexture.getHeight() - 20, vaultTexture.getWidth(),
				vaultTexture.getHeight())));
		vaults.add(new Vault(new Rectangle(w / 2 + vaultTexture.getWidth(), h
				/ 2 + vaultTexture.getHeight() - 20, vaultTexture.getWidth(),
				vaultTexture.getHeight())));

		vaults.add(new Vault(new Rectangle(w / 2 - 3 * vaultTexture.getWidth(),
				h / 2 - 50, vaultTexture.getWidth(), vaultTexture.getHeight())));
		vaults.add(new Vault(new Rectangle(w / 2 - vaultTexture.getWidth(),
				h / 2 - 50, vaultTexture.getWidth(), vaultTexture.getHeight())));
		vaults.add(new Vault(new Rectangle(w / 2 + vaultTexture.getWidth(),
				h / 2 - 50, vaultTexture.getWidth(), vaultTexture.getHeight())));

		vamps.add(new Vamp(new Rectangle(0, h - 70, 100, 64), Direction.RIGHT,
				2, 6));
		vamps.add(new Vamp(new Rectangle(100, h - 70, 100, 64),
				Direction.RIGHT, 2, 2));
		vamps.add(new Vamp(new Rectangle(200, h - 70, 100, 64), Direction.LEFT,
				2, 3));
		vamps.add(new Vamp(new Rectangle(35, h - 70, 100, 64), Direction.LEFT,
				2, 5));

		vamps.add(new Vamp(new Rectangle(790, h - 70, 100, 64),
				Direction.RIGHT, 2, 1));
		vamps.add(new Vamp(new Rectangle(388, h - 70, 100, 64),
				Direction.RIGHT, 2, 4));
		vamps.add(new Vamp(new Rectangle(678, h - 70, 100, 64), Direction.LEFT,
				2, 3));
		vamps.add(new Vamp(new Rectangle(234, h - 70, 100, 64), Direction.LEFT,
				2, 4));

		vamps.add(new Vamp(new Rectangle(16, h - 70, 100, 64), Direction.RIGHT,
				2, 2));
		vamps.add(new Vamp(new Rectangle(113, h - 70, 100, 64),
				Direction.RIGHT, 2, 2));
		vamps.add(new Vamp(new Rectangle(600, h - 70, 100, 64), Direction.LEFT,
				2, 4));
		vamps.add(new Vamp(new Rectangle(47, h - 70, 100, 64), Direction.LEFT,
				2, 4));
	}

	private void createScreens() {
		scr_splash = new Texture(Gdx.files.internal("data/prop/splash.png"));
		scr_tutorial = new Texture(Gdx.files.internal("data/prop/tutorial.png"));
		scr_mainmenu = new Texture(
				Gdx.files.internal("data/prop/m_mainmenu.png"));
		scr_credit = new Texture(Gdx.files.internal("data/prop/creditscr.png"));
		scr_pedia = new Texture(Gdx.files.internal("data/prop/pediascr.png"));
		scr_option = new Texture(Gdx.files.internal("data/prop/optionscr.png"));
		scr_confirm = new Texture(
				Gdx.files.internal("data/prop/confirmscr.png"));
		scr_pause = new Texture(Gdx.files.internal("data/prop/pausemenu.png"));
		scr_blank = new Texture(Gdx.files.internal("data/prop/blank.png"));

		mm_mainmenu = new Texture(Gdx.files.internal("data/prop/mainmenu.png"));
		mm_exit = new Texture(Gdx.files.internal("data/prop/mm_exit.png"));
		mm_option = new Texture(Gdx.files.internal("data/prop/mm_option.png"));
		mm_pedia = new Texture(Gdx.files.internal("data/prop/mm_redpedia.png"));
		mm_credit = new Texture(Gdx.files.internal("data/prop/mm_credit.png"));
		mm_fb = new Texture(Gdx.files.internal("data/prop/icon_facebook.png"));
		mm_tw = new Texture(Gdx.files.internal("data/prop/icon_twitter.png"));

		createStartGameAnimation();
	}

	private void createStartGameAnimation() {
		startTexture = new Texture(Gdx.files.internal("data/prop/playmenu.png"));
		startTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion[][] tmp = TextureRegion.split(startTexture,
				startTexture.getWidth() / 2, startTexture.getHeight() / 1);
		startFrames = new TextureRegion[2];
		int index = 0;
		for (int i = 0; i < 2; i++) {
			startFrames[index++] = tmp[0][i];
		}
		start = new Animation(.1f, startFrames);

	}

	private void initEmergencyEntities() {
		ap = new Texture(Gdx.files.internal("data/prop/_APos.png"));
		am = new Texture(Gdx.files.internal("data/prop/_ANeg.png"));
		op = new Texture(Gdx.files.internal("data/prop/_OPos.png"));
		om = new Texture(Gdx.files.internal("data/prop/_ONeg.png"));
		abp = new Texture(Gdx.files.internal("data/prop/_ABPos.png"));
		abm = new Texture(Gdx.files.internal("data/prop/_ABNeg.png"));
		bp = new Texture(Gdx.files.internal("data/prop/_BPos.png"));
		bm = new Texture(Gdx.files.internal("data/prop/_BNeg.png"));

		donorSleep = new Texture(
				Gdx.files.internal("data/karakter/donor_sleep.png"));

		bloodtypeTexture = new Texture(
				Gdx.files.internal("data/prop/bloodtype.png"));
		TextureRegion[][] tmp = TextureRegion.split(bloodtypeTexture,
				bloodtypeTexture.getWidth() / 8,
				bloodtypeTexture.getHeight() / 1);
		bloodtypesReg = new TextureRegion[8];
		int index = 0;
		for (int i = 0; i < 8; i++) {
			bloodtypesReg[index++] = tmp[0][i];
		}

		x_happy = new Texture(Gdx.files.internal("data/prop/x_happy.png"));
		x_cry = new Texture(Gdx.files.internal("data/prop/x_cry.png"));
		x_vamplol = new Texture(
				Gdx.files.internal("data/prop/vampire-laugh.png"));
		x_vampdie = new Texture(
				Gdx.files.internal("data/prop/vampire-star.png"));

		BedsEM = new ArrayList<Bed>();

		Rectangle B_EmergUnit = new Rectangle(bg01x, 0, 800, 480);

		BedsEM.add(new Bed(new Rectangle(B_EmergUnit.x + 30 + 1 * 125,
				B_EmergUnit.y + 4 * 75, 52, 80), BedsEM.size() + 1));
		BedsEM.add(new Bed(new Rectangle(B_EmergUnit.x + 30 + 2 * 125,
				B_EmergUnit.y + 4 * 75, 52, 80), BedsEM.size() + 1));
		BedsEM.add(new Bed(new Rectangle(B_EmergUnit.x + 30 + 3 * 125,
				B_EmergUnit.y + 4 * 75, 52, 80), BedsEM.size() + 1));
		BedsEM.add(new Bed(new Rectangle(B_EmergUnit.x + 30 + 4 * 125,
				B_EmergUnit.y + 4 * 75, 52, 80), BedsEM.size() + 1));

		Nurses = new ArrayList<Nurse>();
		Nurses.add(new Nurse(new Rectangle(B_EmergUnit.x + 90 + 1 * 125,
				B_EmergUnit.y + 4 * 75, 52, 80), Nurses.size() + 1,
				bloodtype.Am));
		Nurses.add(new Nurse(new Rectangle(B_EmergUnit.x + 90 + 2 * 125,
				B_EmergUnit.y + 4 * 75, 52, 80), Nurses.size() + 1,
				bloodtype.Op));
		Nurses.add(new Nurse(new Rectangle(B_EmergUnit.x + 90 + 3 * 125,
				B_EmergUnit.y + 4 * 75, 52, 80), Nurses.size() + 1,
				bloodtype.ABm));
		Nurses.add(new Nurse(new Rectangle(B_EmergUnit.x + 90 + 4 * 125,
				B_EmergUnit.y + 4 * 75, 52, 80), Nurses.size() + 1,
				bloodtype.Bm));

		Donors2 = new ArrayList<Donor>();
		Donors2.add(new Donor(new Rectangle(B_EmergUnit.x + 30 + 1 * 125,
				B_EmergUnit.y + 4 * 75, 52, 80), Donors2.size() + 1,
				bloodtype.Am));
		Donors2.add(new Donor(new Rectangle(B_EmergUnit.x + 30 + 2 * 125,
				B_EmergUnit.y + 4 * 75, 52, 80), Donors2.size() + 1,
				bloodtype.Op));
		Donors2.add(new Donor(new Rectangle(B_EmergUnit.x + 30 + 3 * 125,
				B_EmergUnit.y + 4 * 75, 52, 80), Donors2.size() + 1,
				bloodtype.ABm));
		Donors2.add(new Donor(new Rectangle(B_EmergUnit.x + 30 + 4 * 125,
				B_EmergUnit.y + 4 * 75, 52, 80), Donors2.size() + 1,
				bloodtype.Bm));
	}

	private void initDonorCenterEntities() {
		// / INIT DONORS
		Donors = new ArrayList<Donor>();
		Donors.add(new Donor(0, 0, Direction.RIGHT, 2));
		Donors.add(new Donor(100, 0, Direction.RIGHT, 2));
		Donors.add(new Donor(200, 0, Direction.LEFT, 2));
		Donors.add(new Donor(35, 0, Direction.LEFT, 2));

		Donors.add(new Donor(790, 0, Direction.RIGHT, 2));
		Donors.add(new Donor(388, 0, Direction.RIGHT, 2));
		Donors.add(new Donor(678, 0, Direction.LEFT, 2));
		Donors.add(new Donor(234, 0, Direction.LEFT, 2));

		Donors.add(new Donor(16, 0, Direction.RIGHT, 2));
		Donors.add(new Donor(113, 0, Direction.RIGHT, 2));
		Donors.add(new Donor(600, 0, Direction.LEFT, 2));
		Donors.add(new Donor(47, 0, Direction.LEFT, 2));

		// /INIT INTERIOR DONOR CENTER
		Rectangle B_DonorCenter = new Rectangle(bg01x, 0, 800, 480);

		Benchs = new ArrayList<Bench>();
		Benchs.add(new Bench(new Rectangle(B_DonorCenter.x
				+ B_DonorCenter.width - 110, B_DonorCenter.y + 2 * 70, 40, 20),
				Benchs.size() + 1));
		Benchs.add(new Bench(new Rectangle(B_DonorCenter.x
				+ B_DonorCenter.width - 110, B_DonorCenter.y + 3 * 70, 40, 20),
				Benchs.size() + 1));
		Benchs.add(new Bench(new Rectangle(B_DonorCenter.x
				+ B_DonorCenter.width - 110, B_DonorCenter.y + 4 * 70, 40, 20),
				Benchs.size() + 1));
		Benchs.add(new Bench(new Rectangle(B_DonorCenter.x
				+ B_DonorCenter.width - 110, B_DonorCenter.y + 5 * 70, 40, 20),
				Benchs.size() + 1));

		Benchs.add(new Bench(new Rectangle(B_DonorCenter.x
				+ B_DonorCenter.width - 180, B_DonorCenter.y + 4 * 70, 40, 20),
				Benchs.size() + 1));
		Benchs.add(new Bench(new Rectangle(B_DonorCenter.x
				+ B_DonorCenter.width - 180, B_DonorCenter.y + 5 * 70, 40, 20),
				Benchs.size() + 1));

		BedsDNC = new ArrayList<Bed>();
		BedsDNC.add(new Bed(new Rectangle(B_DonorCenter.x + 20 + 1 * 125,
				B_DonorCenter.y + 5 * 75, 52, 80), BedsDNC.size() + 1));
		BedsDNC.add(new Bed(new Rectangle(B_DonorCenter.x + 20 + 2 * 125,
				B_DonorCenter.y + 5 * 75, 52, 80), BedsDNC.size() + 1));
		BedsDNC.add(new Bed(new Rectangle(B_DonorCenter.x + 20 + 3 * 125,
				B_DonorCenter.y + 5 * 75, 52, 80), BedsDNC.size() + 1));
		// BedsDNC.add(new Bed(new Rectangle(B_DonorCenter.x + 20 + 4 * 125,
		// B_DonorCenter.y + 5 * 75, 52, 80), BedsDNC.size() + 1));

		BedsDNC.add(new Bed(new Rectangle(B_DonorCenter.x + 20 + 1 * 125,
				B_DonorCenter.y + 3 * 80, 52, 80), BedsDNC.size() + 1));
		BedsDNC.add(new Bed(new Rectangle(B_DonorCenter.x + 20 + 2 * 125,
				B_DonorCenter.y + 3 * 80, 52, 80), BedsDNC.size() + 1));
		// BedsDNC.add(new Bed(new Rectangle(B_DonorCenter.x + 20 + 3 * 125,
		// B_DonorCenter.y + 3 * 80, 52, 80), BedsDNC.size() + 1));
		// BedsDNC.add(new Bed(new Rectangle(B_DonorCenter.x + 20 + 4 * 125,
		// B_DonorCenter.y + 3 * 80, 52, 80), BedsDNC.size() + 1));

		BedsDNC.add(new Bed(new Rectangle(B_DonorCenter.x + 20 + 1 * 125,
				B_DonorCenter.y + 1 * 115, 52, 80), BedsDNC.size() + 1));
		BedsDNC.add(new Bed(new Rectangle(B_DonorCenter.x + 20 + 2 * 125,
				B_DonorCenter.y + 1 * 115, 52, 80), BedsDNC.size() + 1));
	}

	private void createBGprop() {
		bg01Texture = new Texture(Gdx.files.internal("data/prop/bg01.png"));
		bg02Texture = new Texture(Gdx.files.internal("data/prop/bg02.png"));
		bg03Texture = new Texture(Gdx.files.internal("data/prop/bg03.png"));
		bg01doorTexture = new Texture(
				Gdx.files.internal("data/prop/bgdoor01.png"));
		bg02doorTexture = new Texture(
				Gdx.files.internal("data/prop/bgdoor02.png"));
		bg03doorTexture = new Texture(
				Gdx.files.internal("data/prop/bgdoor03.png"));
		nextt = new Texture(Gdx.files.internal("data/prop/next.png"));
		prevt = new Texture(Gdx.files.internal("data/prop/prev.png"));
		pauset = new Texture(Gdx.files.internal("data/prop/pause.png"));

		benchTexture = new Texture(
				Gdx.files.internal("data/prop/propchair.png"));
		bedTexture = new Texture(Gdx.files.internal("data/prop/propbed.png"));

		bloodpackTexture = new Texture(
				Gdx.files.internal("data/prop/proppack.png"));

		ambulanceTexture = new Texture(
				Gdx.files.internal("data/prop/propambulance.png"));

		lockTexture = new Texture(Gdx.files.internal("data/prop/lock.png"));
	}

	private void createTimerAnimation() {
		timerTexture = new Texture(
				Gdx.files.internal("data/prop/timer_fix.png"));
		timerTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion[][] tmp = TextureRegion.split(timerTexture,
				timerTexture.getWidth() / 11, timerTexture.getHeight() / 1);
		timerFrames = new TextureRegion[11];
		int index = 0;
		for (int i = 0; i < 11; i++) {
			timerFrames[index++] = tmp[0][i];
		}
		timerAnimation = new Animation(1f, timerFrames);
	}

	private void createNurseWalkAnimation() {
		nurseWalkTexture = new Texture(
				Gdx.files.internal("data/karakter/nurse_idle.png"));
		nurseWalkTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion[][] tmp = TextureRegion.split(nurseWalkTexture,
				nurseWalkTexture.getWidth() / 2,
				nurseWalkTexture.getHeight() / 1);
		nurseWalkFrames = new TextureRegion[2];
		int index = 0;
		for (int i = 0; i < 2; i++) {
			nurseWalkFrames[index++] = tmp[0][i];
		}
		nurseWalk = new Animation(0.25f, nurseWalkFrames);

		donorSleepBTexture = new Texture(
				Gdx.files.internal("data/karakter/donor_sleepb.png"));
		donorSleepBTexture
				.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tmp = TextureRegion.split(donorSleepBTexture,
				donorSleepBTexture.getWidth() / 2,
				donorSleepBTexture.getHeight() / 1);
		donorSleepBFrames = new TextureRegion[2];
		index = 0;
		for (int i = 0; i < 2; i++) {
			donorSleepBFrames[index++] = tmp[0][i];
		}
		donorSleepB = new Animation(0.25f, donorSleepBFrames);
	}

	private void createDonorWalkAnimation() {
		donorWalkTexture = new Texture(
				Gdx.files.internal("data/karakter/donor_walk.png"));
		donorWalkTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion[][] tmp = TextureRegion.split(donorWalkTexture,
				donorWalkTexture.getWidth() / 2,
				donorWalkTexture.getHeight() / 1);
		donorWalkFrames = new TextureRegion[2];
		int index = 0;
		for (int i = 0; i < 2; i++) {
			donorWalkFrames[index++] = tmp[0][i];
		}
		donorWalk = new Animation(0.25f, donorWalkFrames);

		donorWalkTexture2 = new Texture(
				Gdx.files.internal("data/karakter/donor_walk2.png"));
		donorWalkTexture2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tmp = TextureRegion.split(donorWalkTexture2,
				donorWalkTexture2.getWidth() / 2,
				donorWalkTexture2.getHeight() / 1);
		donorWalkFrames2 = new TextureRegion[2];
		index = 0;
		for (int i = 0; i < 2; i++) {
			donorWalkFrames2[index++] = tmp[0][i];
		}
		donorWalk2 = new Animation(0.25f, donorWalkFrames2);
	}

	@Override
	public void dispose() {
		batch.dispose();
		donorWalkTexture.dispose();
		bg01Texture.dispose();
		bg02Texture.dispose();
		bg03Texture.dispose();
		bg01doorTexture.dispose();
		bg02doorTexture.dispose();
		bg03doorTexture.dispose();
	}

	private void updateDonorCenter() {
		stateTime += Gdx.graphics.getDeltaTime();
		currDonorWalkFrames = donorWalk.getKeyFrame(stateTime, true);
		currDonorWalkFrames2 = donorWalk2.getKeyFrame(stateTime, true);
		currNurseWalkFrames = nurseWalk.getKeyFrame(stateTime, true);

		if (!isWin) {
			if (TodayScore >= LEVEL1SCORE) {
				isWin = true;
			}

			for (Donor d : Donors) {
				if (d.PositionX == Gdx.graphics.getWidth() / 2
						&& d.donationWill)
					if (d.shouldIDonate()) {
						int emptyBenchCount = 0;
						for (Bench b : Benchs) {
							if (!b.isOccupied)
								emptyBenchCount++;
						}
						if (emptyBenchCount > 0)
							for (Bench b : Benchs) {
								if (!b.isOccupied) {
									d.bench = b;
									d.direction = Direction.RIGHT;
									b.isOccupied = true;
									break;
								}
							}
						else
							d.cancelDonation();
					}
				d.Update();

				if (d.state == DonorLocationState.BED) {
					d.injectTimer += Gdx.graphics.getDeltaTime();
					d.currTimerFrames = timerAnimation.getKeyFrame(
							d.injectTimer, false);
				}
			}
		} else {
			for (Donor d : Donors) {
				// d.Update();

			}

		}

	}

	private void updateEmergencyUnit() {
		stateTime += Gdx.graphics.getDeltaTime();
		currDonorSleepBFrames = donorSleepB.getKeyFrame(stateTime, true);
		
//		if(!isWin){
			int counter = 0;
			for (Donor d : Donors2) {
				if(d.resipienstate == RESIPIEN.TRUE){
					level2scoreTrue++;
					counter++;
				}
				if(d.resipienstate == RESIPIEN.FALSE){
					level2scoreFalse++;
					counter++;
				}
			}
//			if(counter == 4){
//				isWin = true;
//			}
//		}
		
		
	}

	private void drawDonorCenter() {
		batch.begin();

		batch.draw(bg01Texture, bg01x, 0);

		for (Bench b : Benchs) {
			batch.draw(benchTexture, b.rectangle.x, b.rectangle.y);
		}

		for (Bed b : BedsDNC) {
			batch.draw(bedTexture, b.rectangle.x, b.rectangle.y);
		}

		// HUD KIRI
		font.setColor(1, 1, 1, 1f);
		int emptyBenchCount = 0;
		for (Bench b : Benchs) {
			if (!b.isOccupied)
				emptyBenchCount++;
		}

		int emptyBedCount = 0;
		for (Bed b : BedsDNC) {
			if (!b.isOccupied)
				emptyBedCount++;
		}

		batch.draw(board, bg01x + 12, 100, board.getWidth(), board.getHeight());

		font.draw(batch, "" + emptyBenchCount, bg01x + 20, 140);
		batch.draw(benchTexture, bg01x + 40, 140 - 15, 16, 16);

		font.draw(batch, "" + emptyBedCount, bg01x + 20, 160);
		batch.draw(bedTexture, bg01x + 40, 160 - 15, 16, 16);

		drawDonors();

		batch.draw(bg01doorTexture, bg01x + 133, 100, 623,
				bg01doorTexture.getHeight());

		// NURSE

		batch.draw(currNurseWalkFrames, 560, 390);

		// HUD SCORE
		font.setColor(1f, 0f, 0f, 1f);
		font.draw(batch, TodayScore + " / 30", Gdx.graphics.getWidth() - 40,
				Gdx.graphics.getHeight() - 35);

		batch.draw(bloodpackTexture, Gdx.graphics.getWidth() - 27,
				Gdx.graphics.getHeight() - 90, 23, 39);

		font.draw(batch, " Blood", Gdx.graphics.getWidth() - 45,
				Gdx.graphics.getHeight() - 15);

		if (isWin) {
			batch.draw(scr_blank, 0, 0, w, h);
			batch.draw(scorescr, w / 2 - scorescr.getWidth() / 2, h / 2
					- scorescr.getHeight() / 2);

			// batch.draw(backicon,
			// w / 2 - scorescr.getWidth() / 2 + scorescr.getWidth() - 55,
			// h / 2 - scorescr.getHeight() / 2 + scorescr.getHeight() / 3 +
			// 115,
			// back.width, back.height);
		}
		batch.end();
	}

	private void drawDonors() {
		for (Donor d : Donors) {
//			if (d.direction == Direction.RIGHT) {
				batch.draw(currDonorWalkFrames, d.PositionX, d.PositionY);
//			} 
			
//			if (d.direction == Direction.LEFT) {
//				batch.draw(currDonorWalkFrames2, d.PositionX, d.PositionY);
//			}

			if (d.state == DonorLocationState.WALKOUT) {
				batch.draw(bloodpackTexture, d.bloodpack.rectangle.x,
						d.bloodpack.rectangle.y, 23, 39);
			}
			if (d.state == DonorLocationState.BED) {
				batch.draw(d.currTimerFrames, d.PositionX, d.PositionY + 70);
			}
		}

	}

	private void drawEmergencyUnit() {
		batch.begin();
		batch.draw(bg02Texture, bg02x, 0);

		for (Bed b : BedsEM) {
			batch.draw(bedTexture, b.rectangle.x, b.rectangle.y);
		}

		for (Nurse n : Nurses) {
			batch.draw(currNurseWalkFrames, n.rectangle.x, n.rectangle.y);
			if (n.active) {
				switch (n.type) {
				case Am:
					batch.draw(am, n.rectangle.x - 40, n.rectangle.y + 50);
					break;
				case ABm:
					batch.draw(abm, n.rectangle.x - 40, n.rectangle.y + 50);
					break;
				case Bm:
					batch.draw(bm, n.rectangle.x - 40, n.rectangle.y + 50);
					break;
				case Op:
					batch.draw(op, n.rectangle.x - 40, n.rectangle.y + 50);
					break;
				}
			}
		}

		for (Donor d : Donors2) {
			batch.draw(donorSleep, d.recta.x, d.recta.y, donorSleep.getWidth(),
					donorSleep.getHeight());

			if (d.active) {
				int idx = 0;
				for (Button r : d.BloodTypes) {
					batch.draw(bloodtypesReg[idx++], r.recta.x, r.recta.y,
							donorSleep.getWidth(), donorSleep.getHeight());
				}
			}

			switch (d.resipienstate) {
			case WAIT:
				// do nothing
				break;

			case TRUE:
				batch.draw(x_happy, d.recta.x, d.recta.y + d.recta.height,
						d.recta.width, d.recta.height);
				break;

			case FALSE:
				batch.draw(x_cry, d.recta.x, d.recta.y + d.recta.height,
						d.recta.width, d.recta.height);
				break;

			}
		}

		batch.draw(bg02doorTexture, bg02x + 20, 100, 695,
				bg02doorTexture.getHeight());

		batch.end();
	}

	private void updateBloodVault() {
		stateTime += Gdx.graphics.getDeltaTime();
		currVampFrames = vamp.getKeyFrame(stateTime, true);
		currSecFrames = sec.getKeyFrame(stateTime, true);

		for (Vamp v : vamps) {

			if (v.hp < 1) {
				v.hp = 0;
				v.Location = VampLocation.DIE;
			}

			if (v.recta.x == Gdx.graphics.getWidth() / 2 && v.drinkWill) {
				if (v.shouldIDrink()) {
					Random r = new Random();
					int temp = r.nextInt(vaults.size() - 1);
					v.vault = vaults.get(temp);
					v.Location = VampLocation.FLYTOVAULT;
				}
			}

			if (v.Location == VampLocation.DIE) {
				if (v.recta.contains(sec01.recta.x, sec01.recta.y + 5)) {
					v.Location = VampLocation.CAPTURE;
				}
			}

			if (v.Location == VampLocation.CAPTURE) {
				v.recta.x = -200;
			}

			v.update();
		}

		if (sec01.recta.x > -200) {
			sec01.recta.x -= sec01.speed;
		}

	}

	private void drawBloodVault() {
		batch.begin();
		batch.draw(bg03Texture, bg03x, 0);

		for (Vault v : vaults) {
			batch.draw(vaultTexture, v.recta.x, v.recta.y);
		}

		batch.draw(bg03doorTexture, bg03x + 44, 100, 623,
				bg03doorTexture.getHeight());

		for (Vamp v : vamps) {
			batch.draw(currVampFrames, v.recta.x, v.recta.y, v.recta.width,
					v.recta.height);
			if (v.Location == VampLocation.FLYTOVAULT) {
				batch.draw(x_vamplol, v.recta.x, v.recta.y + v.recta.height,
						64, 57);
				// font.draw (batch, String.valueOf(v.hp), v.recta.x +
				// v.recta.width/2 + 15, v.recta.y + v.recta.height/8);

			}
			if (v.Location == VampLocation.BRINGPACK) {
				batch.draw(bloodpackTexture,
						v.recta.x + v.recta.width / 2 + 15, v.recta.y
								+ v.recta.height / 8, 23, 39);
				font.setColor(1, 1, 1, 1);
				font.draw(batch, String.valueOf(v.hp), v.recta.x
						+ v.recta.width / 2 + 25, v.recta.y + v.recta.height
						- 33);
			}

			if (v.Location == VampLocation.DIE
					|| v.Location == VampLocation.CAPTURE) {
				batch.draw(x_vampdie, v.recta.x, v.recta.y + v.recta.height,
						64, 57);
			}
		}

		batch.draw(sweepvamp, sweep.x, sweep.y, sweep.width, sweep.height);
		batch.draw(currSecFrames, sec01.recta.x, sec01.recta.y,
				sec01.recta.getWidth(), sec01.recta.getHeight());

		batch.end();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);

		switch (SCREENSTATE) {
		case SPLASH:

			batch.begin();
			batch.draw(scr_splash, 0, 0, w, h);
			batch.end();

			break;
		case MAINMENU:
			drawMainMenu();
			break;

		case TUTORIAL:

			batch.begin();
			batch.draw(scr_tutorial, 0, 0, w, h);
			batch.end();

			break;

		case GAMEPLAY:
			if (flingState == FlingState.Idle) {
				switch (SceneState) {
				case DONORCENTER:

					if (PAUSESTATE == PAUSE_STATE.PLAY) {
						updateDonorCenter();
						drawDonorCenter();
					} else if (PAUSESTATE == PAUSE_STATE.PAUSE) {
						drawDonorCenter();
						drawPausePopUp();
					}

					batch.begin();
					batch.draw(nextt, next.x, next.y, nextt.getWidth(),
							nextt.getHeight());
					batch.draw(pauset, pause.x, pause.y, pause.getWidth(),
							pause.getHeight());
					batch.end();
					break;
				case EMERGENCY:
					updateEmergencyUnit();
					drawEmergencyUnit();

					batch.begin();
					batch.draw(nextt, next.x, next.y, nextt.getWidth(),
							nextt.getHeight());
					batch.draw(prevt, prev.x, prev.y, prevt.getWidth(),
							prevt.getHeight());
					batch.draw(pauset, pause.x, pause.y, pause.getWidth(),
							pause.getHeight());
					batch.end();
					break;
				case BLOODBANK:
					updateBloodVault();
					drawBloodVault();

					batch.begin();
					batch.draw(prevt, prev.x, prev.y, prevt.getWidth(),
							prevt.getHeight());
					batch.draw(pauset, pause.x, pause.y, pause.getWidth(),
							pause.getHeight());
					batch.end();
					break;
				}
			} else if (flingState == FlingState.Flinging) {
				if (flinger.equals("Right")) {
					switch (SceneState) {
					case DONORCENTER:
						flingState = FlingState.Idle;

						break;
					case EMERGENCY:
						if (bg01x < 0) {
							bg01x += 10;
							bg02x += 10;
							bg03x += 10;
						}
						if (bg01x >= 0) {
							bg01x = 0;
							SceneState = Scene.DONORCENTER;
							flingState = FlingState.Idle;
						}

						batch.begin();

						drawFlingBG();

						for (Donor d : Donors) {
							d.PositionX += 10;
							if (d.direction == Direction.RIGHT) {
								batch.draw(currDonorWalkFrames, d.PositionX,
										d.PositionY);
							} else if (d.direction == Direction.RIGHT) {
								batch.draw(currDonorWalkFrames2, d.PositionX,
										d.PositionY);
							}
						}

						// font.setColor(.50f, .60f, .126f, 1f);
						// font.draw(batch, "DONOR CENTER " + flinger, 0,
						// Gdx.graphics.getHeight() - 55);

						batch.end();
						break;
					case BLOODBANK:
						if (bg01x < -800) {
							bg01x += 10;
							bg02x += 10;
							bg03x += 10;
						}
						if (bg01x >= -800) {
							bg01x = -800;
							SceneState = Scene.EMERGENCY;
							flingState = FlingState.Idle;
						}

						batch.begin();

						drawFlingBG();

						// font.setColor(.50f, .60f, .126f, 1f);
						// font.draw(batch, "EMERGENCY " + flinger, 0,
						// Gdx.graphics.getHeight() - 55);

						batch.end();
						break;
					}
				} else if (flinger.equals("Left")) {
					switch (SceneState) {
					case DONORCENTER:
						if (bg01x > -800) {
							bg01x -= 10;
							bg02x -= 10;
							bg03x -= 10;
						}
						if (bg01x <= -800) {
							bg01x = -800;
							SceneState = Scene.EMERGENCY;
							flingState = FlingState.Idle;
						}

						batch.begin();

						drawFlingBG();

						for (Donor d : Donors) {
							d.PositionX -= 10;
							batch.draw(currDonorWalkFrames, d.PositionX,
									d.PositionY);
						}

						// debug
						// font.setColor(.50f, .60f, .126f, 1f);
						// font.draw(batch, "DONOR CENTER " + flinger, 0,
						// Gdx.graphics.getHeight() - 55);

						batch.end();

						break;
					case EMERGENCY:
						if (bg01x > -1600) {
							bg01x -= 10;
							bg02x -= 10;
							bg03x -= 10;
						}
						if (bg01x <= -1600) {
							bg01x = -1600;
							SceneState = Scene.BLOODBANK;
							flingState = FlingState.Idle;
						}

						batch.begin();

						drawFlingBG();

						// font.setColor(.50f, .60f, .126f, 1f);
						// font.draw(batch, "EMERGENCY " + flinger, 0,
						// Gdx.graphics.getHeight() - 55);

						batch.end();

						break;
					case BLOODBANK:
						flingState = FlingState.Idle;

						break;
					}
				}
			}
			break;
		// case PAUSE:
		// batch.begin();
		// batch.draw(scr_pause, w / 2 - scr_pause.getWidth() / 2, h / 2
		// + scr_pause.getHeight() / 2, w, h);
		// batch.end();
		// break;

		case OPTION:
			drawMainMenu();

			batch.begin();
			batch.draw(scr_blank, 0, 0, w, h);
			batch.draw(scr_option, w / 2 - scr_option.getWidth() / 2, h / 2
					- scr_option.getHeight() / 2, scr_option.getWidth(),
					scr_option.getHeight());
			batch.draw(backicon, back.x, back.y, back.width, back.height);

			batch.end();
			break;

		case UPGRADE:
			batch.begin();

			batch.draw(backicon, back.x, back.y, back.width, back.height);
			batch.end();
			break;
		case PEDIA:
			batch.begin();

			batch.draw(scr_pedia, 0, 0, w, h);
			batch.draw(backicon, back.x, back.y, back.width, back.height);

			batch.draw(nextt, next.x, next.y, nextt.getWidth(),
					nextt.getHeight());
			batch.draw(prevt, prev.x, prev.y, prevt.getWidth(),
					prevt.getHeight());

			batch.end();
			break;
		case CONFIRM:
			drawMainMenu();

			batch.begin();
			batch.draw(scr_blank, 0, 0, w, h);
			batch.draw(scr_confirm, w / 2 - scr_confirm.getWidth() / 2, h / 2
					- scr_confirm.getHeight() / 2, scr_confirm.getWidth(),
					scr_confirm.getHeight());

			batch.end();
			break;
		case CREDITS:
			batch.begin();
			batch.draw(scr_credit, 0, 0, w, h);
			// batch.draw(backicon, back.x, back.y, back.width, back.height);
			batch.end();
			break;

		case FACEBOOK:
			batch.begin();
			batch.draw(backicon, back.x, back.y, back.width, back.height);
			batch.end();
			break;
		case TWITTER:

			batch.begin();
			batch.draw(backicon, back.x, back.y, back.width, back.height);
			batch.end();
			break;

		}

		// batch.begin();
		// font.setColor(.50f, .60f, .126f, 1f);
		// font.draw(batch, "FLING: " + flinger, 0, Gdx.graphics.getHeight() -
		// 25);
		// batch.end();
	}

	private void drawMainMenu() {
		stateTime += Gdx.graphics.getDeltaTime();
		currStartFrames = start.getKeyFrame(stateTime, true);

		batch.begin();
		batch.draw(scr_tutorial,0,0, w, h);

		batch.draw(scr_mainmenu, 0, 0, w, h);
		batch.draw(currStartFrames, w / 2 - startTexture.getWidth() / 8, h / 2
				- startTexture.getHeight() / 3);

		batch.draw(mm_exit, 10, 10, mm_exit.getWidth(), mm_exit.getHeight());
		batch.draw(mm_option, 10, 1 * mm_option.getHeight() + 10,
				mm_option.getWidth(), mm_option.getHeight());
		batch.draw(mm_pedia, 10, 2 * mm_option.getHeight() + 10,
				mm_pedia.getWidth(), mm_pedia.getHeight());
		batch.draw(mm_credit, w / 2 - mm_credit.getWidth() / 5, 15,
				mm_credit.getWidth(), mm_credit.getHeight());
		batch.draw(mm_fb, w / 2 - mm_fb.getWidth() / 4,
				h / 4 - mm_fb.getHeight() / 3, mm_fb.getWidth(),
				mm_fb.getHeight());
		batch.draw(mm_tw, w / 2 + mm_tw.getWidth() - (mm_tw.getWidth() / 4), h
				/ 4 - mm_tw.getHeight() / 3, mm_tw.getWidth(),
				mm_tw.getHeight());

		batch.end();
	}

	private void drawPausePopUp() {
		stateTime += Gdx.graphics.getDeltaTime();
		currStartFrames = start.getKeyFrame(stateTime, true);

		batch.begin();
		batch.draw(scr_blank, 0, 0, w, h);
		batch.draw(scr_pause, w / 2 - scr_pause.getWidth() / 2, h / 2
				- scr_pause.getHeight() / 2, scr_pause.getWidth(),
				scr_pause.getHeight());

		batch.draw(currStartFrames, w / 2 - startTexture.getWidth() / 4, h / 2
				- startTexture.getHeight() / 2, startTexture.getWidth() / 2,
				startTexture.getHeight());

		batch.draw(mm_mainmenu, w / 2 - mm_mainmenu.getWidth() / 2, h / 2 - 3
				* mm_mainmenu.getHeight(), mm_mainmenu.getWidth(),
				mm_mainmenu.getHeight());

		batch.end();

	}

	private void drawFlingBG() {
		batch.draw(bg01Texture, bg01x, 0);
		batch.draw(bg01doorTexture, bg01x + 133, 100, 623,
				bg01doorTexture.getHeight());
		batch.draw(bg02Texture, bg02x, 0);
		batch.draw(bg02doorTexture, bg02x + 20, 100, 695,
				bg02doorTexture.getHeight());
		batch.draw(bg03Texture, bg03x, 0);
		batch.draw(bg03doorTexture, bg03x + 44, 100, 623,
				bg03doorTexture.getHeight());
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public enum FlingState {
		Flinging, Idle
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {

		isTouch = true;
		SingleTap.x = x;
		SingleTap.y = y;

		switch (SCREENSTATE) {
		case SPLASH:
			SCREENSTATE = SCREEN_STATE.MAINMENU;
			break;

		case MAINMENU:

			// Rectangle area of PLAY icon
			Rectangle r = new Rectangle(w / 2 - startTexture.getWidth() / 8, h
					/ 2 - startTexture.getHeight() / 3,
					startTexture.getWidth(), startTexture.getHeight());
			if (r.contains(x, Gdx.graphics.getHeight() - y)) {
				SCREENSTATE = SCREEN_STATE.TUTORIAL;
			}

			// Rectangle area of OPTION icon
			r = new Rectangle(10, 1 * mm_option.getHeight() + 10,
					mm_option.getWidth(), mm_option.getHeight());
			if (r.contains(x, Gdx.graphics.getHeight() - y)) {
				SCREENSTATE = SCREEN_STATE.OPTION;
			}

			// Rectangle area of EXIT
			r = new Rectangle(10, 10, mm_exit.getWidth(), mm_exit.getHeight());
			if (r.contains(x, Gdx.graphics.getHeight() - y)) {
				SCREENSTATE = SCREEN_STATE.CONFIRM;
			}

			// Rectangle area of PEDIA
			r = new Rectangle(10, 2 * mm_option.getHeight() + 10,
					mm_pedia.getWidth(), mm_pedia.getHeight());
			if (r.contains(x, Gdx.graphics.getHeight() - y)) {
				SCREENSTATE = SCREEN_STATE.PEDIA;
			}

			// Rectangle area of CREDITS icon
			r = new Rectangle(w / 2 - mm_credit.getWidth() / 5, 15,
					mm_credit.getWidth(), mm_credit.getHeight());
			if (r.contains(x, Gdx.graphics.getHeight() - y)) {
				SCREENSTATE = SCREEN_STATE.CREDITS;
			}

			// Rectangle area of FB
			r = new Rectangle(w / 2 - mm_fb.getWidth() / 4, h / 4
					- mm_fb.getHeight() / 3, mm_fb.getWidth(),
					mm_fb.getHeight());
			if (r.contains(x, Gdx.graphics.getHeight() - y)) {
				SCREENSTATE = SCREEN_STATE.FACEBOOK;
			}

			// Rectangle area of TWITTER
			r = new Rectangle(
					w / 2 + mm_tw.getWidth() - (mm_tw.getWidth() / 4), h / 4
							- mm_tw.getHeight() / 3, mm_tw.getWidth(),
					mm_tw.getHeight());
			if (r.contains(x, Gdx.graphics.getHeight() - y)) {
				SCREENSTATE = SCREEN_STATE.TWITTER;
			}

			break;

		case TUTORIAL:

			SCREENSTATE = SCREEN_STATE.GAMEPLAY;

			break;

		case GAMEPLAY:
			if (flingState == FlingState.Idle) {
				switch (SceneState) {
				case DONORCENTER:

					if (next.contains(x, Gdx.graphics.getHeight() - y)) {
						flinger = "Left";
						flingState = FlingState.Flinging;
					}

					if (pause.contains(x, h - y)
							&& PAUSESTATE == PAUSE_STATE.PLAY) {
						PAUSESTATE = PAUSE_STATE.PAUSE;
					}

					Rectangle r3 = new Rectangle(w / 2
							- startTexture.getWidth() / 4, h / 2
							- startTexture.getHeight() / 2,
							startTexture.getWidth() / 2,
							startTexture.getHeight());
					if (r3.contains(x, h - y)
							&& PAUSESTATE == PAUSE_STATE.PAUSE) {
						PAUSESTATE = PAUSE_STATE.PLAY;
					}

					r3 = new Rectangle(w / 2 - mm_mainmenu.getWidth() / 2, h
							/ 2 - 3 * mm_mainmenu.getHeight(),
							mm_mainmenu.getWidth(), mm_mainmenu.getHeight());
					if (r3.contains(x, h - y)
							&& PAUSESTATE == PAUSE_STATE.PAUSE) {
						PAUSESTATE = PAUSE_STATE.PLAY;
						SCREENSTATE = SCREEN_STATE.MAINMENU;
					}

					r3 = new Rectangle(w / 2 - scorescr.getWidth() / 2
							+ scorescr.getWidth() - 55, h / 2
							- scorescr.getHeight() / 2 + scorescr.getHeight()
							/ 3 + 115, back.width, back.height);

					if (r3.contains(x, h - y)) {
						isWin = false;
						TotalScore += TodayScore;
						TodayScore = 0;
					}

					break;
				case EMERGENCY:

					if (pause.contains(x, h - y)) {
						PAUSESTATE = PAUSE_STATE.PAUSE;
					}

					if (prev.contains(x, Gdx.graphics.getHeight() - y)) {
						flinger = "Right";
						flingState = FlingState.Flinging;
					}
					if (next.contains(x, Gdx.graphics.getHeight() - y)) {
						flinger = "Left";
						flingState = FlingState.Flinging;
					}

					for (Nurse n : Nurses) {
						n.active = false;
					}

					for (Nurse n : Nurses) {
						if (n.rectangle.contains(x, Gdx.graphics.getHeight()
								- y)) {
							n.active = true;
							break;
						}
					}

					for (Donor d : Donors2) {
						switch (d.resipienstate) {
						case WAIT:
							if (!d.active) {
								if (d.recta.contains(x, h - y)) {
									d.active = true;
									break;
								}
							} else if (d.active) {
								for (Button rb : d.BloodTypes) {
									if (rb.recta.contains(x, h - y)
											&& rb.type == d.type) {
										d.resipienstate = RESIPIEN.TRUE;
										d.active = false;
										for (Nurse n : Nurses) {
											n.active = false;
										}
										break;
									} else if (rb.recta.contains(x, h - y)
											&& rb.type != d.type) {
										d.resipienstate = RESIPIEN.FALSE;
										d.active = false;
										for (Nurse n : Nurses) {
											n.active = false;
										}
										break;

									}
								}
							}
						}
					}

					break;
				case BLOODBANK:
					// UNLOCK!! UNDERCONSTRUCTION
					if (pause.contains(x, h - y)) {
						PAUSESTATE = PAUSE_STATE.PAUSE;
					}

					if (prev.contains(x, Gdx.graphics.getHeight() - y)) {
						flinger = "Right";
						flingState = FlingState.Flinging;
					}

					if (sweep.contains(x, h - y)) {
						sec01.recta.x = sweep.x;
					}

					for (Vamp v : vamps) {
						if (v.recta.contains(x, h - y)
								&& (v.Location == VampLocation.BRINGPACK || v.Location == VampLocation.FLYTOVAULT)) {
							v.hp--;
						}
					}
					break;
				}
			}
			break;

		case CREDITS:
			if (back.contains(x, h - y)) {
				SCREENSTATE = SCREEN_STATE.MAINMENU;
			}
			break;

		case FACEBOOK:
			if (back.contains(x, h - y)) {
				SCREENSTATE = SCREEN_STATE.MAINMENU;
			}
			break;
		case OPTION:
			if (back.contains(x, h - y)) {
				SCREENSTATE = SCREEN_STATE.MAINMENU;
			}
			break;
		case PEDIA:
			if (back.contains(x, h - y)) {
				SCREENSTATE = SCREEN_STATE.MAINMENU;
			}
			break;
		case TWITTER:
			if (back.contains(x, h - y)) {
				SCREENSTATE = SCREEN_STATE.MAINMENU;
			}
			break;
		case CONFIRM:
			Gdx.app.exit();
		}

		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {

		MyGdxGame.CurrentDraggedDonor = null;
		isTouch = false;

		for (Bed b : BedsDNC) {
			for (Donor d : Donors) {
				if (!b.isOccupied
						&& b.rectangle.contains(d.PositionX + 30,
								d.PositionY + 30) && !isTouch) {
					d.isPicking = false;
					d.isPickable = false;
					d.state = DonorLocationState.BED;
					d.PositionX = (int) b.rectangle.x;
					d.PositionY = (int) b.rectangle.y;
					d.bed = b;
					d.bloodpack.rectangle = new Rectangle(d.PositionX,
							d.PositionY, 32, 32);

					d.bench.isOccupied = false;
					d.bed.isOccupied = true;

					CurrentDraggedDonor = null;
					break;
				}
			}
		}

		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {

		for (Donor d : Donors) {
			if (d.isPicking) {
				d.PositionX = x - 27;
				d.PositionY = Gdx.graphics.getHeight() - y + 45;
			}
		}

		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {

		return false;
	}

	@Override
	public boolean scrolled(int amount) {

		return false;
	}

	@Override
	public boolean keyDown(int keycode) {

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	public enum SCREEN_STATE {
		SPLASH, MAINMENU, GAMEPLAY, PAUSE, TUTORIAL, OPTION, CREDITS, FACEBOOK, TWITTER, PEDIA, UPGRADE, CONFIRM
	}

	public enum PAUSE_STATE {
		PLAY, PAUSE, CONFIRM
	}
}
