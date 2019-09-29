package component;

import main.LOGGER;
import network.WorldSocket;


public class EntityManager {

	public static final int AIID = 0;
	public static final int AnimationID = 1;
	public static final int AppearanceID = 2;
	public static final int AttributeID = 3;
	public static final int AudioID = 4;
	public static final int BroadCastID = 5;
	public static final int ChatID = 6;
	public static final int CollisionID = 7;
	public static final int HealthID = 8;
	public static final int HUDID = 9;
	public static final int InventoryID = 10;
	public static final int MovementID = 11;
	public static final int NetworkID = 12;
	public static final int PositionID = 13;
	public static final int SizeID = 14;
	public static final int SpeechID = 15;
	public static final int VelocityID = 16;
	public static final int PlayerDataID = 17;

	public static final int MAXCOMPONENTS = 18;

	public static final int MAXENTITIES = 100;
	public boolean[] takenIdx = new boolean[MAXENTITIES];
	public int[] entities = new int[MAXENTITIES];
	public int entityCount = 0;
	
	public int cameraFocusEntityID = 0;
	
	public component.IComponent[][] components = new IComponent[MAXENTITIES][MAXCOMPONENTS];

	private static EntityManager instance = null;

	public static EntityManager getInstance() {
		if (instance == null) {
			instance = new EntityManager();
		}
		return instance;
	}

	public void addEntityComponent(int id, int compoenntID, IComponent newcomp) {
		components[id][compoenntID] = newcomp;
	}

	public int addEntity(int requestedHandle, String name , int entityTextureType, float x, float y,
			boolean facing, int AIType, WorldSocket worldSocket) throws Exception {

		int id = createID(requestedHandle);
		LOGGER.info("created Entity with id: " + requestedHandle, this);

		components[id][AnimationID] = new Animation(entityTextureType, Animation.IDLE);
		components[id][PositionID] = new Position();
		
		components[id][SizeID] = new Size();

		components[id][AppearanceID] = new Appearance();
		components[id][AttributeID] = new Attribute(name);
		components[id][HealthID] = new Health();
		components[id][BroadCastID] = new Broadcast(id,worldSocket);
		
		components[id][VelocityID] = new Velocity(0,0);
		components[id][MovementID] = new Movement();
		components[id][InventoryID] = new Inventory();
		components[id][CollisionID] = new Collision();
		
		if (AItypes.playercontrolled.ordinal() == AIType)
			components[id][NetworkID] = new Network();
		else {
			components[id][AIID] = new AI(AIType);
		}
		
		//register
		((Position)components[id][PositionID]).init(id,x, y);
/*
		if (AIType == AItypes.playercontrolled.ordinal()) {
			components[id][NetworkID] = new Network();
		} else {
			components[id][AIID] = new AI(AIType);
		}
	*/	
		/*
		if (AIType == AItypes.AIcontrolled.ordinal()) {
			components[id][PhysicsID] = new Inventory();
		}
		*/
	
		
		return id;
	}

	private int createID(int requested_id) throws Exception {
		if (entityCount == MAXENTITIES) {
			throw new Exception("cannot create new entity, max entities reached");
		}
		if (requested_id >= MAXENTITIES) {
			throw new Exception("cannot create new entity with id larger than MAXENTITIES");
		}

		if (requested_id >= 0) {

			takenIdx[requested_id] = true;
			entities[entityCount] = requested_id;
			entityCount++;
			return requested_id;
		}

		// get new id
		int id = 1;
		while (takenIdx[id]) {
			id++;
		}
		// take the entity
		takenIdx[id] = true;
		entities[entityCount] = id;
		entityCount++;

		return id;
	}

	public void removeEntity(int id) throws Exception {

		Appearance apperanceComponent = (Appearance) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.AppearanceID);
		if (apperanceComponent != null) {
			apperanceComponent.setFadeOut();
		} else {
			deleteEntity(id);
		}
	}

	public void deleteEntity(int id) throws Exception {
		if (!takenIdx[id]) {
			throw new Exception("Tried to remove non existing entity");
		}
		takenIdx[id] = false;
		for (int i = 0; i < MAXCOMPONENTS; i++) {
			components[id][i] = null;
		}
		entityCount--;
		int i = 0;
		while (entities[i] != id) {
			i++;
		}
		entities[i] = entities[entityCount];
	}

	public IComponent getEntityComponent(int id, int componentID) {
		return components[id][componentID];
	}

	public void removeEntityComponent(int id, int componentID) {
		components[id][componentID] = null;

	}

	public void update() {
		for (int i = 0; i < entityCount; i++) {
			int id = entities[i];
			update(id);
		}
	}

	public void update(int id) {
		/*
		Position positionComponent = (Position) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.PositionID);
		
		if (positionComponent != null) {
			System.out.println("id: " + id +  "x: " + positionComponent.x + " y: " + positionComponent.y);
		}
		
		*/
		componentsSystems.AnimationSystem.update(id);
		componentsSystems.PhysicsSystem.update(id);
		componentsSystems.AISystem.update(id);
		componentsSystems.NetoworkSystem.update(id);
		componentsSystems.BroadCastSystem.update(id);
		componentsSystems.InputSystem.update(id);
		componentsSystems.RegenPower.update(id);
		
	}

	public void focusCameraonEntity(int id) {
		cameraFocusEntityID = id;
	}


	public void setMaxSpeed(int id, float value) {
		((Movement) components[id][MovementID]).maxSpeed = value;
	}

	public void setCurrentAction(int id, int newaction) {

		if (((Animation) components[id][AnimationID]).currentPlayingAction != newaction) {
			((Animation) components[id][AnimationID]).setAnimation(newaction);
		}
	}

	public int getCurrentAction(int id) {
		return ((Animation) components[id][AnimationID]).currentPlayingAction;
	}


	public void say(int id, String s) {
		if (components[id][SpeechID] == null)
			components[id][SpeechID] = (IComponent) new Speech(s);
		else {
			((Speech) components[id][SpeechID]).setSpeech(s);
		}
	}

	public void setattack(int id) {

		Movement movementComponent = (Movement) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.MovementID);
		if (movementComponent == null)
			return;

		movementComponent.scratching = true;

	}

	public void setattack2(int id) {
		/*
		Movement movementComponent = (Movement) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.MovementID);
		if (movementComponent == null)
			return;

		movementComponent.firing = true;
		Position positionComponent = (Position) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.PositionID);

		Animation animationComponent = (Animation) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.AnimationID);

		
		Session.getInstance()
				.SendCommand(new WorldPacket(WorldPacket.SPAWN,
						new NetworkSpawner("", -1, Spawner.FIREBALL,
								positionComponent.x + (30 * (animationComponent.facingRight ? 1 : -1)),
								positionComponent.y, animationComponent.facingRight, 2)));
	*/
	}

	public void die(int id) {
		Attribute attributeComponent = (Attribute) EntityManager.getInstance().getEntityComponent(id,
				EntityManager.AttributeID);
		if (attributeComponent == null)
			return;
		
		attributeComponent.isDead = true;
			
		
	}

	public void destroy() {
		cameraFocusEntityID = 0;
		while (this.entityCount > 0) {
			try {
				this.deleteEntity(this.entities[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
