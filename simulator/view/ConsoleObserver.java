package simulator.view;

import java.util.ArrayList;
import java.util.List;

import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ConsoleObserver implements SimulatorObserver {
	private int _option;
	private ArrayList<Boolean>past;
	private ArrayList<Integer>changes;
	public ConsoleObserver(int option)
	{
		_option=option;
		past=new ArrayList<Boolean>();
		changes=new ArrayList<Integer>();
	}
	public ArrayList<Integer> getChanges()
	{
		return changes;
	}
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		if(_option==0)
		{
			if(b.getPosition().getX()<0)
			{
				changes.add(0);
				past.add(false);
			}
			else if(b.getPosition().getX()>=0)
			{
				changes.add(0);
				past.add(true);
			}
		}
		else
		{
			if(b.getPosition().getY()<0)
			{
				changes.add(0);
				past.add(false);

			}
			else if(b.getPosition().getY()>=0)
			{
				changes.add(0);
				past.add(true);
			}
		}

	}
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		for(int i=0;i<bodies.size();i++)
		{
			if(_option==0)
			{
				if(past.get(i)&&bodies.get(i).getPosition().getX()<0)
				{
					changes.set(i, changes.get(i)+1);
					past.set(i,false);
				}
				else if(!past.get(i)&&bodies.get(i).getPosition().getX()>=0)
				{
					changes.set(i,changes.get(i)+1);
					past.set(i, true);
				}
			}
			else
			{
				if(past.get(i)&&bodies.get(i).getPosition().getY()<0)
				{
					changes.set(i,changes.get(i)+1);
					past.set(i, false);
				}
				else if(!past.get(i)&&bodies.get(i).getPosition().getY()>=0)
				{
					changes.set(i,changes.get(i)+1);
					past.set(i, true);
				}
			}
		}

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
