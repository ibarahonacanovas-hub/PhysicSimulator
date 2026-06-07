package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class StatusBar extends JPanel implements SimulatorObserver {
	private static final long serialVersionUID = 1L;

	private JToolBar toolBar;
	private JLabel _currTime; // for current time
	private JLabel _currLaws; // for gravity laws
	private JLabel _numOfBodies; // for number of bodies

	private int bodies;
	private double time;
	private String laws;

	StatusBar(Controller ctrl) {
		JSONObject jso = ctrl.getStatus();
		time = jso.getDouble("currTime");
		bodies = jso.getInt("numBodies");
		laws = jso.getString("flInfo");
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBorder(BorderFactory.createBevelBorder(1));

		toolBar = new JToolBar();
		_currTime = new JLabel();
		_currTime.setMinimumSize(new Dimension(120, 20));
		_currTime.setMaximumSize(new Dimension(120, 20));
		_currTime.setPreferredSize(new Dimension(120, 20));
		_numOfBodies = new JLabel();
		_numOfBodies.setMinimumSize(new Dimension(120, 20));
		_numOfBodies.setMaximumSize(new Dimension(120, 20));
		_numOfBodies.setPreferredSize(new Dimension(120, 20));
		_currLaws = new JLabel();

		resetLabels();

		toolBar.add(_currTime);
		toolBar.add(_numOfBodies);
		toolBar.add(_currLaws);

		add(toolBar);
		setOpaque(true);
	}

	private void resetLabels() {
		_currTime.setText("  Time: " + time);
		_numOfBodies.setText("  Bodies: " + bodies);
		_currLaws.setText("  Laws: " + laws);
	}

	// SimulatorObserver methods
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		this.bodies = bodies.size();
		this.time = time;
		laws = fLawsDesc;
		resetLabels();
	}
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		this.bodies = bodies.size();
		this.time = time;
		laws = fLawsDesc;
		resetLabels();
	}
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		this.bodies = bodies.size();
		resetLabels();
	}
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		this.bodies = bodies.size();
		this.time = time;
		resetLabels();
	}
	@Override
	public void onDeltaTimeChanged(double dt) {}
	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		laws = fLawsDesc;
		resetLabels();
	}

	@Override
	public void onBang(List<Body> bodies) {
		// TODO Auto-generated method stub
		
	}
}