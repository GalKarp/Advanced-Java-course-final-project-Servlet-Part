package war;

import java.util.Queue;
import java.util.Scanner;
import launchers.EnemyLauncher;
import launchers.IronDome;
import launchers.LauncherDestroyer;
import Missiles.EnemyMissile;
import Missiles.Interceptor;

public class Program {
	public static Scanner scanner = new Scanner(System.in);
	
	public Program() {
		War war = new War();
		menu(war);
	}	

	/**
	 * Show menu to the user
	 * 
	 * @param war
	 *            - current war
	 */
	public static void menu(War war) {

		try {
			int menuOption = -1;
			do {
				menuOption = showMenu(); // show Menu
				switch (menuOption) {

				case 1:
					add_Luncher_Destructor(war);
					break;
				case 2:
					add_Iron_Dome(war);
					break;
				case 3:
					add_Enemy_Launcher(war);
					break;
				case 4:
					launch_missile(war);
					break;
				case 5:
					destroy_Launcher(war);
					break;
				case 6:
					intercept_missile(war);
					break;
				case 7:
					show_Statistics(war);
					break;
				case 8:
					end_war(war);
					System.exit(0);
				default:
					System.out.println("Sorry, please enter valid Option");

				}

			} while (menuOption != 0);

			// Exiting message when user decides to quit Program
			System.out.println("Thanks for using this Program...");

		} catch (Exception ex) {
			// Error Message
			System.out.println("Sorry problem occured within Program");
			// flushing scanner
			scanner.next();

		} finally {
			// Ensuring that scanner will always be closed and cleaning up
			// resources
			scanner.close();
		}

	}

	/**
	 * finish the program and show war statistics
	 * 
	 * @param war
	 */
	private static void end_war(War war) {
		show_Statistics(war);
		System.exit(0);
	}

	/**
	 * Get war statistics
	 * 
	 * @param war
	 */
	private static void show_Statistics(War war) {
		int hit = 0;
		int damage = 0;
		int launched = 0;
		int intercept = 0;
		int destroyed_launcher = 0;
		for (EnemyMissile missile : war.getAllMissiles()) {
			if (missile.getMode() == EnemyMissile.Mode.Hit) {
				hit += 1;
				launched += 1;
				damage += missile.getDamage();
			} else if (missile.getMode() == EnemyMissile.Mode.Launched) {
				launched += 1;
			}
		}
		for (IronDome iron_dome : war.getIronDomes()) {
			for (Interceptor interceptor : iron_dome.getAllInterceptor()) {
				// if interceptor intercept missile
				if (interceptor.getStatus() == Interceptor.Status.Intercept) {
					intercept += 1;
				}
			}
		}
		for (EnemyLauncher launcher : war.getLaunchers()) {
			// launcher is down
			if (launcher.alive() == false) {
				destroyed_launcher += 1;
			}
		}
		System.out.println("Total Missiles Fired: " + launched);
		System.out.println("Total Missiles Intercepted:" + intercept);
		System.out.println("Total Missiles that hit target: " + hit);
		System.out.println("Total Launchers destroyed: " + destroyed_launcher);
		System.out.println("Total Missiles damage: " + damage);
	}

	/**
	 * Launch Interceptor from Iron Dome towards an Enemy Missile
	 * 
	 * @param war
	 */
	private static void intercept_missile(War war) {
		IronDome iDome = null;
		EnemyMissile e_missile = null;
		if (war.getIronDomes().isEmpty()) {
			System.out
					.println("\t No Iron Dome Available, Enter Iron Dome First");
		} else {
			System.out.println("Choose Iron Dome to shot from: ");
			for (IronDome dome : war.getIronDomes()) {
				System.out.print("\t" + dome.getDomeId());
			}
			System.out.println("\n Your selection: ");
			while (true) {
				String domeId = scanner.next();
				for (IronDome dome : war.getIronDomes()) {
					if (dome.getDomeId().equalsIgnoreCase(domeId)) {
						iDome = dome;
					}
				}
				if (iDome != null) {
					break;
				}

				else {
					System.out.println("Wrong Iron dome id, enter again: ");
				}

			}

			System.out.println("Select missile to intercept:");
			for (EnemyMissile missile : war.getAllMissiles()) {
				if (missile.alive()
						&& missile.getMode() == EnemyMissile.Mode.Launched) {
					System.out.print("\t" + missile.getID());
				}
			}
			System.out.println("\n Your selection: ");
			while (true) {
				String missileId = scanner.next();
				for (EnemyMissile missile : war.getAllMissiles()) {
					if (missile.getID().equalsIgnoreCase(missileId)) {
						e_missile = missile;
					}
				}
				if (e_missile != null)
					break;
				else
					System.out.println("Wrong Missile id, enter again: ");

			}
			int waitOnlaunch = (int) (Math.random() * 10);
			iDome.addMissileToIntercept(e_missile, "" + waitOnlaunch);
		}

	}

