package simulator.view;

public class LawsInfo {
	private String key;
	private String value;
	private String description;
	
	public LawsInfo(String k, String v, String d) {
		key = k;
		value = v;
		description = d;
	}
	
	public String getKey() { return key; }
	
	public String getValue() { return value; }
	public String getDescription() { return description; }
	
	public void setValue(String object) {
		value = object;
	}
	
	public String toString() {
		return "key:     " + key + "       value:" + value + "      description:" + description;
	}
}