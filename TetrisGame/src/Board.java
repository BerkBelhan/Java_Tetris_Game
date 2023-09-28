import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Board extends JPanel implements KeyListener{
	
	public static int STATE_GAME_PLAY = 0;
	public static int STATE_GAME_PAUSE = 1;
	public static int STATE_GAME_OVER = 2;
	
	private int state = STATE_GAME_PLAY;
	
	
	private static int FPS = 60;
	private static int delay = 1000/FPS;
	
	
	public static final int BOARD_WIDTH = 10;
	public static final int BOARD_HEIGHT = 20;
	public static final int BLOCK_SIZE = 30;
	private Timer looper;
	
	
	private int score = 0;
	
	private boolean gameOver = false;// i decleared a varibale to keep a track of the game state. 
	
	
	private Color[][] board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
	
	private Color[] colors = {Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"), 
	        Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc")};
	private Random random;
	private Shape[] shapes = new Shape[7];
	private Shape currentShape, nextShape;
			
	
	public Board() {
		random = new Random();
		
		// create shapes
        shapes[0] = new Shape(new int[][]{
            {1, 1, 1, 1} // I shape;
        }, this, colors[0]);

        shapes[1] = new Shape(new int[][]{
            {1, 1, 1},
            {0, 1, 0}, // T shape;
        }, this, colors[1]);

        shapes[2] = new Shape(new int[][]{
            {1, 1, 1},
            {1, 0, 0}, // L shape;
        }, this, colors[2]);

        shapes[3] = new Shape(new int[][]{
            {1, 1, 1},
            {0, 0, 1}, // J shape;
        }, this, colors[3]);

        shapes[4] = new Shape(new int[][]{
            {0, 1, 1},
            {1, 1, 0}, // S shape;
        }, this, colors[4]);

        shapes[5] = new Shape(new int[][]{
            {1, 1, 0},
            {0, 1, 1}, // Z shape;
        }, this, colors[5]);

        shapes[6] = new Shape(new int[][]{
            {1, 1},
            {1, 1}, // O shape;
        }, this, colors[6]);
        

        currentShape = shapes[random.nextInt(shapes.length)];
        nextShape = shapes[random.nextInt(shapes.length)];
	
		
		
		
		
		
		looper = new Timer(delay, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
				repaint();
				// it will redraw the shape  
				
			}
		});
		looper.start();
		
		
		
	}
	private void update() {
		 
		currentShape.update();
		}
	
		
		
			
		
		
		
	
	
	//this is for setting the current shape and the next shape drawing.
	public void setCurrentShape() {
		if(gameOver) {
		return;	// stops spawning new shapes if the game is over.
		//With this change, when the game is in the "STATE_GAME_OVER" state,
		//the method will exit immediately without setting the current shape to a new shape, preventing any new shapes from being spawned.
		}
	 currentShape = nextShape;
	 nextShape = shapes[random.nextInt(shapes.length)]; 
	 currentShape.reset();
	 checkOverGame();	
	 repaint();
	}
	private void drawNextShape(Graphics g) {
	    
		 // draw the label for the next shape
	    g.setColor(Color.BLACK);
	    g.setFont(new Font("Georgia", Font.BOLD, 18));
	    g.drawString("NEXT SHAPE:", 300, 100);
	    
	    int[][] coords = nextShape.getCoords();
	    for (int row = 0; row < coords.length; row++) {
	        for (int col = 0; col < coords[0].length; col++) {
	            if (coords[row][col] != 0) {
	                g.setColor(nextShape.getColor());
	                g.fillRect(300 + col * BLOCK_SIZE, 150 + row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
	            }
	        }
	    }
	}

	
	
	
	private void checkOverGame() {
		int [][] coords = currentShape.getCoords();
		for(int row = 0; row < coords.length; row++ ) {
			for(int col = 0; col < coords[row].length; col++) {
				if(coords[row][col] != 0) {
					if(board[row+currentShape.getY()][col+ currentShape.getX()] != null) {
						state = STATE_GAME_OVER;
						gameOver = true;
					}
				}
			}
		}
	}
	
		
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		currentShape.render(g);
		drawNextShape(g);
		
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				
				if(board[row][col] != null) {
				g.setColor(board[row][col]);
				g.fillRect(col*BLOCK_SIZE, row*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				}
			}
		}//draws the score counter
		    g.setColor(Color.BLACK);
		    g.setFont(new Font("Georgia", Font.BOLD, 20));
		    g.drawString("SCORE: " + score, 300, 50);

		// draws the board 
		g.setColor(Color.BLACK);
		for(int row = 0; row< BOARD_HEIGHT; row++) {
			g.drawLine(0, BLOCK_SIZE*row, BLOCK_SIZE*BOARD_WIDTH, BLOCK_SIZE*row);
		}
		for(int col = 0; col< BOARD_WIDTH + 1; col++) {
			g.drawLine(col*BLOCK_SIZE, 0, col*BLOCK_SIZE, BLOCK_SIZE*BOARD_HEIGHT);
		}
	    if(state == STATE_GAME_OVER) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Georgia", Font.BOLD, 45));
		g.drawString("GAME OVER", 50, 200);
	    }
	    if(state == STATE_GAME_PAUSE) {
	    	g.setColor(Color.BLACK);
	    	g.setFont(new Font("Georgia", Font.BOLD, 45));
			g.drawString("PAUSED", 50, 200);
	    }
		
		
}
	public Color[][] getBoard(){
		return board;
	}


	
	
	

	// In here the shape goes down fast if down button is being pressed 
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentShape.speedUp();
		}else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			currentShape.goRight();
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			currentShape.goLeft();
		}else if(e.getKeyCode() == KeyEvent.VK_UP) {
			currentShape.rotation();
		}
		
	
		//CLEANS THE BOARD. RESTARTS THE GAME 
		if(state == STATE_GAME_OVER || state== STATE_GAME_PAUSE ) {
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				currentShape.rotation();
				for (int row = 0; row < board.length; row++) {
					for (int col = 0; col < board[row].length; col++) {
						board[row][col] = null;
					
						
					}
				}
				 setCurrentShape();
				 state = STATE_GAME_PLAY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_P) {
		    if (state == STATE_GAME_PLAY) {
		        state = STATE_GAME_PAUSE;
		        looper.stop(); // Pause the Timer
		    } else if (state == STATE_GAME_PAUSE) {
		        state = STATE_GAME_PLAY;
		        looper.start(); // Resume the Timer
		    }
		    repaint();
		}
		
	
	}   
		


// In here the shape goes down with normal speed if down button is being released
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentShape.speedN();
		}
	}
	public void addScore() {
		if(!gameOver) {
		score += 10;
		}
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
