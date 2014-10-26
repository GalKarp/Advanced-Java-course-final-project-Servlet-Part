package Missiles;

import java.util.logging.Level;

import launchers.IronDome;
import war.IdfControlServlet;
import war.War;

public class Interceptor extends Thread {
	// enum for setting interceptor status
	public enum Status {
		Intercept, missed
	};

	private int destructAfterLaunch;
	private String missleID;
	private IronDome father;
	private EnemyMissile missile = null;
	private Status status;

	/**
	 * Interceptor constructor for read from XML
	 * 
	 * @param id
	 *            --> String, the id of the interceptor
	 * @param destructAfterLaunch
	 *            --> String, time the interceptor wil hit target
	 * @param iron_Dome
	 *            --> iron_Dome, the iron dome that interceptor will be launch
	 *            from
	 */
	public Interceptor(String id, String destructAfterLaunch,
			IronDome iron_Dome) {
		this.father = iron_Dome;
		this.destructAfterLaunch = Integer.parseInt(destructAfterLaunch);
		this.missleID = id;
		this.start();
	}

	/**
	 * Interceptor constructor for user input
	 * 
	 * @param iron_Dome
	 *            --> iron_Dome, the iron dome that interceptor will be launch
	 *            from
	 * @param enemy_Missile
	 *            --> Enemy_Missile, the enemy missile that will be destroyed
	 * @param destructAfterLaunch
	 *            --> String, time the interceptor wil hit target
	 */
	public Interceptor(IronDome iron_Dome, EnemyMissile enemy_Missile,
			String destructAfterLaunch) {
		this.father = iron_Dome;
		this.destructAfterLaunch = Integer.parseInt(destructAfterLaunch);
		this.missile = enemy_Missile;
		this.start();
	}

	private void intercept() {
		// only one missile will be hit at iron dome
		synchronized (missile) {
			// calculate to know if missile will be intercept before he will hit
			// the ground
			if (destructAfterLaunch < missile.getFlyTime()
					+ missile.getLaunchTime()
					&& missile.getLaunchTime() <= destructAfterLaunch) {
				// check if missile is live and if he already launched
				if (missile.alive()
						&& missile.getMode() == EnemyMissile.Mode.Launched) {
				  War.theLogger.log(
							Level.INFO,
							" Iron dome #" + father.getDomeId()
									+ " intercepted missile #"
									+ missile.getID(), father);
				  War.theLogger.log(
							Level.INFO,
							" Iron dome #" + father.getDomeId()
									+ " intercepted missile #"
									+ missile.getID(), missile.getFather());
					missile.setIsAlive(false);
					// remove the destroyed missile from his launcher
					missile.getFather().removeMissile(missile);
					// Setting status to intercept
					this.status = Status.Intercept;
					missile.interrupt(); // stop thread sleep if missile is
					// intercepted

				} else if (missile.alive()
						&& missile.getMode() == EnemyMissile.Mode.Launched) {
				  War.theLogger.log(Level.INFO,
							" Iron dome #" + father.getDomeId()
									+ " failed to intercept missile #"
									+ missile.getID(), father);
				  War.theLogger.log(
							Level.INFO,
							" Iron dome #" + father.getDomeId()
									+ " failed to intercept #"
									+ missile.getID(), missile.getFather());
					// Setting enum status to missed
					this.status = Status.missed;
				}
			}

		}
	}

	public void run() {
		try {
			// delay for interception
			sleep(destructAfterLaunch * 1000);
			intercept();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Getters and Setters
	public Integer getDestructAfterLaunch() {
		return destructAfterLaunch;
	}

	public void setDestructAfterLaunch(int destructAfterLaunch) {
		this.destructAfterLaunch = destructAfterLaunch;
	}

	public String getMissleID() {
		return missleID;
	}

	public Status getStatus() {
		return status;
	}

	public void setMissleID(String missleID) {
		this.missleID = missleID;
	}
}
