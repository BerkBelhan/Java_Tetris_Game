import java.awt.Color;
import java.awt.Graphics;

public class Shape {
	private int x = 4,y = 0;
	private int normal = 900;
	private int fast = 30;
	private int delayTimeForShapesMovement = normal;
	private long beginTime; 
	public static final int BOARD_WIDTH = 10;
	public static final int BOARD_HEIGHT = 20;
	public static final int BLOCK_SIZE = 30;
	
	private int deltaX = 0;
	private boolean collision = false; 
	private int[][] coords;
	private Board board;
	private Color color;
	
	
	public Shape(int[][] coords, Board board, Color color) {
		this.coords = coords;
		this.board = board;
		this.color = color;
	}
	
	public void setX (int x ) {
		this.x = x;
	}
	public void setY (int y ) {
		this.y = y;
	}
	public void reset() {
		this.x = 4;
		this.y = 0;
		collision = false;
	}
	
	private int counter = 0;
	
	//Fills color for the board
	public void update() {
		if(collision) {
			//i added a counter to reset their mv.speed because after 5-6 shapes fall, they were moving down so fast so i set a counter to track everytime.
			counter++;
			 if(counter % 1 == 0) {
		            delayTimeForShapesMovement = normal;
		        }
			for(int row = 0; row<coords.length; row++) {
				for(int col= 0; col<coords[0].length; col++) {
					if(coords[row][col] != 0) {
						board.getBoard()[y + row][x + col] = color;
					}
				}
			}
			board.setCurrentShape();
			checkLine();
			reset();
			board.addScore();
			return;
		}
		boolean moveX = true;
		
	

	if (!(x + deltaX + coords[0].length > BOARD_WIDTH) && !(x + deltaX < 0)) {
		for(int row = 0; row< coords.length; row++) {
			for(int col = 0; col< coords[row].length; col++) {
				if(coords[row][col] != 0) {
				if(board.getBoard()[y+row][x+deltaX+col] != null) {
					moveX = false;
				   }
				}
			}
		}
		if(moveX) {
		 x += deltaX;
		}
	}//controls whether the shape is in the matrix(above)
	deltaX = 0;
	
	if(System.currentTimeMillis()- beginTime > delayTimeForShapesMovement) {
		// vertical movement
		if(!(y  + 1 + coords.length > BOARD_HEIGHT)) {
			for(int row = 0; row<coords.length; row++) {
				for(int col = 0; col<coords[row].length; col++) {
					if(coords[row][col] != 0) {
						if(board.getBoard()[y+1+row][x+deltaX+col] !=null) {
							collision = true;
						}
						
					}
					
				}
			}
			if(!collision) {
			y++;
			}
			
		}else {
			collision = true;
		}//checks if it reaches the bottom of the matrix field or collides with another shape.
		beginTime = System.currentTimeMillis();
		}
}
	//Full line checker, checks if the line is fulfitted with tetrominoes. If it is it removes the fulfitted line and 
	//drops the topLine to the removed line 
	private void checkLine() {
		int bottomLine = board.getBoard().length - 1;
		for(int topLine = board.getBoard().length - 1; topLine > 0; topLine--) {
			int count = 0;
			for(int col = 0; col< board.getBoard()[0].length; col++) {
				if(board.getBoard()[topLine][col] != null) {
					count++;
				}
		      board.getBoard()[bottomLine][col] = board.getBoard()[topLine][col];
			}
			if(count < board.getBoard()[0].length) {
				bottomLine--;
			}else {
				board.addScore();// Increment the score by 10 for each line cleared
			
				
			}
		}
	}
	public void rotation() {
	    int[][] rotatedShape = transposeMatrix(coords);
	    reverseRow(rotatedShape);

	    // Check if the rotation would go out of bounds
	    if ((x + rotatedShape[0].length > BOARD_WIDTH) || (y + rotatedShape.length > BOARD_HEIGHT)) {
	        return;
	    }

	    // Check for collision with other shapes before rotation
	    for (int row = 0; row < rotatedShape.length; row++) {
	        for (int col = 0; col < rotatedShape[row].length; col++) {
	            if (rotatedShape[row][col] != 0) {
	                if (board.getBoard()[y + row][x + col] != null) {
	                    return;
	                }
	            }
	        }
	    }

	    coords = rotatedShape;
	}
	
	
	
	public int[][] transposeMatrix(int [][] matrix){
		int [][] temp = new int [matrix[0].length][matrix.length];
		for(int row = 0; row < matrix.length; row++) {
			for(int col = 0; col< matrix[0].length; col++){
				temp[col][row]= matrix[row][col];
				
			}
		}return temp;
	}
			
			
			
	private void reverseRow(int[][] matrix) {
		int middle = matrix.length / 2;
		for(int row = 0; row < middle; row++) {
			int[] temp = matrix[row];
			matrix[row] = matrix[matrix.length-row-1 ];
			matrix[matrix.length-row-1] = temp;
		}
		
	}

	
	public void render(Graphics g) {
		//draws the shape
		for (int row = 0; row < coords.length; row++) {
			for (int col = 0; col < coords[0].length; col++) {
				if(coords[row][col] != 0) {
				g.setColor(color);
				g.fillRect(col*BLOCK_SIZE + x * BLOCK_SIZE, row * BLOCK_SIZE + y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				}
			}
		}
		
	}
	public int[][] getCoords() {
        return coords;
    }
	
	public void speedUp() {
		delayTimeForShapesMovement = fast;
	}
	public void speedN() {
	    delayTimeForShapesMovement = normal;
	}
	public void goRight() {
		deltaX=1;
	}
	public void goLeft() {
		deltaX=-1;
	}
	public int getY() {
		return y;
	}
	public int getX() {
		return x;
	}
	public Color getColor() {
	    return color;
	}//connected within the nextShape method.
	
	
	


}
