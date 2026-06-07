package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import simulator.exceptions.NotEqualStatesException;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {

	private PhysicsSimulator _sim;
	private Factory<Body> _bodiesFactory;
	private Factory<ForceLaws> _forceLawsFactory;

	public Controller(PhysicsSimulator sim, Factory<Body> bodiesFactory, Factory<ForceLaws> forceLawsFactory) {
		_sim = sim;
		_bodiesFactory = bodiesFactory;
		_forceLawsFactory = forceLawsFactory;
	}

	public void loadBodies(InputStream in) {
		JSONObject jsonInput = new JSONObject(new JSONTokener(in));
		JSONArray bodies = jsonInput.getJSONArray("bodies");
		for (int i = 0; i < bodies.length(); i++) {
			_sim.addBody(_bodiesFactory.createInstance(bodies.getJSONObject(i)));
		}
	}

	public void run(int steps, OutputStream out, InputStream expOut, StateComparator cmp) throws NotEqualStatesException {
		JSONObject expOutJO = null;

		if (expOut != null) {
			expOutJO = new JSONObject(new JSONTokener(expOut));
		}

		if (out == null) {
			out = new OutputStream() {
				@Override
				public void write(int b) throws IOException{}
			};
		}

		PrintStream p = new PrintStream(out);
		p.println("{");
		p.println("\"states\": [");

		JSONObject currState = null;
		JSONObject expState = null;

		currState = _sim.getState(); //Comparacion de los estados base
		p.println(currState);
		if (expOutJO != null) {
			expState = expOutJO.getJSONArray("states").getJSONObject(0);
			if (!cmp.equal(expState, currState))
				throw new NotEqualStatesException(expState, currState, 0);
		}

		for (int i = 1; i <= steps; i++) {
			_sim.advance();
			currState = _sim.getState();
			p.println("," + currState);

			if (expOutJO != null) {
				expState = expOutJO.getJSONArray("states").getJSONObject(i);
				if(!cmp.equal(expState, currState)) // Comparacion de los siguientes estados
					throw new NotEqualStatesException(expState, currState, i);
			}
		}

		p.println("]");
		p.println("}");
	}
	
	public void reset() {
		_sim.reset();
	}
	
	public void setDeltaTime(double dt) {
		_sim.setDeltaTime(dt);
	}
	
	public void addObserver(SimulatorObserver o) {
		_sim.addObserver(o);
	}
	
	public List<JSONObject> getForceLawsInfo() {
		return _forceLawsFactory.getInfo();
	}
	
	public void setForceLaws(JSONObject info) {
		ForceLaws fl = _forceLawsFactory.createInstance(info);
		_sim.setForceLaws(fl);
	}
	
	public JSONObject getStatus() {
		return _sim.getStatus();
	}

	public void bang(int x) {
		_sim.bang(x);
		
	}
}