import javax.swing.JFrame;


public class TetrisGame {
	private static final int WIDTH = 445; 
	private static final int HEIGHT  = 629;
	private JFrame window;
	private Board board;
	
	public TetrisGame() {
		window = new JFrame("TetrisGame");
		window.setSize(WIDTH, HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		
		board = new Board();
		window.add(board);
		window.addKeyListener(board);
		window.setVisible(true);
		
	}
	
	public static void main (String[]args) {
		new TetrisGame();
		
		
		
		
		
	}
	
}




  
  
     
      










