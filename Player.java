
public class Player {
	static double x = 0.5;
	static double y = 0.5;
	static int health = 1000;
	static int maxHealth = 1000;
	static long damage = 0;
	public static void tick() {
		if (Main.keyPress['a']) {
			Player.x-=0.002*Main.timePassed;
			if (Player.x%1<0.1&&!MazeMaker.maze[(int)Player.y][(int)Player.x].left) {
				Player.x+=0.002*Main.timePassed;
			}
		}
		if (Main.keyPress['s']) {
			Player.y+=0.002*Main.timePassed;
			if (Player.y%1>0.9&&!MazeMaker.maze[(int)Player.y][(int)Player.x].down) {
				Player.y-=0.002*Main.timePassed;
			}
		}
		if (Main.keyPress['d']) {
			Player.x+=0.002*Main.timePassed;
			if (Player.x%1>0.9&&!MazeMaker.maze[(int)Player.y][(int)Player.x].right) {
				Player.x-=0.002*Main.timePassed;
			}
		}
		if (Main.keyPress['w']) {
			Player.y-=0.002*Main.timePassed;
			if (Player.y%1<0.1&&!MazeMaker.maze[(int)Player.y][(int)Player.x].up) {
				Player.y+=0.002*Main.timePassed;
			}
		}
	}
	public static int[] visible() {
		int[] dimensions = {(int)Player.x, (int)Player.x, (int)Player.y, (int)Player.y};
		for (int i=(int)Player.x; i>=0; i--) {
			if (MazeMaker.maze[(int)Player.y][i].left) {
				dimensions[0]--;
			} else {
				break;
			}
		}
		for (int i=(int)Player.x; i<MazeMaker.MAZE_SIZE; i++) {
			if (MazeMaker.maze[(int)Player.y][i].right) {
				dimensions[1]++;
			} else {
				break;
			}
		}
		for (int i=(int)Player.y; i>=0; i--) {
			if (MazeMaker.maze[i][(int)Player.x].up) {
				dimensions[2]--;
			} else {
				break;
			}
		}
		for (int i=(int)Player.y; i<MazeMaker.MAZE_SIZE; i++) {
			if (MazeMaker.maze[i][(int)Player.x].down) {
				dimensions[3]++;
			} else {
				break;
			}
		}
		return dimensions;
	}
	public static void takeDamage(int d) {
		health-=d;
		damage = System.currentTimeMillis()+500;
	}
}
