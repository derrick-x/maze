import java.awt.Canvas;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Main extends Canvas{
	static boolean[] keyPress = new boolean[128];
	static MazeMaker.Path solution;
	static JFrame frame;
	static int repaintTick = 0;
	static long timePassed = 0;
	static int drugs = 50;
	static long currentTime = System.currentTimeMillis();
	static int[] visible = new int[4];
	static ArrayList<Enemy> enemies = new ArrayList<>();
	public static void main(String[] args) throws InterruptedException{
		run();
		MazeMaker.makeMaze(10);
		solution = MazeMaker.pathFind(new Point(0, 0), new Point(MazeMaker.MAZE_SIZE-1, MazeMaker.MAZE_SIZE-1));
		frame = new JFrame();
		Canvas canvas  = new Main();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.setSize(600, 600);
		frame.add(canvas);
		canvas.setBackground(Color.black);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
		canvas.setFocusable(false);
		timePassed = System.currentTimeMillis();
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				keyPress[e.getKeyChar()] = true;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keyPress[e.getKeyChar()] = false;
			}
		});
		for (int i=0; i<3; i++) {
			enemies.add(new Enemy((int)(Math.random()*MazeMaker.MAZE_SIZE), (int)(Math.random()*MazeMaker.MAZE_SIZE)));
		}
		while (true) {
			if (repaintTick>drugs) {
				canvas.repaint();
				repaintTick = 0;
			}
			canvas.paint(canvas.getGraphics());
			visible = Player.visible();
			timePassed = System.currentTimeMillis()-currentTime;
			currentTime = System.currentTimeMillis();
			repaintTick+=timePassed;
			for (int i=0; i<enemies.size(); i++) {
				enemies.get(i).tick();
			}
			Player.tick();	
		}
	}
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(300-(int)(Player.x*50)+(int)Player.x*50, visible[2]*50-(int)(Player.y*50)+300, 50, (visible[3]-visible[2]+1)*50);
		g.fillRect(visible[0]*50+300-(int)(Player.x*50), 300-(int)(Player.y*50)+(int)Player.y*50, (visible[1]-visible[0]+1)*50, 50);
		g.setColor(Color.blue);
		g.fillOval(290, 290, 20, 20);
		g.setColor(Color.red);
		for (Enemy e : enemies) {
			if ((int)e.y==(int)Player.y) {
				if ((int)e.x>=visible[0]&&(int)e.x<=visible[1]) {
					g.fillOval((int)((e.x-Player.x)*50)+290, (int)((e.y-Player.y)*50)+290, 20, 20);
				}
			}
			if ((int)e.x==(int)Player.x) {
				if ((int)e.y>=visible[2]&&(int)e.y<=visible[3]) {
					g.fillOval((int)((e.x-Player.x)*50)+290, (int)((e.y-Player.y)*50)+290, 20, 20);
				}
			}
			if (e.health<e.maxHealth) {
				g.setColor(Color.black);
				g.drawRect((int)((e.x-Player.x)*50)+285, (int)((e.y-Player.y)*50)+285, 30, 3);
				g.setColor(Color.red);
				g.fillRect((int)((e.x-Player.x)*50)+285, (int)((e.y-Player.y)*50)+285, (30*e.health)/e.maxHealth, 3);g.setColor(Color.white);
			}
		}
		g.drawRect(10, 500, 100, 10);
		g.setColor(Color.red);
		g.fillRect(10, 500, (100*Player.health)/Player.maxHealth, 10);
		if (Player.damage>System.currentTimeMillis()) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.gray);
		}
		for (int y=Math.max(0, (int)Player.y-1); y<MazeMaker.MAZE_SIZE&&y<Player.y+2; y++) {
			for (int x=Math.max(0, (int)Player.x-1); x<MazeMaker.MAZE_SIZE&&x<Player.x+2; x++) {
				MazeMaker.Cell cell = MazeMaker.maze[y][x];
				if (!cell.up) {
					g.fillRect(x*50-(int)(Player.x*50)+300, y*50-(int)(Player.y*50)+300, 50, 5);
				}
				if (!cell.right) {
					g.fillRect(x*50-(int)(Player.x*50)+350, y*50-(int)(Player.y*50)+300, 5, 50);
				}
				if (!cell.down) {
					g.fillRect(x*50-(int)(Player.x*50)+300, y*50-(int)(Player.y*50)+350, 50, 5);
				}
				if (!cell.left) {
					g.fillRect(x*50-(int)(Player.x*50)+300, y*50-(int)(Player.y*50)+300, 5, 50);
				}
			}
		}
	}
	static ScreenManager s;
	public static void run() {
		s = new ScreenManager();
		try {
			DisplayMode dm = new DisplayMode(600, 600, 32, 0);
			frame = s.setFullScreen(dm);
		} finally {
			
		}
	}
}
