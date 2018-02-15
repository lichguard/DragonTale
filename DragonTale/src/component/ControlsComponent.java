package component;
import java.util.LinkedList;
import java.util.ListIterator;
import DesertAdventures.CONTROLS;
import DesertAdventures.Gameplay;
import DesertAdventures.World;
import Entity.ENTITY;
import Network.Session;
import PACKET.CommandPacket;
import PACKET.NetworkSpawner;
import PACKET.SpeechPacket;

public class ControlsComponent implements IComponent{

	World world;
	ENTITY entity;
	Session session;
	public boolean typing = false;
	public LinkedList<String> command_histroy = new LinkedList<String>();
	public ListIterator<String> command_histroy_it;
	
	public ControlsComponent(World world,ENTITY entity,Session session )
	{
		this.world =world;
		this.entity=entity;
		this.session=session;
	}
	
	@Override
	public void update() {
		update2();
	}
	
	public void handleInput() {

		if (CONTROLS.isPressed(CONTROLS.ENTER)) {
			if (!typing) {
				command_histroy_it = null;
				typing = true;
				CONTROLS.setCaptureText(true);
			} else {
				String command = CONTROLS.getCapturedText();
				typing = false;
				CONTROLS.setCaptureText(false);
				if (command.compareTo("") != 0) {

					command_histroy.push(command);
					if (command_histroy.size() > 10) {
						command_histroy.removeLast();
					}
					switch (command.charAt(0)) {
					case '.':
						execute_server_command(command.substring(1, command.length()));
						break;
					case '/':
						execute_user_command(command.substring(1, command.length()));
					default:
						say(command);
						break;
					}
				}
			}
		}

		if (typing) {
			if (CONTROLS.isPressed(CONTROLS.UP)) {
				if (command_histroy_it == null)
					command_histroy_it = command_histroy.listIterator();
				if (command_histroy_it.hasNext()) {
					CONTROLS.setText((String) command_histroy_it.next());
				} else {
					command_histroy_it = command_histroy.listIterator();
					if (command_histroy_it.hasNext()) {
						CONTROLS.setText((String) command_histroy_it.next());
					}
				}
			}

			return;
		}

		entity.setRight(CONTROLS.keyState[CONTROLS.RIGHT]);
		entity.setLeft(CONTROLS.keyState[CONTROLS.LEFT]);
		entity.setJumping(CONTROLS.keyState[CONTROLS.JUMP]);
		entity.setGliding(CONTROLS.keyState[CONTROLS.GLIDE]);

		if (CONTROLS.isPressed(CONTROLS.SCRATCH)) {
			//entity.setattack2();
		}
		if (CONTROLS.isPressed(CONTROLS.FIREBALL)) {
			//entity.setattack();
		}

	}

	public void execute_server_command(String command) {
		String[] args = command.split(" ");
		switch (args[0]) {
		case "spawn":
			session.SendCommand(new CommandPacket(CommandPacket.SPAWN,
					new NetworkSpawner(-1, Integer.parseInt(args[1]),
							entity.getx() + (30 * (entity.facingRight ? 1 : -1)),
							entity.gety(), entity.facingRight, false)));
			break;
		case "setspeed":
			entity.setMaxSpeed(Float.parseFloat(args[1]) / 100.0f * entity.maxSpeed);
			break;

		default:
			break;
		}
	}

	public void say(String command) {
		session.SendCommand(new CommandPacket(CommandPacket.SPEECH, new SpeechPacket(entity.handle, command)));
	}

	public void execute_user_command(String command) {

	}

	public void update2() {

		handleInput();
		world.tm.setPosition(Gameplay.WIDTH / 2 - entity.getx(),
				Gameplay.HEIGHT / 2 - entity.gety());

		/*
		if (!entity.flinching) {
			ArrayList<ENTITY> entities = world.getCollisions(this.entity);
			for (ENTITY enemy : entities) {
				//if (enemy.type !=  1)
				//	entity.gethit(1, (Enemy) enemy);

			}
		}
		*/
	}


}
