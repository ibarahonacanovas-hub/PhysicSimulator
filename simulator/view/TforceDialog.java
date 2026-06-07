package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import javax.swing.JDialog;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import simulator.control.Controller;

public class TforceDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	//To know if the Dialog is closed with OK or Cancel
	private int _status;

	//List of force laws (JSONObjects default)
	// Needed to access the description
	private Controller _ctrl;

	//GUI
	private JPanel mainPanel;
	
	
	private JScrollPane tableScroll;	
	private JTable dataTable;
	private TotalForceModelTable _dataTableModel;
	
	
	private JPanel buttonPanel;
	private JButton okButton;

	public TforceDialog(Frame windowAncestor, Controller _ctrl) {
		super(windowAncestor, true);
		setTitle("ForceLaws Selection");
		_status = 0;
		this._ctrl = _ctrl;
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		initGUI();
	}

	private void initGUI() {
		setTitle("Total Force per Body");
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		setMinimumSize(new Dimension(800, 400));
		setMaximumSize(new Dimension(800, 400));
		setPreferredSize(new Dimension(800, 400));
		
		tableScroll();
		buttonPanel();
		
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(tableScroll);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(buttonPanel);

		add(mainPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(false);
	}
	
	private void tableScroll() {
		_dataTableModel = new TotalForceModelTable(_ctrl);
		dataTable = new JTable(_dataTableModel);
		tableScroll = new JScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	private void buttonPanel() {
		buttonPanel = new JPanel(new FlowLayout());
		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					_status = 0;
					TforceDialog.this.setVisible(false);
			}
		});
		
		buttonPanel.add(okButton);
	}

	
	public int open() {
		pack();
		setVisible(true);
		return _status;
	}

	public String toString() {
		return _dataTableModel.toString();
	}
	

}