package component;

import java.util.LinkedList;
import java.util.ListIterator;

import UI.Textbox;


public class PlayerData implements IComponent {
	public boolean typing = false;
	public LinkedList<String> command_histroy = new LinkedList<String>();
	public ListIterator<String> command_histroy_it;
	public Textbox txtbox = new Textbox();

}
