package component;

import java.awt.Font;

public class Speech implements component.IComponent {

	public Speech(String s) {
		setSpeech(s);
	}
	
	public void setSpeech(String s) {
		start_time = System.currentTimeMillis();
		txt= s;
	}
	
	public String txt = "";
	public long start_time = 0;
	public long duration = 5000;
	public Font font = new Font("Arial", Font.PLAIN,11);;
}
