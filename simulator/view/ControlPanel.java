package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver {
	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	private boolean _stopped;
	private ForceLawsDialog changeForceLawsDialog;
	private TforceDialog totalForce;
	
	private JToolBar toolBar;
	private JButton load;
	private JButton physics;
	private JButton tForce;
	private JButton start;
	private JButton bang;
	private JSpinner bangers;
	private JButton stop;
	private JButton exit;
	private JSpinner steps;
	private JTextField deltaTime;
	private JFileChooser fileChooser;
	
	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_stopped = true;
		initGUI();
		_ctrl.addObserver(this);
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		
		toolBar = new JToolBar();
		fileChooser = new JFileChooser();
		changeForceLawsDialog = null;
		totalForce = null;
		loadButton();
		physicsButton();
		tForceButton();
		startButton();
		stopButton();
		exitButton();
		bangButton();
		bangersSpinner();
		stepsSpinner();
		dtTextField();
		
		toolBar.add(load);
		toolBar.addSeparator();
		toolBar.add(physics);
		toolBar.addSeparator();
		toolBar.add(start);
		toolBar.add(stop);
		toolBar.addSeparator();
		toolBar.add(new JLabel("Steps: "));
		toolBar.add(steps);
		toolBar.addSeparator();
		toolBar.add(new JLabel("Delta-time: "));
		toolBar.add(deltaTime);
		toolBar.addSeparator();
		toolBar.add(tForce);
		toolBar.addSeparator();
		toolBar.add(bang);
		toolBar.add(bangers);
		toolBar.add(Box.createGlue());
		toolBar.add(exit);
		
		add(toolBar, BorderLayout.PAGE_START);
		setOpaque(true);
	}
	
	private void loadButton() {
		load = new JButton(new ImageIcon("resources/icons/open.png"));
		load.setToolTipText("Load a file");
		load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { loadFile(); }
		});
	}

	private void bangButton() {
		bang = new JButton(new ImageIcon("resources/icons/run.png"));
		bang.setToolTipText("BANG");
		bang.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { bang((int)bangers.getValue()); }
		});
	}
	private void physicsButton() {
		physics = new JButton(new ImageIcon("resources/icons/physics.png"));
		physics.setToolTipText("Select gravity strategy");
		physics.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { selectForceLaws(); }
		});
	}
	private void tForceButton() {
		tForce = new JButton(new ImageIcon("resources/icons/physics.png"));
		tForce.setToolTipText("Total force per body");
		tForce.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { tForceLaws(); }
		});
	}
	private void startButton() {
		start = new JButton(new ImageIcon("resources/icons/run.png"));
		start.setToolTipText("Run simulation");
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				enableButtons(false);
				_stopped = false;
				_ctrl.setDeltaTime(Double.parseDouble(deltaTime.getText()));
				run_sim((int)steps.getValue());
			}
		});
	}
	
	private void stopButton() {
		stop = new JButton(new ImageIcon("resources/icons/stop.png"));
		stop.setToolTipText("Stop simulation");
		stop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				_stopped = true;
			}
		});
	}
	
	private void exitButton() {
		exit = new JButton(new ImageIcon("resources/icons/exit.png"));
		exit.setToolTipText("Exit simulator");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showOptionDialog(null, "Are sure you want to quit?", "Quit",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (n == 0) { System.exit(0); }
			}
		});
	}
	
	private void stepsSpinner() {
		steps = new JSpinner(new SpinnerNumberModel(10000,1,9000000,100)); // la profe lo hace de 100 en 100 
		steps.setMinimumSize(new Dimension(70, 20));
		steps.setMaximumSize(new Dimension(70, 20));
		steps.setPreferredSize(new Dimension(70, 20));
		steps.setToolTipText("Set steps");
	}
	private void bangersSpinner() {
		bangers = new JSpinner(new SpinnerNumberModel(100,1,100,1)); // la profe lo hace de 100 en 100 
		bangers.setMinimumSize(new Dimension(70, 20));
		bangers.setMaximumSize(new Dimension(70, 20));
		bangers.setPreferredSize(new Dimension(70, 20));
		bangers.setToolTipText("Set bang");
	}
	
	private void dtTextField() {
		deltaTime = new JTextField();
		deltaTime.setMinimumSize(new Dimension(60, 20));
		deltaTime.setMaximumSize(new Dimension(60, 20));
		deltaTime.setPreferredSize(new Dimension(60, 20));
		deltaTime.setToolTipText("Set delta-time");
	}
	
	private void enableButtons(boolean ena) {
		load.setEnabled(ena);
		physics.setEnabled(ena);
		start.setEnabled(ena);
		exit.setEnabled(ena);
		tForce.setEnabled(ena);
		bang.setEnabled(ena);
	}
	
	private void run_sim(int n) {
		if ( n>0 && !_stopped ) {
			try {
				_ctrl.run(1, null, null, null);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this.getParent(), "Simulation stopped");
				_stopped = true;
				enableButtons(true);
				return;
			}
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					run_sim(n-1);
				}
			});
		} else {
			_stopped = true;
			enableButtons(true);
		}
	}
	
	private void selectForceLaws() {
		if (changeForceLawsDialog == null) {
			changeForceLawsDialog = new ForceLawsDialog((Frame)SwingUtilities.getWindowAncestor(this),
			_ctrl.getForceLawsInfo());
		}
		int status = changeForceLawsDialog.open();
		if (status == 1) {
			try {
				JSONObject obj = changeForceLawsDialog.getSelectedLaws();
				_ctrl.setForceLaws(obj);
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(this.getParent(), "Something went wrong: " + e.getLocalizedMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	private void tForceLaws() {
		if(totalForce==null)
		{
			totalForce=new TforceDialog((Frame)SwingUtilities.getWindowAncestor(this),_ctrl);
		}
		int status=totalForce.open();
		if (status == 1) {
			try {
				
				JSONObject o = new JSONObject();
				
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(this.getParent(), "Something went wrong: " + e.getLocalizedMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	private void bang(int x)
	{
		_ctrl.bang(x);
	}
	
	private void loadFile() {
		try {
			int v = fileChooser.showOpenDialog(null);
			if (v == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				_ctrl.reset();
				_ctrl.loadBodies(new FileInputStream(file));
				System.out.println("loading " + file.getName());
			}
			else System.out.println("load cancelled by user");
		}
		catch (FileNotFoundException fnfe) { System.err.println("File not found"); }
		catch (JSONException jse) { System.err.println("Failed to load bodies"); }
	}
	
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) { deltaTime.setText(Double.toString(dt)); }
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) { deltaTime.setText(Double.toString(dt)); }
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {}
	@Override
	public void onAdvance(List<Body> bodies, double time) {}
	@Override
	public void onDeltaTimeChanged(double dt) { deltaTime.setText(Double.toString(dt)); }
	@Override
	public void onForceLawsChanged(String fLawsDesc) {}

	@Override
	public void onBang(List<Body> bodies) {
		// TODO Auto-generated method stub
		
	}
}