package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public class Body {
	protected String id;
	protected Vector2D vel;
	protected Vector2D force;
	protected Vector2D pos;
	protected double mass;

	public Body(String id, Vector2D v, Vector2D p, double m) {
		this.id = id;
		vel = new Vector2D(v);
		force = new Vector2D();
		pos = new Vector2D(p);
		mass = m;
	}

	public String getId() {return id;}
	public Vector2D getVelocity() {return vel;}
	public Vector2D getForce() {return force;}
	public Vector2D getPosition(){return pos;}
	public double getMass() {return mass;}

	void addForce (Vector2D f) { // Añade la fuerza f al vector de fuerza del cuerpo (usando el plus del Vector2D)
		force = force.plus(f);
	}

	void resetForce() { // Pone el valor del vector de fuerza a (0,0)
		force = new Vector2D();
	}

	void move(double t){ // Mueve el cuerpo
		Vector2D accel;
		if (mass == 0) accel = new Vector2D();
		else accel = new Vector2D(force.scale(1.0/mass));
		pos = pos.plus(vel.scale(t).plus(accel.scale(0.5*t*t)));
		vel = vel.plus(accel.scale(t));
	}

	public boolean equals(Body other) { // Compara el cuerpo con otro (other)
		return this.getId().equals(other.getId());
	}

	public JSONObject getState() { // Formato JSON
		JSONObject jso = new JSONObject();
		jso.put("id",getId());
		jso.put("m",getMass());
		jso.put("p",getPosition().asJSONArray());
		jso.put("v",getVelocity().asJSONArray());
		jso.put("f",getForce().asJSONArray());
		return jso;
	}

	public String toString() { // Devuelve getState como string
		return getState().toString();
	}

	public void bang(int x) {
		mass=(((100.0-(x))/100.0)*(mass));
		
	}
}