package simulator.model;

import java.util.List;
import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws{
	private  double g;
	private final Vector2D o;
	
	public MovingTowardsFixedPoint(Vector2D o, double g) {
		this.g = g;
		this.o = new Vector2D(o);
	}
	
	public void apply(List<Body> bs) { // Aplicacion de una fuerza hacia el centro
		for (Body b : bs) {
			b.addForce(o.minus(b.getPosition()).direction().scale(g*b.getMass()));
		}
	}
	
	public String toString() {
		return "Moving towards " + o + " with constant acceleration " + g;
	}
}