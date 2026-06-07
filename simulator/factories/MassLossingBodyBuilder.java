package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MassLossingBody;
public class MassLossingBodyBuilder extends Builder<Body> {

	public MassLossingBodyBuilder() {
		super("mlb", "Mass lossing body");
	}

	@Override
	protected Body createTheInstance(JSONObject data) {
		// TODO Auto-generated method stub
		double m = data.getDouble("m");
		
		String id = data.getString("id");
		
		JSONArray pos = data.getJSONArray("p");
		Vector2D p = new Vector2D(pos.getDouble(0),pos.getDouble(1));
		
		JSONArray vel = data.getJSONArray("v");
		Vector2D v = new Vector2D(vel.getDouble(0),vel.getDouble(1));
		
		double freq = data.getDouble("freq");
		double factor = data.getDouble("factor");
		
		return new MassLossingBody(id, v, p, m, factor, freq);
	}
	
	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("id", "the identifier");
		data.put("p", "position");
		data.put("v", "velocity");
		data.put("m", "mass");
		data.put("freq", "frequency");
		data.put("factor", "the factor");
		return data;
	}
}