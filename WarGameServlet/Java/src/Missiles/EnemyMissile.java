package Missiles;

import java.util.logging.Level;

import launchers.EnemyLauncher;
import war.IdfControlServlet;
import war.War;

public class EnemyMissile extends Thread {
	// enum for the statistics, showing current status
	public enum Mode {
		Ready, Launched, Hit
	};

	private Mode mode;
	private String id;
	private String destination;
	private EnemyLauncher enemy_Launcher;
	private int launchTime;
	private int flyTime;
	private int damage;
	private boolean isAlive;

	/**
	 * Enemy Missile constructor
	 * 
	 * @param damage
	 *            --> Integer, the damage of the missile
	 * @param destination
	 *            --> String, the destination of the missile
	 * @param flyTime
	 *            --> Integer, the flytime of the missile
	 * @param enemy_Launcher
	 *            --> Enemy_Launcher, the launcher of the missile
	 */
	public EnemyMissile(int damage, String destination, int flyTime,
			EnemyLauncher enemy_Launcher) {
		this.id = "M" + (int) (Math.random() * 100); // rendomize missile id
		this.destination = destination;
		this.flyTime = flyTime;
		this.damage = damage;
		this.enemy_Launcher = enemy_Launcher;
		this.launchTime = 4;
		this.isAlive = true;
		this.mode = Mode.Ready;
	}

	/**
	 * Enemy Missile constructor for XML read
	 * 
	 * @param damage
	 *            --> String, the damage of the missile
	 * @param destination
	 *            --> String, the destination of the missile
	 * @param flyTime
	 *            --> String, the flytime of the missile
	 * @param id
	 *            --> String, the id of the missile
	 * @param launchtime
	 *            --> String, the launchtime of the missile
	 * @param enemy_Launcher
	 *            --> Enemy_Launcher, the launcher of the missile
	 */
	public EnemyMissile(String damage, String destination, String flytime,
			String id, String launchtime, EnemyLauncher enemy_Launcher) {
		this.id = id;
		this.destination = destination;
		this.launchTime = Integer.parseInt(launchtime);
		this.flyTime = Integer.parseInt(flytime);
		this.damage = Integer.parseInt(damage);
		this.enemy_Launcher = enemy_Launcher;
		this.isAlive = true;
		this.mode = Mode.Ready;

	}

	/**
	 * Launch method - only delay the launch from the start
	 */
	public void launch() {
		synchronized (this) {
			try {
				Thread.sleep((long) launchTime * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Fly method of the missile
	 */
	public void fly() {

		synchronized (enemy_Launcher) {
			boolean status = enemy_Launcher.isHidden();
			if (status) { // if missile is hidden set him as unhidden
				enemy_Launcher.setHidden(false);
			}
			// if missile alive and launcher is alive
			if (this.isAlive && enemy_Launcher.alive()) {
			  War.theLogger.log(Level.INFO, " Missile " + getID()
						+ " fired to " + getDestination() + " and will hit in "
						+ getFlyTime() + "s", enemy_Launcher);
				this.mode = Mode.Launched;
			}
			try {
				Thread.sleep((long) flyTime * 1000);
			} catch (InterruptedException e) {				
			}
			if (this.isAlive) {
			  War.theLogger.log(Level.INFO, " Missile " + getID() + " hit "
						+ getDestination() + " for " + getDamage() + " damage",
						enemy_Launcher);
				// Setting the missile as "dead"
				enemy_Launcher.removeMissile(this);
				this.mode = Mode.Hit;
			}

			if (status) {
				enemy_Launcher.setHidden(true);

			}
		}

	}

	/**
	 * While missile is live launch him and fly and waits for notify for
	 * finishing
	 */
	@Override
	public void run() {
		while (isAlive) {
			try {
				launch();
				if (enemy_Launcher.alive() && this.isAlive) {
					fly();
				}

				synchronized (this) {
					wait();
				}
			} catch (InterruptedException e) {
			}
		}
	}

	// Getters and Setters
	public String getID() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDestination() {
		return destination;
	}

	public void setIsAlive(boolean bool) {
		this.isAlive = bool;
	}

	public boolean alive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getLaunchTime() {
		return launchTime;
	}

	public EnemyLauncher getFather() {
		return this.enemy_Launcher;

	}

	public void setLaunchTime(int launchTime) {
		this.launchTime = launchTime;
	}

	public int getFlyTime() {
		return flyTime;
	}

	public void setFlyTime(int flyTime) {
		this.flyTime = flyTime;
	}

	public int getDamage() {
		return damage;
	}

	public Mode getMode() {
		return mode;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

}
