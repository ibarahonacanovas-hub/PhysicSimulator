package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body> {

	public BasicBodyBuilder() {
		super("basic", "Default Body");
	}
	
	protected Body createTheInstance(JSONObject data) {
		double m = data.getDouble("m");
		
		String id = data.getString("id");
		
		JSONArray pos = data.getJSONArray("p");
		Vector2D p = new Vector2D(pos.getDouble(0),pos.getDouble(1));
		
		JSONArray vel = data.getJSONArray("v");
		Vector2D v = new Vector2D(vel.getDouble(0),vel.getDouble(1));
		
		return new Body(id, v, p, m);
	}
	
	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("id","identifier");
		data.put("v","velocity ");
		data.put("p","body position");
		data.put("m","mass");
		return data;
	}
}