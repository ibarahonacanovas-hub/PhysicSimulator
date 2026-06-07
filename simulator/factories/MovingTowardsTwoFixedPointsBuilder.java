package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.MovingTowardsTwoFixedPoints;
import simulator.misc.Vector2D;
public class MovingTowardsTwoFixedPointsBuilder extends Builder<ForceLaws>{
	public  MovingTowardsTwoFixedPointsBuilder() {
		super("mt2fp", "Moving	 towards two fixed points a fixed point");
	}
	
	@Override
	protected ForceLaws createTheInstance(JSONObject data) {
		double g1 = data.has("g1") ? data.getDouble("g1") : 9.81;
		double g2 = data.has("g2") ? data.getDouble("g2") : 9.81;
		Vector2D c1;
		Vector2D c2;
		if (data.has("c1")) {
			JSONArray centre = data.getJSONArray("c1");
			c1 = new Vector2D(centre.getDouble(0), centre.getDouble(1));
		}
		else c1 = new Vector2D(0, 0);
		if (data.has("c2")) {
			JSONArray centre = data.getJSONArray("c2");
			c2 = new Vector2D(centre.getDouble(0), centre.getDouble(1));
		}
		else c2 = new Vector2D(0, 0);
		
		return new MovingTowardsTwoFixedPoints(c1,c2,g1,g2);
	}
	
	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("c1", "the first point towards bodies move\r\n"
				+ "(a json list of 2 numbers, e.g., [100.0,50.0])");
		data.put("c2", "the second point towards bodies move\r\n"
				+ "(a json list of 2 numbers, e.g., [100.0,50.0])");
		data.put("g1", "the length of the first acceleration vector (a number)");
		data.put("g2", "the length of the second acceleration vector (a number)");
		return data;
	}
}