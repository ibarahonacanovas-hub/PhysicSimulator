package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSimulator {

	private double _dt;
	private List<Body> bodyList;
	private ForceLaws forceLaws;
	private double currentTime;
	private List<SimulatorObserver> observerList;
	
	public PhysicsSimulator(double t, ForceLaws fl) throws IllegalArgumentException {
		if (t <= 0.0) throw new IllegalArgumentException("Unvalid delta-time");
		if (fl == null) throw new IllegalArgumentException("No force laws specified");
		_dt = t;
		forceLaws = fl;
		currentTime = 0.0;
		bodyList = new ArrayList<Body>();
		observerList = new ArrayList<SimulatorObserver>();
	}

	public void advance() {
		for(Body b:bodyList) b.resetForce();
		forceLaws.apply(bodyList);
		for(Body b: bodyList) b.move(_dt);
		currentTime += _dt;
		
		for(SimulatorObserver ob : observerList) {
			ob.onAdvance(bodyList, currentTime);
		}
	}

	public void addBody(Body b) throws IllegalArgumentException {
		if (bodyList.contains(b)) throw new IllegalArgumentException("Tried to add an already existing body");
		bodyList.add(b);
		
		for (SimulatorObserver ob : observerList) {
			ob.onBodyAdded(bodyList, b);
		}
	}

	public JSONObject getState() {
		JSONArray bodyListJSON = new JSONArray();
		for (Body b : bodyList) bodyListJSON.put(b.getState());
		
		JSONObject jso = new JSONObject();
		jso.put("time", currentTime);
		jso.put("bodies", bodyListJSON);
		return jso;
	}
	
	public JSONObject getStatus() {
		JSONObject jso = new JSONObject();
		jso.put("currTime", currentTime);
		jso.put("numBodies", bodyList.size());
		jso.put("flInfo", forceLaws.toString());
		return jso;
	}

	public String toString() {
		return getState().toString();
	}
	
	public void reset() {
		bodyList.clear();
		currentTime = 0.0;
		
		for (SimulatorObserver ob : observerList) {
			ob.onReset(bodyList, currentTime, _dt, forceLaws.toString());
		}
	}
	
	public void addObserver(SimulatorObserver o) {
		if (!observerList.contains(o)) observerList.add(o);
		
		o.onRegister(bodyList, currentTime, _dt, forceLaws.toString());
	}
	
	public void setDeltaTime(double dt) throws IllegalArgumentException {
		if (dt <= 0.0) throw new IllegalArgumentException("Unvalid delta-time");
		_dt = dt;
		
		for (SimulatorObserver ob : observerList) {
			ob.onDeltaTimeChanged(_dt);
		}
	}
	
	public void setForceLaws(ForceLaws forceLaws) {
		if (forceLaws == null) throw new IllegalArgumentException("No force laws specified");
		this.forceLaws = forceLaws;
		
		for (SimulatorObserver ob : observerList) {
			ob.onForceLawsChanged(this.forceLaws.toString());
		}
	}

	public void bang(int x)throws IllegalArgumentException {
		if(x<0||x>100)throw new IllegalArgumentException("Incorrect value of x"); 
		for(Body b:bodyList) b.bang(x);
		for (SimulatorObserver ob : observerList) {
			ob.onBang(bodyList);
		}
	}
}