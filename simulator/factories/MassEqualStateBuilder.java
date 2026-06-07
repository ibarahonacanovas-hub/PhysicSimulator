package simulator.factories;

import org.json.JSONObject;

import simulator.control.MassEqualStates;
import simulator.control.StateComparator;

public class MassEqualStateBuilder extends Builder<StateComparator> {

	public MassEqualStateBuilder() {
		super("masseq", "Mass-equal states comparator");
	}
	
	@Override
	protected StateComparator createTheInstance(JSONObject data) {
		return new MassEqualStates();
	}

	protected JSONObject createData() {
		JSONObject data = new JSONObject();
		return data;
	}
}