	/**
	 * Destroy a Launcher with a Launcher Destroyer
	 * 
	 * @param war
	 */
	private static void destroy_Launcher(War war) {
		LauncherDestroyer destroyer;
		EnemyLauncher user_launcher = null;
		String launcherId;
		int destroyerId = 0;
		if (war.getWarLauncherDestroyer().isEmpty()) {
			System.out.println("\tNo Destroyer available to hit the launcher");
		} else {

			System.out.println("Enter destroyer id: ");
			for (LauncherDestroyer launcher_destroyer : war
					.getWarLauncherDestroyer()) {
				System.out.print("\t" + launcher_destroyer.getLauncherType()
						+ " id: " + launcher_destroyer.getLauncherId());
			}
			System.out.println("\n Your selection: ");
			while (true) { // check user input
				destroyerId = scanner.nextInt();
				destroyer = war.findDestroyerById(destroyerId);
				if (destroyer != null)
					break;
				else
					System.out.println("Id not found, enter agian ");
			}
			System.out.println("Choose launcher to destroy");
			for (EnemyLauncher launcher : war.getLaunchers()) {
				if (launcher.alive())
					System.out.print("\t" + launcher.getLauncherId());
			}
			System.out.println("\n Your selection: ");

			while (true) { // check user input
				launcherId = scanner.next();
				user_launcher = war.findLauncherById(launcherId);

				if (user_launcher != null)
					break;
				else
					System.out.println("Id not found, enter agian ");
			}
			destroyer.addLauncherToDestroy(user_launcher, ""
					+ War.DESTROYER_MISSILE_DELAY_TIME);

		}

	}

	/**
	 * Create new Enemy Launcher
	 * 
	 * @param war
	 */
	private static void add_Enemy_Launcher(War war) {
		EnemyLauncher launcher = null;
		Queue<EnemyLauncher> launchers = war.getLaunchers();
		while (true) {
			launchers = war.getLaunchers();
			launcher = null;
			System.out.println("Enter new Launcher ID: ");
			String id = scanner.next();
			for (EnemyLauncher l : launchers) {
				if (l.getLauncherId().equalsIgnoreCase(id)) {
					launcher = l;
					break;
				}
			}
			if (launcher == null) {
				war.Create_enemy_launcher(id);
				break;
			} else {
				System.out.println("This ID already exists! ");
			}
		}

	}

	/**
	 * Create new Iron Dome
	 * 
	 * @param war
	 */
	private static void add_Iron_Dome(War war) {
		IronDome dome;
		Queue<IronDome> IronDomes = war.getIronDomes();
		while (true) {
			dome = null;
			System.out.println("Enter Iron Dome ID: ");
			String id = scanner.next();
			for (IronDome idome : IronDomes) {
				if (idome.getDomeId().equalsIgnoreCase(id)) {
					dome = idome;
					break;
				}
			}
			if (dome == null) {
				try {
					war.Create_Iron_Dome(id);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			} else {
				System.out.println("This ID already exists! ");
			}
		}
	}

	/**
	 * Create new Launcher Destroyer
	 * 
	 * @param war
	 */
	private static void add_Luncher_Destructor(War war) {
		System.out.println("Select launcher detroyer type: Plane or Ship: ");
		String type = scanner.next();
		while (true) { // user
			if (type.equalsIgnoreCase("ship") || type.equalsIgnoreCase("plane")) {
				war.Create_Launcher_Destroyer(type);
				break;

			} else {
				System.out
						.println("Type should be Plane/Ship only \nType again: ");
				type = scanner.next();
			}

		}

	}

	/**
	 * Launch a missile from an Enemy Launcher to hit a city
	 * 
	 * @param war
	 */
	private static void launch_missile(War war) {
		if (war.getLaunchers().isEmpty()) {
			System.out.println("\n\tNo available Launchers");
		} else {
			EnemyLauncher user_launcher = null;
			System.out.println("Destination of the missile: ");
			String destination = scanner.next();
			System.out.println("Damage of the missile: ");
			int damage = scanner.nextInt();
			System.out.println("Flytime of the missile: ");
			int flytime = scanner.nextInt();
			while (true) {
				System.out.println("Choose Launcher to shoot from: ");
				for (EnemyLauncher launcher : war.getLaunchers()) {
					if (launcher.alive())
						System.out.print("\t" + launcher.getLauncherId());
				}
				System.out.println("\n Your selection: ");
				String launcherId = scanner.next();
				user_launcher = war.findLauncherById(launcherId);
				if (user_launcher != null) {
					war.LaunchMissile(destination, damage, flytime,
							user_launcher);
					break;
				} else {
					System.out.println("Launcher does not exist");
				}
			}

		}

	}

	/**
	 * Show Menu in console
	 * 
	 * @return
	 */
	public static int showMenu() {
		int option = 0;
		// Printing menu to screen
		System.out.println("\n\t\t Missile Management system \n");
		System.out.println("1.\t Add new launcher destructor");
		System.out.println("2.\t Add new Iron dome");
		System.out.println("3.\t Add new Missile launcher");
		System.out.println("4.\t Fire a missile");
		System.out.println("5.\t Destroy a Missile launcher");
		System.out.println("6.\t Intercept a Missile");
		System.out.println("7.\t Show statistics");
		System.out.println("8.\t End the war \n");
		// Getting user option from above menu
		System.out.print("Enter: ");
		option = scanner.nextInt();
		return option;
	}

}
