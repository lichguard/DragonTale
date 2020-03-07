package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		// System.setProperty("sun.java2d.opengl", "true");
		JFrame obj = new JFrame();
		Gameplay gameplay = new Gameplay();
		Config.loadConfig();
		obj.setBounds(10, 10, GameConstants.WIDTH, GameConstants.HEIGHT);
		obj.add(gameplay);
		// obj.pack();
		obj.setTitle("Dragon Tale");
		obj.setResizable(false);
		obj.setVisible(true);
		// obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		obj.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				gameplay.stop();
				try {
					Config.writeConfig();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		gameplay.start();

	}

}
