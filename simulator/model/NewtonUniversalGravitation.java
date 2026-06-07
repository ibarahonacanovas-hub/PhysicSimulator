package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws {

	private final double G;

	public NewtonUniversalGravitation(double G) {
		this.G = G;
	}

	public void apply(List<Body> bs) { // Funcion aplicacion de fuerza 
		Vector2D F;
		double f;
		for (Body Bi : bs) {
			F = new Vector2D();
			for (Body Bj : bs) {
				if (Bi != Bj) {
					f = 0;
					if (Bi.getMass() == 0.0) Bi.vel = new Vector2D();
					else {
						Vector2D delta = Bj.getPosition().minus(Bi.getPosition());
						double dist = delta.magnitude();
						f = dist > 0.0 ? (G * Bi.getMass() * Bj.getMass()) / (dist*dist) : 0.0;
						F = F.plus(delta.direction().scale(f));
					}
				}
			}
			Bi.addForce(F);
		}
	}
	
	 public String toString() {  
		 return "Newton's Universal Gravitation with G = " + G;
	}
}