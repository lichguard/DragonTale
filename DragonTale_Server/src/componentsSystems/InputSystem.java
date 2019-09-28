package componentsSystems;

import componentNew.EntityManager;
import componentNew.PlayerData;


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

	

		}

		public static void execute_server_command(int id, String command) {
			/*
			String[] args = command.split(" ");
			switch (args[0]) {
			case "spawn":
				
				Position positionComponent = (Position) EntityManager.getInstance().getEntityComponent(id,
						EntityManager.PositionID);
				
				Animation animationComponent = (Animation) EntityManager.getInstance().getEntityComponent(id,
						EntityManager.AnimationID);
				*/
				/*
				Session.getInstance().SendCommand(new WorldPacket(
						WorldPacket.SPAWN, new NetworkSpawner("-1", -1, Integer.parseInt(args[1]),
								positionComponent.x + (30 * (animationComponent.facingRight ? 1 : -1)), positionComponent.y,
								animationComponent.facingRight, 0)));
				//TODO: the network 0 should depend on the type of thing created
				*/
				/*
				break;
			case "setspeed":
				Movement movemenetComponent = (Movement) EntityManager.getInstance().getEntityComponent(id,
						EntityManager.MovementID);
				
				movemenetComponent.maxSpeed = (Float.parseFloat(args[1]) / 100.0f * movemenetComponent.maxSpeed);
				break;
			default:
				break;
			}
			*/
		}

		public static void say(int id, String command) {
			//if (Session.getInstance().  == null)
			//	entity.say(command);
			//else
			//Session.getInstance().SendCommand(new WorldPacket(WorldPacket.SPEECH, new SpeechPacket(id, command)));
		}

		public static void execute_user_command(String command) {

		}


}
