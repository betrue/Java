import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ini4j.Ini;
import org.ini4j.Wini;


public class FrmConfiguration extends JDialog {
	
	private FrmConfiguration thisFrm = this;
	private String strIniName = new String("config.ini");
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4251445391177066060L;
	private boolean result;
	
	public FrmConfiguration(JFrame parent) {
		super(parent, true);
		initComponents(parent);
	}
	
	private void initComponents(JFrame parent) {
		Dimension frmSize = new Dimension(250, 200);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(frmSize);
		this.setSize(frmSize);
		this.setResizable(false);
		this.setLocationRelativeTo(parent);
		
		JPanel plOptions = new JPanel();
		plOptions.setLayout(new GridBagLayout());
		plOptions.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		GridBagConstraints gbConstr = new GridBagConstraints();
		gbConstr.fill = GridBagConstraints.HORIZONTAL;
		gbConstr.weightx = 0.5;
		gbConstr.weighty = 0.5;
		gbConstr.insets = new Insets(4, 6, 4, 6);
		
		JLabel lbWidth = new JLabel("Ширина:");
		gbConstr.anchor = GridBagConstraints.EAST;
		gbConstr.gridx = 0;
		gbConstr.gridy = 0;
		plOptions.add(lbWidth, gbConstr);
		
		JLabel lbHeight = new JLabel("Высота:");
		gbConstr.anchor = GridBagConstraints.EAST;
		gbConstr.gridx = 0;
		gbConstr.gridy = 1;
		plOptions.add(lbHeight, gbConstr);
		
		final JTextField edWidth = new JTextField();
		gbConstr.anchor = GridBagConstraints.WEST;
		gbConstr.gridx = 1;
		gbConstr.gridy = 0;
		plOptions.add(edWidth, gbConstr);
		
		final JTextField edHeight = new JTextField();
		gbConstr.anchor = GridBagConstraints.WEST;
		gbConstr.gridx = 1;
		gbConstr.gridy = 1;
		plOptions.add(edHeight, gbConstr);
		
		this.add(plOptions, BorderLayout.PAGE_START);
		
		JButton btnOK = new JButton("Сохранить");
		JButton btnCancel = new JButton("Отмена");
		
		Box bxHBottomPanel = Box.createHorizontalBox();
		bxHBottomPanel.add(Box.createGlue());
		bxHBottomPanel.add(btnOK);
		bxHBottomPanel.add(Box.createHorizontalStrut(10));
		bxHBottomPanel.add(btnCancel);
		bxHBottomPanel.add(Box.createGlue());
		bxHBottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		this.add(bxHBottomPanel, BorderLayout.PAGE_END);
		
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					File f = new File(strIniName);
					f.createNewFile();
					Wini ini = new Wini(f);
					ini.put("fieldFormat", "width", edWidth.getText());
					ini.put("fieldFormat", "height", edHeight.getText());
					ini.store();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				result = true;
				thisFrm.dispose();
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result = false;
				thisFrm.dispose();
			}
		});
		
	}
	
	public boolean execute() {
		setVisible(true);
		return result;
	}
	
}
