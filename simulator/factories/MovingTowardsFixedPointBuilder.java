package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;
import simulator.misc.Vector2D;
public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws>{
	public  MovingTowardsFixedPointBuilder() {
		super("mtfp", "Moving	 towards a fixed point");
	}
	
	@Override
	protected ForceLaws createTheInstance(JSONObject data) {
		double g = data.has("g") ? data.getDouble("g") : 9.81;
		
		Vector2D c;
		if (data.has("c")) {
			JSONArray centre = data.getJSONArray("c");
			c = new Vector2D(centre.getDouble(0), centre.getDouble(1));
		}
		else c = new Vector2D(0, 0);
		
		return new MovingTowardsFixedPoint(c, g);
	}
	
	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("c", "the point towards which bodies move\r\n"
				+ "(a json list of 2 numbers, e.g., [100.0,50.0])");
		data.put("g", "the length of the acceleration vector (a number)");
		return data;
	}
}