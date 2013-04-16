package entity;

public class MovingEntity extends GameEntity {

	float speed;
	
	/**
	 * Creates a MovingEntity that 
	 * @param id
	 * @param speed should be somewhere between [0.2f,1f]
	 * @param rotation in degrees
	 */
	public MovingEntity(String id, float speed, float rotation) {
		super(id);
		this.speed=speed;
		this.setRotation(rotation);
		}
	
	
}