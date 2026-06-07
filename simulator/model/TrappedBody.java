package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public class TrappedBody extends Body{
	private double dist;
	private Vector2D p;

	public TrappedBody(String id, Vector2D v, Vector2D p, double m,double dist) {
		super(id,v,p,m);
		this.dist=dist;
		this.pos=p;
	}


	public double getDist() {return dist;}

	void move(double t){ // Mueve el cuerpo
		Vector2D accel;
		if (mass == 0) accel = new Vector2D();
		else accel = new Vector2D(force.scale(1.0/mass));
		Vector2D position = pos.plus(vel.scale(t).plus(accel.scale(0.5*t*t)));
		if(p.distanceTo(position)<dist)
		{
			pos=position;
			vel = vel.plus(accel.scale(t));
		}
		
	}



	public JSONObject getState() { // Formato JSON
		JSONObject jso = new JSONObject();
		jso.put("id",getId());
		jso.put("pos",getPosition().asJSONArray());
		jso.put("vel",getVelocity().asJSONArray());
		jso.put("mass",getMass());
		jso.put("dist",getDist());
		return jso;
	}

	public String toString() { // Devuelve getState como string
		return getState().toString();
	}
}