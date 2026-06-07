package simulator.factories;

import org.json.JSONObject;

public abstract class Builder<T> {

	protected  String _typeTag;
	protected String _desc; 
	
	public Builder(String typeTag, String desc) {
		_typeTag = typeTag;
		_desc = desc;
	}
	
	public T createInstance(JSONObject info) {
		T b = null;
		if (_typeTag != null && _typeTag.equals(info.getString("type"))) {
			b = createTheInstance(info.getJSONObject("data"));
		}
		return b;
	}
	
	protected abstract T createTheInstance(JSONObject jsonObject);
	
	public JSONObject getBuilderInfo() {
		JSONObject info = new JSONObject();
		info.put("type", _typeTag);
		info.put("data", createData()); 
		info.put("desc", _desc);
		return info;
	}
	
	protected  JSONObject createData() {
		return new JSONObject();
	}
}