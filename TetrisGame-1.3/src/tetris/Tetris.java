package tetris;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;


public class Tetris extends JFrame {
	private GameView game;

	public Tetris() {
		Init();
	}

	public void Init() {
		game = new GameView();
		game.GameViewInit();
		getContentPane().add(game);

		// 鍵盤事件處理
		addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (game != null) {
					game.keyCode(e.getKeyCode());
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Tetris tetris = new Tetris();
		tetris.setTitle("俄羅斯方塊");
		tetris.setBackground(Color.black);
		tetris.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tetris.setResizable(true);//
		tetris.setVisible(true);
		tetris.setSize(350, 500);
		tetris.setLocation(350, 50);

	}

}
