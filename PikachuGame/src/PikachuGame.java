import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
 
class PikachuGame {
	
	// const
	final int imagesCount = 28; // count of images for cards
	final int fieldWidth = 12; // width of field (cards) = x56 px
	final int fieldHeight = 7; // height of field (cards) = x72 px
	
	final String strScore = new String("Очков: ");
	// --
	
	private BufferedImage[] images = new BufferedImage[imagesCount]; // image array for cards
	
	// borders for panels
	private Border brdRaisedBevel = BorderFactory.createRaisedBevelBorder(); // -- free
	private Border brdBlackLine = BorderFactory.createLineBorder(Color.BLACK); // -- selected
	
	// field
	private byte[][] mainField = new byte[fieldHeight][fieldWidth];
	// cards on field
	private PicturePanel[][] cards = new PicturePanel[fieldHeight][fieldWidth];
	
	// states and conditions
	private Dimension coordFrom = new Dimension(0, 0);
	private Dimension coordTo = new Dimension(0, 0); // -- coords of start and finish
	private boolean isStartSelected = false;
	
	// information
	private JLabel lbStatus = new JLabel("Начало игры");
	private JLabel lbScore = new JLabel(strScore + "0");
	private JLabel lbChangingScore = new JLabel("0");
	
	PikachuGame() {
		
		// **** Загрузка картинок для карточек ****
		try {
			for (int i = 0; i < imagesCount; i++) {
				images[i] = ImageIO.read(new java.io.File("images\\" + String.valueOf(i) + ".png"));
			}
		} catch (IOException ex) {
			System.err.println("Ошибка загрузки картинки");
			ex.printStackTrace();
		}
		
		// Новый главный контейнер
		JFrame jfrm = new JFrame("Pikachu game");
		jfrm.setLayout(new BorderLayout());
		// Настройка размеров и положения
		jfrm.setMinimumSize(new Dimension(780, 600));
		jfrm.setSize(780, 600);
		jfrm.setLocationRelativeTo(null);
		// Обработка завершения программы
		jfrm.setDefaultCloseOperation(jfrm.DO_NOTHING_ON_CLOSE);
		jfrm.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				Object[] options = {
						"Закрыть",
						"Отмена"
				};
				int rslt = JOptionPane.showOptionDialog(we.getWindow(),
						"Вы уверены, что хотите закрыть приложение?",
						"Завершение программы",
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
		
		// **** Основное меню ****
		JMenuBar mainMenu = new JMenuBar(); // bar
		JMenu menuGame = new JMenu("Игра"); // item - game
		
		JMenuItem mitemNew = new JMenuItem("Новый расклад"); // subitem - new game
		menuGame.add(mitemNew);
		
		menuGame.addSeparator();
		
		JMenuItem mitemConfig = new JMenuItem("Настройки"); // subitem - configuration
		menuGame.add(mitemConfig);
		mitemConfig.setEnabled(false); // потому что пока не реализовано
		
		mainMenu.add(menuGame); // adding to main frame
		jfrm.setJMenuBar(mainMenu); // linking
		
		// для компоновки по центру
		Box hBox = Box.createHorizontalBox();
		hBox.add(Box.createGlue());
		
		// **** панель карточек ****
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
				cards[i][j] = new PicturePanel();
				cards[i][j].setPosition(new Dimension(j, i));
				cards[i][j].addMouseListener(new CardMouseListener());
				gbConstr.fill = GridBagConstraints.HORIZONTAL;
				gbConstr.weightx = 0.5; // balance weight
				gbConstr.gridx = j; // cell at X axis
				gbConstr.gridy = i; // cell at Y axis
				panel.add(cards[i][j], gbConstr);
			}
		arrangeElements(1);
		hBox.add(panel);
		hBox.add(Box.createGlue());
		
		Box vBox = Box.createVerticalBox();
		vBox.add(Box.createGlue());
		vBox.add(hBox);
		vBox.add(Box.createGlue());
		
		jfrm.add(vBox);
		
		// панель статуса
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		jfrm.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(jfrm.getWidth(), 20));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		
		Box hStatusBox = Box.createHorizontalBox();
		hStatusBox.add(lbScore);
		hStatusBox.add(Box.createGlue());
		hStatusBox.add(lbChangingScore);
		hStatusBox.add(Box.createGlue());
		hStatusBox.add(lbStatus);
		
