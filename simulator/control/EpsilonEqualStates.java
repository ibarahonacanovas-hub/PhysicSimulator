package simulator.control;

import org.json.JSONObject;
import org.json.JSONArray;
import simulator.misc.Vector2D;

public class EpsilonEqualStates implements StateComparator {

	private double eps;
	
	public EpsilonEqualStates(double eps) {
		this.eps = eps;
	}

	public boolean equal(JSONObject s1, JSONObject s2) {
		/* si los campos "time" son distintos, entonces falso
		 * si contienen distinto numero de cuerpos,entonces falso
		 * 
		 * recorrer los cuerpos y comparar
		 * para double usar Math.abs(d1-d2)
		 * para posicion, velocidad.. (es decir vectores):
		 * crear un vector 2D v1, con las dos componentes extraidas del campo correspondiente de s1
		 * crear un vector 2D v2, con las dos componentes extraidas del campo correspondiente de s2
		 * comprobar con v1.distanceTo(v2)<=eps
		 * 
		 */
		if (Math.abs(s1.getDouble("time") - s2.getDouble("time")) > eps) return false;
		JSONArray jsa1 = s1.getJSONArray("bodies");
		JSONArray jsa2 = s2.getJSONArray("bodies");
		if (jsa1.length() != jsa2.length()) return false;

		for (int i = 0; i < jsa1.length(); i++) {
			JSONObject jso1 = jsa1.getJSONObject(i);
			JSONObject jso2 = jsa2.getJSONObject(i);
			Vector2D v1;
			Vector2D v2;
			if (!jso1.getString("id").equals(jso2.getString("id"))) return false;

			v1 = new Vector2D(jso1.getJSONArray("p").getDouble(0),jso1.getJSONArray("p").getDouble(1));
			v2 = new Vector2D(jso2.getJSONArray("p").getDouble(0),jso2.getJSONArray("p").getDouble(1));
			if (v1.distanceTo(v2) > eps) return false;

			v1 = new Vector2D(jso1.getJSONArray("v").getDouble(0),jso1.getJSONArray("v").getDouble(1));
			v2 = new Vector2D(jso2.getJSONArray("v").getDouble(0),jso2.getJSONArray("v").getDouble(1));
			if (v1.distanceTo(v2) > eps) return false;

			v1 = new Vector2D(jso1.getJSONArray("f").getDouble(0),jso1.getJSONArray("f").getDouble(1));
			v2 = new Vector2D(jso2.getJSONArray("f").getDouble(0),jso2.getJSONArray("f").getDouble(1));
			if (v1.distanceTo(v2) > eps) return false;
		}
		return true;
	}
}