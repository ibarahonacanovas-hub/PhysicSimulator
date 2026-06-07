package simulator.model;


import org.json.JSONObject;

import simulator.misc.Vector2D;

public class MassLossingBody extends Body{

	private double lossFactor; // factor perdida de masa
	private double lossFrequency; // intervalo despues del cual pierde masa
	private double count;
	
	public MassLossingBody(String id, Vector2D v, Vector2D p, double m,double lossFactor,double lossFrequency) {
		super(id, v, p, m);
		this.lossFactor = lossFactor;
		this.lossFrequency = lossFrequency;
		count = 0.0;
	}
	
	public double getLossFactor() {return lossFactor;}
	public double getLossFrequency() {return lossFrequency;}
	
	void move(double t) {
		super.move(t);
		count += t;
		if (count >= lossFrequency) {
			mass *= 1 - lossFactor;
			count = 0.0;
		}
	}
	
	public JSONObject getState() { // Formato JSON
		JSONObject jso = super.getState();
		jso.put("freq", getLossFrequency());
		jso.put("factor", getLossFactor());
		return jso;
	}
}