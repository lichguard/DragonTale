package main;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//this class contains a boolean array of current and previous key states
//for the 10 keys that are used for this game.
//a key k is down when keyState[k] is true.

public class Controls implements KeyListener, MouseListener {
	
	public static final int NUM_KEYS = 14;
	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];
	public static int RIGHT = 0;
	public static int LEFT = 1;
	public static int JUMP = 2;
	public static int SCRATCH = 3;
	public static int FIREBALL = 4;
	public static int GLIDE = 5;
	public static int ENTER = 6;
	public static int UP = 7;
	public static int DOWN = 8;
	public static int ESCAPE = 9;
	public static int TAB = 10;
	public static int RMB = 11;
	public static int LMB = 12;
	public static int MMB = 13;
	public static int mousex = 0;
	public static int mousey = 0;

	public static void Mouseset(MouseEvent e,boolean b)
	{
		if(e.getButton() == MouseEvent.BUTTON1)  keyState[LMB] = b ; //Last_time[RIGHT] = System.nanoTime();}
		else if(e.getButton() == MouseEvent.BUTTON3) keyState[RMB] =b;
		else if(e.getButton() == MouseEvent.BUTTON2) keyState[MMB] =b;
		


		mousex = e.getX();
		mousey = e.getY();
		
	}
	
	public static void keySet(KeyEvent key, int i, boolean b) {
		if(i == KeyEvent.VK_RIGHT)  keyState[RIGHT] = b ; //Last_time[RIGHT] = System.nanoTime();}
		else if(i == KeyEvent.VK_LEFT) keyState[LEFT] = b;
		else if(i == KeyEvent.VK_SPACE) keyState[JUMP] = b;
		else if(i == KeyEvent.VK_R) keyState[SCRATCH] = b;
		else if(i == KeyEvent.VK_F) keyState[FIREBALL] = b;
		else if(i == KeyEvent.VK_E) keyState[GLIDE] = b;
		else if(i == KeyEvent.VK_ENTER) keyState[ENTER] = b;
		else if(i == KeyEvent.VK_UP) keyState[UP] = b;
		else if(i == KeyEvent.VK_DOWN) keyState[DOWN] = b;
		else if(i == KeyEvent.VK_ESCAPE) keyState[ESCAPE] = b;
		else if(i == KeyEvent.VK_TAB) keyState[TAB] = b;
/*
		if (focused_control != null)
		{
			focused_control.KeyBoardEvent(key,i,b);
		}
	*/	
		/*
		if (captureText & b)
		{
			if (i == KeyEvent.VK_BACK_SPACE && capturedText.length() > 0)
				capturedText.deleteCharAt(capturedText.length()-1);
			else
				if (key.getKeyChar() >= 32 && key.getKeyChar() <= 126)
				capturedText.append(key.getKeyChar());
		}
		*/
		
	}
	/*
	public static String getCapturedText()
	{
		return capturedText.toString();
	}
	public static void setText(String text)
	{
		capturedText = new StringBuilder(text);
	}
	public static void clearCapturedText()
	{
		capturedText = new StringBuilder();
	}
	*/
	/*
	public static void setCaptureText(boolean capture)
	{
		captureText = capture;
		clearCapturedText();
	}
	*/
	public static void update() {
		for(int i = 0; i < NUM_KEYS; i++) {
			prevKeyState[i] = keyState[i];
		}
	}
	
	public static boolean isPressed(int i) {
		return keyState[i] && !prevKeyState[i];
	}
	
	public static boolean isdoublePressed(int i) {
		return false;
		//return ((System.nanoTime() -Last_time[i] < 10000000) && keyState[i] && !prevKeyState[i]);
	}
	
	public static boolean anyKeyPress() {
		for(int i = 0; i < NUM_KEYS; i++) {
			if(keyState[i]) return true;
		}
		return false;
	}

	@Override
	public void keyPressed(KeyEvent key) {
		Controls.keySet(key, key.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent key) {
		Controls.keySet(key, key.getKeyCode(), false);

	}

	@Override
	public void keyTyped(KeyEvent key) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		Controls.Mouseset(e,true);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		Controls.Mouseset(e,false);
	}

	
}
