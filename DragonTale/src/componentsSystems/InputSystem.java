package componentsSystems;

import PACKET.NetworkSpawner;
import PACKET.SpeechPacket;
import PACKET.WorldPacket;
import componentNew.Animation;
import componentNew.EntityManager;
import componentNew.Movement;
import componentNew.PlayerData;
import componentNew.Position;
import main.CONTROLS;
import network.Session;

public class InputSystem {


		public static void update(int id) {
			
			PlayerData playerDataComponent = (PlayerData) EntityManager.getInstance().getEntityComponent(id,
					EntityManager.PlayerDataID);
			
			
			if (playerDataComponent == null) {
				return;
			}
			
			handleInput(id, playerDataComponent);
			/*
			Position positionComponent = (Position) EntityManager.getInstance().getEntityComponent(id,
					EntityManager.PositionID);
			if (positionComponent == null) {
				return;
			}
			World.getInstance().tm.setPosition(GameConstants.WIDTH / 2 - positionComponent.x, GameConstants.HEIGHT / 2 - positionComponent.y);
			*/
			
		}
		
		public static void handleInput(int id, PlayerData playerDataComponent ) {

			if (CONTROLS.isPressed(CONTROLS.ENTER)) {
				if (!playerDataComponent.typing) {
					playerDataComponent.command_histroy_it = null;
					playerDataComponent.typing = true;
					//entity.renders.add(chatboxcontrol);
					//EntityManager.getInstance().addEntityComponent(id,EntityManager.ChatID, new Chat());
					CONTROLS.focused_control = playerDataComponent.txtbox;
					//this.txtbox.focus();
					playerDataComponent.txtbox.text = "";
					//CONTROLS.setCaptureText(true);
				} else {
					String command = playerDataComponent.txtbox.text;// CONTROLS.getCapturedText();
					playerDataComponent.typing = false;
					
					//EntityManager.getInstance().removeEntityComponent(id, EntityManager.ChatID);
					//entity.renders.remove(chatboxcontrol);
					CONTROLS.focused_control = null;
					//this.txtbox.unfocus();
					//CONTROLS.setCaptureText(false);
					if (command.compareTo("") != 0) {

						playerDataComponent.command_histroy.push(command);
						if (playerDataComponent.command_histroy.size() > 10) {
							playerDataComponent.command_histroy.removeLast();
						}
						switch (command.charAt(0)) {
						case '.':
							execute_server_command(id,command.substring(1, command.length()));
							break;
						case '/':
							execute_user_command(command.substring(1, command.length()));
						default:
							say(id, command);
							break;
						}
					}
				}
			}

			if (playerDataComponent.typing) {
				if (CONTROLS.isPressed(CONTROLS.UP)) {
					if (playerDataComponent.command_histroy_it == null)
						playerDataComponent.command_histroy_it = playerDataComponent.command_histroy.listIterator();
					if (playerDataComponent.command_histroy_it.hasNext()) {
						playerDataComponent.txtbox.text = (String) playerDataComponent.command_histroy_it.next();
						//CONTROLS.setText((String) command_histroy_it.next());
					} else {
						playerDataComponent.command_histroy_it = playerDataComponent.command_histroy.listIterator();
						if (playerDataComponent.command_histroy_it.hasNext()) {
							playerDataComponent.txtbox.text = (String) playerDataComponent.command_histroy_it.next();
							//CONTROLS.setText((String) command_histroy_it.next());
						}
					}
				}

				return;
			}

			
			Movement movementComponent=(Movement) EntityManager.getInstance().getEntityComponent(id, EntityManager.MovementID);
			if (movementComponent == null)
				return;
			
			
			movementComponent.right = (CONTROLS.keyState[CONTROLS.RIGHT]);
			movementComponent.left = (CONTROLS.keyState[CONTROLS.LEFT]);
			movementComponent.setJumping(id,CONTROLS.keyState[CONTROLS.JUMP]);
			movementComponent.gliding =  (CONTROLS.keyState[CONTROLS.GLIDE]);

			if (CONTROLS.isPressed(CONTROLS.SCRATCH)) {
				EntityManager.getInstance().setattack(id);
				
			}
			if (CONTROLS.isPressed(CONTROLS.FIREBALL)) {
				EntityManager.getInstance().setattack2(id);
				
			}

		}

		public static void execute_server_command(int id, String command) {
			
			String[] args = command.split(" ");
			switch (args[0]) {
			case "spawn":
				Position positionComponent = (Position) EntityManager.getInstance().getEntityComponent(id,
						EntityManager.PositionID);
				
				Animation animationComponent = (Animation) EntityManager.getInstance().getEntityComponent(id,
						EntityManager.AnimationID);
				
				Session.getInstance().SendCommand(new WorldPacket(
						WorldPacket.SPAWN, new NetworkSpawner("-1", -1, Integer.parseInt(args[1]),
								positionComponent.x + (30 * (animationComponent.facingRight ? 1 : -1)), positionComponent.y,
								animationComponent.facingRight, 0)));
				//TODO: the network 0 should depend on the type of thing created
				break;
			case "setspeed":
				Movement movemenetComponent = (Movement) EntityManager.getInstance().getEntityComponent(id,
						EntityManager.MovementID);
				
				movemenetComponent.maxSpeed = (Float.parseFloat(args[1]) / 100.0f * movemenetComponent.maxSpeed);
				break;
			default:
				break;
			}
			
		}

		public static void say(int id, String command) {
			//if (Session.getInstance().  == null)
			//	entity.say(command);
			//else
			Session.getInstance().SendCommand(new WorldPacket(WorldPacket.SPEECH, new SpeechPacket(id, command)));
		}

		public static void execute_user_command(String command) {

		}


}
