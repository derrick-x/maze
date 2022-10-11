import java.awt.Point;

public class Enemy {
	double x;
	double y;
	int health;
	int maxHealth;
	int reload;
	int hitSpeed;
	boolean chasing;
	MazeMaker.Path pathfind;
	Point goal;
	public Enemy(int x, int y) {
		this.x = x;
		this.y = y;
		health = 1000;
		reload = 0;
		hitSpeed = 1000;
		maxHealth = health;
		chasing = false;
		goal = new Point(((int)Math.random())*MazeMaker.MAZE_SIZE, (int)(Math.random()*MazeMaker.MAZE_SIZE));
	}
	public void tick() {
		if ((int)x==goal.x&&(int)y==goal.y) {
			goal = new Point(((int)Math.random())*MazeMaker.MAZE_SIZE, (int)(Math.random()*MazeMaker.MAZE_SIZE));
			chasing = false;
		}
		if (Math.hypot(x-Player.x, y-Player.y)<3) {
			goal = new Point((int)Player.x, (int)Player.y);
			chasing = true;
		}
		pathfind = MazeMaker.pathFind(new Point((int)x, (int)y), goal);
		Point target;
		try {
			target = pathfind.directions.get(1);
		} catch (Exception e) {
			target = new Point(goal);
		}
		if (x+0.1<target.x+0.5) {
			x+=0.0005*Main.timePassed;
			
		} else if (x-0.1<target.x+0.5) {
			x = target.x+0.5;
		} else {
			x-=0.0005*Main.timePassed;
		}
		if (y+0.1<target.y+0.5) {
			y+=0.0005*Main.timePassed;
		} else if (y-0.1<target.y+0.5) {
			y = target.y+0.5;
		} else {
			y-=0.0005*Main.timePassed;
		}
		if (chasing) {
			if (x+0.1<target.x+0.5) {
				x+=0.0005*Main.timePassed;
				
			} else if (x-0.1<target.x+0.5) {
				x = target.x+0.5;
			} else {
				x-=0.0005*Main.timePassed;
			}
			if (y+0.1<target.y+0.5) {
				y+=0.0005*Main.timePassed;
			} else if (y-0.1<target.y+0.5) {
				y = target.y+0.5;
			} else {
				y-=0.0005*Main.timePassed;
			}
		}
		reload+=Main.timePassed;
		if (reload>hitSpeed) {
			reload = hitSpeed;
			if (Math.hypot(x-Player.x, y-Player.y)<1) {
				Player.takeDamage(100);
				reload = 0;
			}
		}
	}
}
