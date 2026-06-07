package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws> {

	public  NoForceBuilder() {
		super("nf", "No force");
	}
	
	@Override
	protected ForceLaws createTheInstance(JSONObject jsonObject) {
		return new NoForce();
	}
	
	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		return data;
	}
}