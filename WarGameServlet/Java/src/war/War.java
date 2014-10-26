package war;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import Missiles.EnemyMissile;
import launchers.EnemyLauncher;
import launchers.IronDome;
import launchers.LauncherDestroyer;
import logger.Handler;

/**
 * @author Gal Karp AND Andrey Chasovski
 */
public class War {
	public static final int THREAD_SLEEP_TIME = 1000;
	public static final double LAUNCHER_HIDE_RATE = Math.random() * 10;
	public static final int DESTROYER_MISSILE_DELAY_TIME = 2;
	private Queue<EnemyLauncher> launchers = new LinkedList<EnemyLauncher>();
	private Queue<IronDome> ironDomes = new LinkedList<IronDome>();
	private Queue<LauncherDestroyer> LauncherDestroyers = new LinkedList<LauncherDestroyer>();
	private Queue<EnemyMissile> allMissiles = new LinkedList<EnemyMissile>();
	private EnemyLauncher enemy_launcher;
	private IronDome iron_dome;
	private LauncherDestroyer launcherDestroyer;
	public static Logger theLogger = Logger.getLogger("myLogger");

	War() {
		initLogger();
		new readXml(this, launchers, ironDomes, LauncherDestroyers, allMissiles);

	}


	private void initLogger() {
		File file = new File("loggerFiles");
		if (file.exists()) {
		  System.out.println("2");
			String[] myFiles;
			if (file.isDirectory()) {
				myFiles = file.list();
				for (int i = 0; i < myFiles.length; i++) {
					File myFile = new File(file, myFiles[i]);
					myFile.delete();
				}
			}
		} else {
			file.mkdir();
		}

		try {
		  
			theLogger.setUseParentHandlers(false);
			theLogger.addHandler((new Handler("FullWarLog")));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Creates enemy launcher by given id
	 * 
	 * @param id
	 *            -->String, the id for the new launcher
	 */
	public void Create_enemy_launcher(String id) {
		// if the launcher will be hidden or not
		enemy_launcher = new EnemyLauncher(id,
				(LAUNCHER_HIDE_RATE < 2 ? "true" : "false"));
		launchers.add(enemy_launcher);
		enemy_launcher.start();
	}

	/**
	 * Add a new enemy launcher to the Launcher QUEUE and start the Thread
	 * 
	 * @param enemy_launcher
	 *            --> The launcher you want to add
	 */
	public void Create_enemy_launcher(EnemyLauncher enemy_launcher) {
		launchers.add(enemy_launcher);
		enemy_launcher.start();
	}

	/**
	 * Create new Launcher destroyer and start Thread
	 * 
	 * @param type
	 *            --> Enter the type of the destroyer plain/ship
	 * @return The new Launcher Destroyer
	 */
	public LauncherDestroyer Create_Launcher_Destroyer(String type) {
		launcherDestroyer = new LauncherDestroyer(type);
		LauncherDestroyers.add(launcherDestroyer);
		launcherDestroyer.start();
		return launcherDestroyer;
	}

	/**
	 * Create new Iron Dome and start Thread
	 * 
	 * @param id
	 *            --> The iron dome id user select
	 * @return The new Iron Dome
	 */
	public IronDome Create_Iron_Dome(String id) {
		iron_dome = new IronDome(id);
		ironDomes.add(iron_dome);
		iron_dome.start();
		return iron_dome;
	}

	/**
	 * Create a new Enemy missile and launch him over destination
	 * 
	 * @param destination
	 *            --> String, The destination of the missile
	 * @param damage
	 *            --> Integer, The Damage of the missile
	 * @param flytime
	 *            --> Integer, The flytime of the missile
	 * @param launcher
	 *            --> Enemy_Launcher, The launcher which will fire the missile
	 */
	public void LaunchMissile(String destination, int damage, int flytime,
			EnemyLauncher launcher) {
		// Check if there are any launcher available or user should create new
		// one
		if (launchers.size() == 0) {
			System.out.println("There are no active launchers");
		} else {
			EnemyMissile em = new EnemyMissile(damage, destination, flytime,
					launcher);
			allMissiles.add(em);
			launcher.addMissile(em);
		}
	}

	/**
	 * Select the enemy launcher you want to destroy
	 * 
	 * @param destructTime
	 *            --> String, the delay time for the interception
	 * @param id
	 *            --> String, The id of the launcher you want to destroy
	 * @param launcherDestroyer
	 *            --> Launcher destroyer, The destroyer that destroy the
	 *            launcher
	 */
	public void DestroyLauncher(String destructTime, String id,
			LauncherDestroyer launcherDestroyer) {

		for (EnemyLauncher launcher : launchers) {
			if (launcher.getLauncherId().equalsIgnoreCase(id)) {
				launcherDestroyer.addLauncherToDestroy(launcher, destructTime);
				break;
			}
		}
	}

	/**
	 * Select the enemy missile you want to intercept
	 * 
	 * @param destructAfterLaunch
	 *            --> String, The time which the interception will occured
	 * @param id
	 *            --> String , The id of the enemy missile
	 * @param ironDome
	 *            --> Iron_Dome, the iron dome that will fire the interceptpr
	 */
	public void InterceptMissile(String destructAfterLaunch, String id,
			IronDome ironDome) {
		for (EnemyMissile missile : allMissiles) {
			if (missile.getID().equalsIgnoreCase(id)) {
				ironDome.addMissileToIntercept(missile, destructAfterLaunch);
				break;
			}
		}
	}

	/**
	 * Add a new missile to available launcher
	 * 
	 * @param enemy_launcher
	 *            --> Enemy_Launcher, the launcher the missile will be added to
	 * @param missile
	 *            --> Enemy missile, The missile that will be added
	 */
	public void addMissileToLauncher(EnemyLauncher enemy_launcher,
			EnemyMissile missile) {
		enemy_launcher.addMissile(missile);
		allMissiles.add(missile);
	}

	/**
	 * Help method for validation of user selection for Launcher
	 * 
	 * @param launcherId
	 *            --> String, the launcherId the will be checked
	 */
	public EnemyLauncher findLauncherById(String launcherId) {
		EnemyLauncher user_launcher = null;
		for (EnemyLauncher launcher : getLaunchers()) {
			if (launcher.getLauncherId().equalsIgnoreCase(launcherId)) {
				user_launcher = launcher;
				break;
			}
		}
		if (user_launcher != null) {
			return user_launcher;
		} else {
			return null;
		}
	}

	/**
	 * Help method for validation of user selection for destroyer
	 * 
	 * @param i
	 *            --> String, the destroyerId the will be checked
	 */
	public LauncherDestroyer findDestroyerById(int i) {
		
		LauncherDestroyer user_destroyer = null;
		for (LauncherDestroyer launcher_destroyer : getWarLauncherDestroyer()) {
			if (launcher_destroyer.getLauncherId() == i) {
				user_destroyer = launcher_destroyer;
				break;
			}
		}
		if (user_destroyer != null) {
			return user_destroyer;
		} else {
			return null;
		}
	}


	// Getters and Setters
	public Queue<EnemyLauncher> getLaunchers() {
		return launchers;
	}

	public Queue<EnemyMissile> getAllMissiles() {
		return allMissiles;
	}

	public Queue<IronDome> getIronDomes() {
		return ironDomes;
	}

	public IronDome getIronDomePeek() {
		return ironDomes.peek();
	}

	public Queue<LauncherDestroyer> getWarLauncherDestroyer() {
		return LauncherDestroyers;
	}

	public IronDome findIronDomeById(String ironDomeId) {
		IronDome iDome = null;
		for (IronDome dome : getIronDomes()) {
			if (dome.getDomeId().equalsIgnoreCase(ironDomeId)) {
				iDome = dome;
			}		
	}
		return iDome;
	}

	public EnemyMissile findMissileById(String missileId) {
		EnemyMissile enemyMissile = null;
		for (EnemyMissile missile : getAllMissiles()) {
			if (missile.getID().equalsIgnoreCase(missileId)) {
				enemyMissile = missile;
			}
		}
		return enemyMissile;
	}



}
