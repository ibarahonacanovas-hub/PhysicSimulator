package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;

public class MassEqualStates implements StateComparator{

	public boolean equal(JSONObject s1, JSONObject s2) {
		/* si los tiempos son distintos entonces false
		 * si el numero de cuerpos(bodies)es distinto entonces falso
		 * recorrer los cuerpos en ambos estados y prguntar por los campos "id" y "m"
		 */

		if (s1.getDouble("time") != s2.getDouble("time")) return false;
		JSONArray jsa1 = s1.getJSONArray("bodies");
		JSONArray jsa2 = s2.getJSONArray("bodies");

		if (jsa1.length() != jsa2.length()) return false;

		for (int i = 0; i < jsa1.length(); i++) {
			if (!jsa1.getJSONObject(i).getString("id").equals(jsa2.getJSONObject(i).getString("id")) || 
					jsa1.getJSONObject(i).getDouble("m")!=jsa2.getJSONObject(i).getDouble("m"))return false;
		}

		return true;
	}
}