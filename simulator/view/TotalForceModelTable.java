package simulator.view;

import java.util.ArrayList;
import java.util.List;


import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;

import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class TotalForceModelTable extends AbstractTableModel implements SimulatorObserver{

	private static final long serialVersionUID = 1L;
	private  String[] nombres = {"BODY","TOTAL FORCES"};
	private List<ForceInfo>force;
	
	public TotalForceModelTable(Controller ctrl) {
		
		ctrl.addObserver(this);
		force = new ArrayList<ForceInfo>();
	}
	public void clear() {
		force.clear();
	}
	@Override
	public int getRowCount() {
		return force == null ? 0 : force.size();
	}
	public boolean isCellEditable (int row, int column) {
		return false;
	}
	@Override
	public int getColumnCount() {
		return nombres.length;
	}
	@Override
	public String getColumnName(int column) {
		return nombres[column];
	
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object obj=null;
		switch (columnIndex) {
		case 0:
			obj = force.get(rowIndex).getKey();
			break;
		case 1:
			obj = force.get(rowIndex).getValue();
			break;
		}
		return obj;
	}

	

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
	
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		
		clear();
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		
		force.add(new ForceInfo(b.getId(), b.getForce()));
		fireTableStructureChanged();
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		for(int i=0;i<bodies.size();i++)
		{
			force.get(i).addValue(bodies.get(i).getForce());
		}
		fireTableStructureChanged();
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onBang(List<Body> bodies) {
		// TODO Auto-generated method stub
		
	}
}
