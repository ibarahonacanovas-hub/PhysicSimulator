package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.TrappedBody;

public class TrappedBodyBuilder extends Builder<Body> {

	public TrappedBodyBuilder() {
		super("trapped", "Trapped Body");
	}
	
	protected Body createTheInstance(JSONObject data) {
		double m = data.getDouble("m");
		
		String id = data.getString("id");
		
		JSONArray pos = data.getJSONArray("p");
		Vector2D p = new Vector2D(pos.getDouble(0),pos.getDouble(1));
		
		JSONArray vel = data.getJSONArray("v");
		Vector2D v = new Vector2D(vel.getDouble(0),vel.getDouble(1));
		double dist = data.getDouble("dist");
		
		return new TrappedBody(id, v, p, m,dist);
	}
	
	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("id","identifier");
		data.put("pos","body position");
		data.put("vel","velocity ");
		data.put("mass","mass");
		data.put("dist","distance");
		return data;
	}
}