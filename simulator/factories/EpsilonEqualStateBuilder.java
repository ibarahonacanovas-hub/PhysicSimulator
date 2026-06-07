package simulator.factories;

import org.json.JSONObject;

import simulator.control.EpsilonEqualStates;
import simulator.control.StateComparator;

public class EpsilonEqualStateBuilder extends Builder<StateComparator> {
	
	public  EpsilonEqualStateBuilder() {
		super("epseq", "Epsilon-equal states comparator");
	}
	
	@Override
	protected StateComparator createTheInstance(JSONObject data) {
		double eps = data.has("eps") ? data.getDouble("eps") : 0.0;
		
		return new EpsilonEqualStates(eps);
	}

	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("eps", "the allowed error");
		return data;
	}
}