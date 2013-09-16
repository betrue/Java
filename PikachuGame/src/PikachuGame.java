import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
 
class PikachuGame {
	
	final int imagesCount = 11; // count of images for cards
	final int fieldWidth = 12; // width of field (cards) = x56 px
	final int fieldHeight = 7; // height of field (cards) = x72 px
	private BufferedImage[] images = new BufferedImage[imagesCount]; // image array for cards
	// borders for panels
	private Border brdRaisedBevel = BorderFactory.createRaisedBevelBorder(); // -- free
	// field
	private byte[][] mainField = new byte[fieldWidth][fieldHeight];
	// cards on field
	private PicturePanel[][] cards = new PicturePanel[fieldWidth][fieldHeight];
	
	PikachuGame() {
		
		// **** �������� �������� ��� �������� ****
		try {
			for (int i = 0; i < imagesCount; i++) {
				images[i] = ImageIO.read(new java.io.File("C:\\Distr\\Java\\eclipse-java-juno-SR2-win32\\workspace\\PikachuGame\\src\\images\\" + String.valueOf(i) + ".png"));
			}
		} catch (IOException ex) {
			System.err.println("������ �������� ��������");
			ex.printStackTrace();
		}
		
		// ����� ������� ���������
		JFrame jfrm = new JFrame("Pikachu game");
		// ��������� �������� � ���������
		jfrm.setMinimumSize(new Dimension(780, 600));
		jfrm.setSize(780, 600);
		jfrm.setLocationRelativeTo(null);
		// ��������� ���������� ���������
		jfrm.setDefaultCloseOperation(jfrm.DO_NOTHING_ON_CLOSE);
		jfrm.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				Object[] options = {
						"�������",
						"������"
				};
				int rslt = JOptionPane.showOptionDialog(we.getWindow(),
						"�� �������, ��� ������ ������� ����������?",
						"���������� ���������",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[1]);
				if (rslt == 0) {
					we.getWindow().setVisible(false);
					System.exit(0);
				}
			}
		});
		
		// **** �������� ���� ****
		JMenuBar mainMenu = new JMenuBar(); // bar
		JMenu menuGame = new JMenu("����"); // item - game
		
		JMenuItem mitemNew = new JMenuItem("����� �������"); // subitem - new game
		menuGame.add(mitemNew);
		
		JMenuItem mitemHint = new JMenuItem("���������"); // subitem - hint
		menuGame.add(mitemHint);
		mitemHint.setEnabled(false); // ������ ��� ���� �� �����������
		
		menuGame.addSeparator();
		
		JMenuItem mitemConfig = new JMenuItem("���������"); // subitem - configuration
		menuGame.add(mitemConfig);
		mitemConfig.setEnabled(false); // ������ ��� ���� �� �����������
		
		mainMenu.add(menuGame); // adding to main frame
		jfrm.setJMenuBar(mainMenu); // linking
		
		// ��� ���������� �� ������
		Box hBox = Box.createHorizontalBox();
		hBox.add(Box.createGlue());
		
		// **** ������ �������� ****
		JPanel panel = new JPanel();
		Dimension panelSize = new Dimension(672, 504);
		panel.setLayout(new GridBagLayout());
		panel.setOpaque(false);
		panel.setPreferredSize(panelSize);
		panel.setSize(panelSize);
		panel.setMinimumSize(panelSize);
		panel.setMaximumSize(panelSize);
		GridBagConstraints gbConstr = new GridBagConstraints();
		for (int i = 0; i < fieldHeight; i++)
			for (int j = 0; j < fieldWidth; j++) {
				cards[j][i] = new PicturePanel();
				gbConstr.fill = GridBagConstraints.HORIZONTAL;
				gbConstr.weightx = 0.5; // balance weight
				gbConstr.gridx = j; // cell at X axis
				gbConstr.gridy = i; // cell at Y axis
				panel.add(cards[j][i], gbConstr);
			}
		arrangeElements(1);
		hBox.add(panel);
		hBox.add(Box.createGlue());
		
		Box vBox = Box.createVerticalBox();
		vBox.add(Box.createGlue());
		vBox.add(hBox);
		vBox.add(Box.createGlue());
		
		jfrm.setLayout(new BorderLayout());
		jfrm.add(vBox);
		
		jfrm.pack();
		
		// ===========================================================================
		// ================== �������� ===============================================
		
		mitemNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				arrangeElements(1);
			}
		});
		
		// ���������� �����
		jfrm.setVisible(true);
	}
	
	public void arrangeElements(int level) {
		
		// this block will changed in future for loading samples
		byte[][] sampleField = new byte[fieldWidth][fieldHeight];
		for (int i = 0; i < fieldHeight; i++)
			for (int j = 0; j < fieldWidth; j++)
				sampleField[j][i] = 0;
		// -- /
		
		System.arraycopy(sampleField, 0, mainField, 0, sampleField.length);
		int cells = fieldWidth * fieldHeight; // cell count for filling
		Random rnd = new Random(); // initilize random generator
		int row, column; // vars for coordinating in array
		byte index = 0; // index for image on card
		boolean isOccupiedCell; // flg cell is occupied
		while (cells > 0) {
			index = (byte) rnd.nextInt(imagesCount);
			// ��������� ����� ����� ����
			for (int i = 0; i < 2; i++) {
				isOccupiedCell = true;
				while (isOccupiedCell) {
					row = rnd.nextInt(fieldWidth);
					column = rnd.nextInt(fieldHeight);
					if (mainField[row][column] == 0) {
						mainField[row][column] = (byte) (index + 1);
						cards[row][column].setImage(images[index]);
						cards[row][column].setBorder(brdRaisedBevel);
						isOccupiedCell = false;
					}
				}
				cells--; // decrement free cells count
			}
		}
		
	}
    
    public static void main(String[] args) {
    	// ������� ����� � ������ ��������������� �������
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new PikachuGame();
			}
		});
    }
        
}