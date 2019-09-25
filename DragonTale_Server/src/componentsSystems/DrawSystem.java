package componentsSystems;
import java.awt.Color;
import java.awt.Graphics;
import componentNew.*;
import main.GameConstants;

public class DrawSystem {
	
	public static void draw(int id,Graphics g) {
		
		Attribute attributeComponent = (Attribute) 
				EntityManager.getInstance().getEntityComponent(id, EntityManager.AttributeID);
		
		if (attributeComponent == null || !attributeComponent.inWorld)
			return;
		
		Position positionComponent=(Position) EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID);
		if (positionComponent == null || positionComponent.notonScreen())
			return;
		
		
		chatbox(id,g);
		animation(id,g);
		hud(id,g);
		name(id,g);
		nameplate(id,g);
		speech(id,g);
	}
	
	
	private static void chatbox(int id,Graphics g) {
		/*
		Chat chatComponent = (Chat) EntityManager.getInstance().getEntityComponent(id, EntityManager.ChatID);
		if (chatComponent == null)
			return;
		*/
		/*
		PlayerData playerDataComponent = (PlayerData) EntityManager.getInstance().getEntityComponent(id, EntityManager.PlayerDataID);
		if (playerDataComponent == null || !playerDataComponent.typing)
			return;

		
		g.setFont(playerDataComponent.txtbox.font);
		g.setColor(Color.black);
		g.fillRect(17, GameConstants.HEIGHT - 60, 120, 15);
		g.setColor(Color.white);
		g.drawString(playerDataComponent.txtbox.text , 20, GameConstants.HEIGHT - 50);
		*/
	}
	
	private static void animation(int id,Graphics g) {
		Animation animationComponent = (Animation) EntityManager.getInstance().getEntityComponent(id, EntityManager.AnimationID);
		if (animationComponent == null)
			return;
		
		Appearance apperanceComponent = (Appearance) EntityManager.getInstance().getEntityComponent(id, EntityManager.AppearanceID);
		if (apperanceComponent == null)
			return;
		
		Position positionComponent=(Position) EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID);
		if (positionComponent == null)
			return;
		
		Size sizeComponent=(Size) EntityManager.getInstance().getEntityComponent(id, EntityManager.SizeID);
		if (sizeComponent == null)
			return;
		
		if (apperanceComponent.fadein || apperanceComponent.fadeout) {

			float progress = ((float)(System.currentTimeMillis() - apperanceComponent.statFadeTimer))  / apperanceComponent.fadeDuration ;
			if (apperanceComponent.fadein) {	
				apperanceComponent.alpha = progress;
				if (progress >= 1.0f) {
					apperanceComponent.fadein = false;
					apperanceComponent.alpha = 1.0f;
				}
			} else if (apperanceComponent.fadeout)  {
				apperanceComponent.alpha = 1.0f - progress;
				if (progress >= 1.0f) {
					apperanceComponent.alpha = 0;
					try {
						
						EntityManager.getInstance().deleteEntity(id);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		//TODO: Composite semi transpert fade in out for character login 
	
	
		//Composite currentComposite = g.getComposite();
		//g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,apperanceComponent.alpha));

		if (animationComponent.facingRight)
			g.drawImage(animationComponent.getImage()
					,(int) (positionComponent.x + positionComponent.xmap - sizeComponent.width / 2)
					,(int) (positionComponent.y + positionComponent.ymap - sizeComponent.height / 2)
					,sizeComponent.width * GameConstants.ENTITYSCALE
					,sizeComponent.height * GameConstants.ENTITYSCALE
					,null);
		else
			g.drawImage(animationComponent.getImage()
					,(int) (positionComponent.x + positionComponent.xmap - sizeComponent.width / 2 + sizeComponent.width)
					,(int) (positionComponent.y + positionComponent.ymap - sizeComponent.height / 2),
					-sizeComponent.width * GameConstants.ENTITYSCALE
					,sizeComponent.height * GameConstants.ENTITYSCALE
					, null);
		
		
		//g.setComposite(currentComposite);
	}
	

	private static void hud(int id,Graphics g) {
		
		
	}

	private static void name(int id,Graphics g) {
		
		Position positionComponent=(Position) EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID);
		if (positionComponent == null)
			return;
		
		Attribute attributeComponent = (Attribute) EntityManager.getInstance().getEntityComponent(id, EntityManager.AttributeID);
		if (attributeComponent == null)
			return;
		
		
		
		int TEXTHEIGHT = g.getFontMetrics().getHeight();
		int TEXTWIDTH = g.getFontMetrics().stringWidth(attributeComponent.name);
		int XPOS = (int) (positionComponent.x + positionComponent.xmap - TEXTWIDTH / 2);
		int YPOS = (int) (positionComponent.y + positionComponent.ymap - TEXTHEIGHT) - 8;

		g.setColor(Color.black);
		g.drawString(attributeComponent.name, XPOS, YPOS - 3);
	}
	private static void nameplate(int id,Graphics g) {
		
		
		Position positionComponent=(Position) EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID);
		if (positionComponent == null)
			return;
		
		Attribute attributeComponent = (Attribute) EntityManager.getInstance().getEntityComponent(id, EntityManager.AttributeID);
		if (attributeComponent == null)
			return;
		
		Size sizeComponent=(Size) EntityManager.getInstance().getEntityComponent(id, EntityManager.SizeID);
		if (sizeComponent == null)
			return;
		
		Health healthComponent=(Health) EntityManager.getInstance().getEntityComponent(id, EntityManager.HealthID);
		if (healthComponent == null)
			return;
		
		double precentage = ((double) healthComponent.health / (double) healthComponent.maxHealth);
		int XPOS = (int) (positionComponent.x + positionComponent.xmap - sizeComponent.width / 2);
		int YPOS = (int) (positionComponent.y + positionComponent.ymap - sizeComponent.height * 0.6);
		int PlateBold = 4;

		g.setColor(Color.RED);
		g.fillRect(XPOS, YPOS, (int) (sizeComponent.width * precentage), PlateBold);
		g.setColor(Color.BLACK);
		g.fillRect(XPOS + (int) (sizeComponent.width * precentage), YPOS, (int) (sizeComponent.width * (1 - precentage)), PlateBold);
	}
	private static void speech(int id,Graphics g) {
		
		Speech speechComponent = (Speech) EntityManager.getInstance().getEntityComponent(id, EntityManager.SpeechID);
		if (speechComponent == null)
			return;
		
		if (System.currentTimeMillis() -  speechComponent.start_time  > speechComponent.duration) {
			EntityManager.getInstance().removeEntityComponent(id, EntityManager.SpeechID);
		}
		
		Position positionComponent=(Position) EntityManager.getInstance().getEntityComponent(id, EntityManager.PositionID);
		if (positionComponent == null)
			return;
		
		g.setFont(speechComponent.font);
		int TEXTHEIGHT = g.getFontMetrics().getHeight();
		int TEXTWIDTH = g.getFontMetrics().stringWidth(speechComponent.txt);
		int xgap = 7;
		int ygap = 3;
		int XPOS = (int) (positionComponent.x + positionComponent.xmap - TEXTWIDTH / 2);
		int YPOS = (int) (positionComponent.y + positionComponent.ymap - TEXTHEIGHT) - 8;
		
		g.setColor(Color.black);
		g.fillRect(XPOS - xgap, YPOS - TEXTHEIGHT - ygap, xgap * 2 + TEXTWIDTH, ygap * 2 + TEXTHEIGHT);
		g.setColor(Color.white);
		g.drawString(speechComponent.txt, XPOS, YPOS - 3);
		
	//	if (System.currentTimeMillis() - speechComponent.start_time > 1000 + 300 * speechComponent.txt.length() / 5)
	//	{
	//		EntityManager.getInstance().removeComponent(id,EntityManager.AnimationComponentID);
	//	}
	}
}

