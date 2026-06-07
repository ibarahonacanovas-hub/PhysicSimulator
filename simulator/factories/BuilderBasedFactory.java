 package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {

	private List<Builder<T>> _builders;
	private List<JSONObject> _factoryElements;
	
	public BuilderBasedFactory(List<Builder<T>> builders) {
		_builders = new ArrayList<>(builders);
		_factoryElements = new ArrayList<>();
		
		for (Builder<T> b : _builders) _factoryElements.add(b.getBuilderInfo());
	}

	@Override
	public T createInstance(JSONObject info) throws IllegalArgumentException {
		if (info == null) throw new IllegalArgumentException("Invalid value for createInstance: null");
		
		for (Builder<T> b : _builders) {
			T aux = b.createInstance(info);
			if (aux != null) return aux;
		}
		
		throw new IllegalArgumentException("Unable to create instance with given info");
	}

	@Override
	public List<JSONObject> getInfo() {
		return _factoryElements;
	}
}