		statusPanel.add(hStatusBox);
		
		jfrm.pack();
		
		// ===========================================================================
		// ================== ДЕЙСТВИЯ ===============================================
		
		mitemNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				arrangeElements(1);
			}
		});
		
		// Отображаем фрейм
		jfrm.setVisible(true);
	}
	
	public void arrangeElements(int level) {
		
		// this block will changed in future for loading samples
		byte[][] sampleField = new byte[fieldHeight][fieldWidth];
		for (int i = 0; i < fieldHeight; i++)
			for (int j = 0; j < fieldWidth; j++)
				sampleField[i][j] = 0;
		// -- /
		
		System.arraycopy(sampleField, 0, mainField, 0, sampleField.length);
		int cells = fieldWidth * fieldHeight; // cell count for filling
		Random rnd = new Random(); // initilize random generator
		int row, column; // vars for coordinating in array
		byte index = 0; // index for image on card
		boolean isOccupiedCell; // flg cell is occupied
		while (cells > 0) {
			index = (byte) rnd.nextInt(imagesCount);
			// добавлять нужно сразу пару
			for (int i = 0; i < 2; i++) {
				isOccupiedCell = true;
				while (isOccupiedCell) {
					row = rnd.nextInt(fieldHeight);
					column = rnd.nextInt(fieldWidth);
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
		isStartSelected = false;
		
	}
	
	private boolean isCardsEquals(Dimension coords1, Dimension coords2) {
		return mainField[coords1.height][coords1.width] == mainField[coords2.height][coords2.width] ? true : false;
	}
	
	// ======================= СОБЫТИЯ МЫШИ ДЛЯ КАРТОЧЕК ========================
	public class CardMouseListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
        	PicturePanel pp = (PicturePanel) e.getSource(); // caller object
        	Dimension clickPosition = new Dimension(0, 0); // card position
        	clickPosition = pp.getPosition();
        	if (mainField[clickPosition.height][clickPosition.width] == 0) // if empty card
        		return;
        	// if start not selected yet
        	if (!isStartSelected) { 
        		coordFrom = clickPosition; // remember coords
        		pp.setBorder(brdBlackLine); // visual
        		isStartSelected = true;
        	} else {
        		coordTo = clickPosition;
        		// if the same card or wrong card
        		if ((coordFrom.equals(coordTo)) || (!isCardsEquals(clickPosition, coordFrom))) {
        			cards[coordFrom.height][coordFrom.width].setBorder(brdRaisedBevel);
        			isStartSelected = false;
        		} else {
        			// this place for calculate way!!!!!!!!!!!!
        			mainField[coordFrom.height][coordFrom.width] = 0;
        			mainField[coordTo.height][coordTo.width] = 0;
        			cards[coordFrom.height][coordFrom.width].setImage(null);
        			cards[coordTo.height][coordTo.width].setImage(null);
        			cards[coordFrom.height][coordFrom.width].setBorder(brdRaisedBevel);
        			cards[coordTo.height][coordTo.width].setBorder(brdRaisedBevel);
        			isStartSelected = false;
        		}
        	}
        	/*
             JButton button = (JButton) e.getSource();
             String text = "<html><b>" + button.getText()
                       + " mouseReleased() <br>" + button.getText()
                       + " mouseClicked() </b><html>";
             eventLabel.setText(text);
            */
        }
        
        public void mouseEntered(MouseEvent e) {
        	return;
        	/*
             JButton button = (JButton) e.getSource();
             eventLabel.setText(button.getText() + " mouseEntered()");
            */
        }

        public void mouseExited(MouseEvent e) {
        	return;
        	/*
             JButton button = (JButton) e.getSource();
             eventLabel.setText(button.getText() + " mouseExited()");
            */
        }
        
        public void mousePressed(MouseEvent e) {
        	return;
        	/*
             JButton button = (JButton) e.getSource();
             eventLabel.setText(button.getText() + " mousePressed()");
            */
        }

        public void mouseReleased(MouseEvent e) {
        	return;
        	/*
             JButton button = (JButton) e.getSource();
             eventLabel.setText(button.getText() + " mouseReleased()");
            */
        }
        
	}
    
    public static void main(String[] args) {
    	// Создаем фрейм в потоке диспетчеризации событий
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new PikachuGame();
			}
		});
    }
        
}