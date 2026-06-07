package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.json.JSONObject;

public class ForceLawsDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	//To know if the Dialog is closed with OK or Cancel
	private int _status;

	//List of force laws (JSONObjects default)
	// Needed to access the description
	private List<JSONObject> _forceLawsInfo;

	//GUI
	private JPanel mainPanel;
	
	private JLabel help;
	
	private JScrollPane tableScroll;	
	private JTable dataTable;
	private LawsTableModel _dataTableModel;
	
	private JPanel lawSelectionPanel;
	private JComboBox<String> lawsComboBox;
	private int _selectedLawsIndex;
	
	private JPanel buttonPanel;
	private JButton cancelButton;
	private JButton okButton;

	public ForceLawsDialog(Frame windowAncestor, List<JSONObject> forceLawsInfo) {
		super(windowAncestor, true);
		setTitle("ForceLaws Selection");
		_status = 0;
		_forceLawsInfo = forceLawsInfo;
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		initGUI();
	}

	private void initGUI() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		setMinimumSize(new Dimension(800, 400));
		setMaximumSize(new Dimension(800, 400));
		setPreferredSize(new Dimension(800, 400));
		
		tableScroll();
		lawSelectionPanel();
		buttonPanel();
		
		help = new JLabel("<html><p>Select a force law and provide values for the parameters in the <b>Value column</b> (default values are used for parametes with no value).</p></html>");
		help.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(help);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(tableScroll);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(lawSelectionPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(buttonPanel);

		add(mainPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(false);
	}
	
	private void tableScroll() {
		_dataTableModel = new LawsTableModel();
		dataTable = new JTable(_dataTableModel);
		tableScroll = new JScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	private void lawSelectionPanel() {
		lawSelectionPanel = new JPanel(new FlowLayout());
		lawSelectionPanel.setAlignmentX(CENTER_ALIGNMENT);
		lawsComboBox = new JComboBox<String>();
		
		for (JSONObject jso : _forceLawsInfo) lawsComboBox.addItem(jso.getString("desc"));
		
		_selectedLawsIndex = 0;
		lawsComboBox.setSelectedIndex(_selectedLawsIndex);
		
		JSONObject data = _forceLawsInfo.get(_selectedLawsIndex).getJSONObject("data");
		_dataTableModel.updateTable(data);
		
		lawsComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_selectedLawsIndex = lawsComboBox.getSelectedIndex();
				JSONObject data = _forceLawsInfo.get(_selectedLawsIndex).getJSONObject("data");
				_dataTableModel.updateTable(data);
			}
		});
		
		lawSelectionPanel.add(new JLabel("Force Law: "));
		lawSelectionPanel.add(lawsComboBox);
	}
	
	private void buttonPanel() {
		buttonPanel = new JPanel(new FlowLayout());

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_status = 0;
				ForceLawsDialog.this.setVisible(false);
			}
		});

		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lawsComboBox.getSelectedItem() != null) {
					_status = 1;
					ForceLawsDialog.this.setVisible(false);
				}
				else JOptionPane.showMessageDialog(ForceLawsDialog.this.getParent(), "No force law selected");
			}
		});
		
		buttonPanel.add(cancelButton);
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
	
	public JSONObject getSelectedLaws() {
		JSONObject jso = new JSONObject();
		JSONObject selectedLaw = _forceLawsInfo.get(_selectedLawsIndex);
		String data = "{";
		for (int i = 0; i < _dataTableModel.getRowCount(); i++) {
			String key = (String)_dataTableModel.getValueAt(i, 0);
			String value = (String)_dataTableModel.getValueAt(i, 1);
			if (!value.equals("")) 
			{
				if (i != 0 && !data.equals("{")) data += ",";
				data += key + ":" + value;
			}
		}
		data += "}";
		
		jso.put("type", selectedLaw.getString("type"));
		jso.put("data", new JSONObject(data));
		
		return jso;
	}
}