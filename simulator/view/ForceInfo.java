package simulator.view;

import simulator.misc.Vector2D;

public class ForceInfo {
	private String key;
	private Vector2D _v;
	
	public ForceInfo(String k,Vector2D value) {
		key = k;
		_v = value;
		
	}
	
	public String getKey() { return key; }
	
	public Vector2D getValue() { return _v; }
	
	
	public void setValue(Vector2D v) {
		_v = v;
	}
	
	public String toString() {
		return "key:     " + key + "       value:" + _v ;
	}
	public void addValue(Vector2D v)
	{
		_v=_v.plus(v);
	
	}
}