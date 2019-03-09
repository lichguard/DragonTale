package main;

import javax.swing.JFrame;
public class Main {

	public static void main(String[] args) {
			//System.setProperty("sun.java2d.opengl", "true");
			JFrame obj = new JFrame();
			Gameplay gameplay = new Gameplay();
			obj.setBounds(10, 10, GameConstants.WIDTH, GameConstants.HEIGHT);
			obj.add(gameplay);
			//obj.pack();
			obj.setTitle("Dragon Tale");
			obj.setResizable(false);
			obj.setVisible(true);
			obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//obj.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			
	}

}
