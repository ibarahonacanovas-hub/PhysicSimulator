package simulator.model;

import java.util.List;
import simulator.misc.Vector2D;

public class MovingTowardsTwoFixedPoints implements ForceLaws{
	private  double g1,g2;
	private final Vector2D o1;
	private final Vector2D o2;
	
	public MovingTowardsTwoFixedPoints(Vector2D o1,Vector2D o2, double g1,double g2) {
		this.g1 = g1;
		this.g2 = g2;
		this.o1 = new Vector2D(o1);
		this.o2 = new Vector2D(o2);
	}
	
	public void apply(List<Body> bs) { // Aplicacion de una fuerza hacia el centro
		for (Body b : bs) {
			b.addForce(((o1.minus(b.getPosition().direction()).scale(g1)).plus((o2.minus(b.getPosition().direction()).scale(g2)))).scale(b.getMass()));
			
		}
	}
	
	public String toString() {
		return "Moving towards two fixed points " + o1+o2 + " with constant acceleration " + g1+" "+g2;
	}
}