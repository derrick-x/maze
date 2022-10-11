import java.awt.Point;
import java.util.*;

public class MazeMaker {
	static final int MAZE_SIZE = 20;
	static Cell[][] maze = new Cell[MAZE_SIZE][MAZE_SIZE];
	static class Cell{
		boolean up = false, down = false, right = false, left = false;
		int xPos, yPos;
		public Cell(int x, int y) {
			xPos = x;
			yPos = y;
		}
		public void open(byte path) {
			switch (path) {
			case 0:
				right = true;
				break;
			case 1:
				down = true;
				break;
			case 2:
				left = true;
				break;
			case 3:
				up = true;
				break;
			}
		}
	}
	static class Path{
		ArrayList<Point> directions = new ArrayList<Point>();
		public Path(ArrayList<Point> d, Point p) {
			directions = new ArrayList<Point>(d);
			directions.add(p);
		}
	}
	public static void makeMaze(int remove) {
		int cellsFilled = 0;
		int xPos = 0;
		int yPos = 0;
		int emptyX = 0;
		int emptyY = 0;
		byte lastDirection = 0;
		while (cellsFilled<MAZE_SIZE*MAZE_SIZE) {
			if (maze[yPos][xPos]==null) {
				maze[yPos][xPos] = new Cell(xPos, yPos);
				if (cellsFilled>0) {
					maze[yPos][xPos].open(lastDirection);
				}
				cellsFilled++;
			}
			ArrayList<Integer> directions = new ArrayList<Integer>();
			if (xPos>0&&maze[yPos][xPos-1]==null) {
				directions.add(0);
			}
			if (yPos>0&&maze[yPos-1][xPos]==null) {
				directions.add(1);
			}
			if (xPos<MAZE_SIZE-1&&maze[yPos][xPos+1]==null) {
				directions.add(2);
			}
			if (yPos<MAZE_SIZE-1&&maze[yPos+1][xPos]==null) {
				directions.add(3);
			}
			if (directions.size()==0) {
				boolean found = false;
				while (emptyY<MAZE_SIZE) {
					while (emptyX<MAZE_SIZE) {
						if (maze[emptyY][emptyX]==null) {
							try {
								if (maze[emptyY][emptyX-1]!=null) {
									maze[emptyY][emptyX-1].right = true;
									lastDirection = 2;
									found = true;
									break;
								}
							} catch (Exception e) {
								
							}
							try {
								if (maze[emptyY-1][emptyX]!=null) {
									maze[emptyY-1][emptyX].down = true;
									lastDirection = 3;
									found = true;
									break;
								}
							} catch (Exception e) {
								
							}
							try {
								if (maze[emptyY][emptyX+1]!=null) {
									maze[emptyY][emptyX+1].left = true;
									lastDirection = 0;
									found = true;
									break;
								}
							} catch (Exception e) {
								
							}
							try {
								if (maze[emptyY+1][emptyX]!=null) {
									maze[emptyY][emptyX+1].up = true;
									lastDirection = 1;
									found = true;
									break;
								}
							} catch (Exception e) {
								
							}
						}
						emptyX++;
					}
					if (found) {
						xPos = emptyX;
						yPos = emptyY;
						break;
					}
					emptyX = 0;
					emptyY++;
				}
			} else {
				int choice = directions.get((int)(Math.random()*directions.size()));
				if (choice==0) {
					maze[yPos][xPos].left = true;
					lastDirection = 0;
					xPos--;
				} else if (choice==1) {
					maze[yPos][xPos].up = true;
					lastDirection = 1;
					yPos--;
				} else if (choice==2) {
					maze[yPos][xPos].right = true;
					lastDirection = 2;
					xPos++;
				} else if (choice==3) {
					maze[yPos][xPos].down = true;
					lastDirection = 3;
					yPos++;
				}
			}
		}
		for (int i=0; i<remove; i++) {
			int x = (int)(Math.random()*(MAZE_SIZE-2))+1;
			int y = (int)(Math.random()*(MAZE_SIZE-2))+1;
			maze[y][x].up = true;
			maze[y][x].down = true;
			maze[y][x].left = true;
			maze[y][x].right = true;
			maze[y+1][x].up = true;
			maze[y-1][x].down = true;
			maze[y][x+1].left = true;
			maze[y][x-1].right = true;
		}
	}
	public static void makeMaze(String seed) {
		int index = 0;
		for (int y=0; y<MAZE_SIZE; y++) {
			for (int x=0; x<MAZE_SIZE; x++) {
				maze[y][x] = new Cell(x, y);
				if ((seed.charAt(index)-65)/8==1) {
					maze[y][x].up = true;
				}
				if (((seed.charAt(index)-65)%8)/4==1) {
					maze[y][x].left = true;
				}
				if (((seed.charAt(index)-65)%4)/2==1) {
					maze[y][x].down = true;
				}
				if ((seed.charAt(index)-65)%2==1) {
					maze[y][x].right = true;
				}
				index++;
			}
		}
	}
	public static String getSeed() {
		String seed = "Seed: ";
		for (int y=0; y<MAZE_SIZE; y++) {
			for (int x=0; x<MAZE_SIZE; x++) {
				char character = 65;
				if (maze[y][x].up) {
					character+=8;
				}
				if (maze[y][x].left) {
					character+=4;
				}
				if (maze[y][x].down) {
					character+=2;
				}
				if (maze[y][x].right) {
					character+=1;
				}
				seed+=character;
			}
		}
		return seed;
	}
	public static Path pathFind(Point start, Point end){
		Queue<Path> paths = new LinkedList<Path>();
		boolean[][] visited = new boolean[MAZE_SIZE][MAZE_SIZE];
		paths.add(new Path(new ArrayList<Point>(), start));
		try {
			visited[start.y][start.x] = true;
		} catch (Exception e) {
			System.out.println(start);
			System.out.println(end);
			System.exit(0);
		}
		while (paths.size()>0) {
			Point check = new Point(getCell(paths.peek()).xPos, getCell(paths.peek()).yPos);
			if (check.equals(end)) {
				break;
			}
			if (getCell(paths.peek()).up&&!visited[getCell(paths.peek()).yPos-1][getCell(paths.peek()).xPos]) {
				paths.add(new Path(paths.peek().directions, new Point(getCell(paths.peek()).xPos, getCell(paths.peek()).yPos-1)));
				visited[getCell(paths.peek()).yPos-1][getCell(paths.peek()).xPos] = true;
			}
			if (getCell(paths.peek()).left&&!visited[getCell(paths.peek()).yPos][getCell(paths.peek()).xPos-1]) {
				paths.add(new Path(paths.peek().directions, new Point(getCell(paths.peek()).xPos-1, getCell(paths.peek()).yPos)));
				visited[getCell(paths.peek()).yPos][getCell(paths.peek()).xPos-1] = true;
			}
			if (getCell(paths.peek()).down&&!visited[getCell(paths.peek()).yPos+1][getCell(paths.peek()).xPos]) {
				paths.add(new Path(paths.peek().directions, new Point(getCell(paths.peek()).xPos, getCell(paths.peek()).yPos+1)));
				visited[getCell(paths.peek()).yPos+1][getCell(paths.peek()).xPos] = true;
			}
			if (getCell(paths.peek()).right&&!visited[getCell(paths.peek()).yPos][getCell(paths.peek()).xPos+1]) {
				paths.add(new Path(paths.peek().directions, new Point(getCell(paths.peek()).xPos+1, getCell(paths.peek()).yPos)));
				visited[getCell(paths.peek()).yPos][getCell(paths.peek()).xPos+1] = true;
			}
			paths.poll();
		}
		return paths.peek();
	}
	public static Cell getCell(Path p) {
		int x = p.directions.get(p.directions.size()-1).x;
		int y = p.directions.get(p.directions.size()-1).y;
		return maze[y][x];
	}
}
