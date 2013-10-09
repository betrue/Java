import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.BevelBorder;
 
class PikachuGame {
	
	// const
	enum GameState { DEFAULT, START, WRONG_CARDS, TOO_FAR }
	
	final String strScore = new String("Очков: ");
	final String strStart = new String("Начало игры");
	final String strWrongCards = new String("На карточках разные картинки");
	final String strTooFar = new String("Нет подходящего пути");
	// --
	
	JFrame jfrm; // main window
	Field field; // main field
	JProgressBar pbTimer; // timer
	Timer tTimer;
	
	// information
	private JLabel lbStatus = new JLabel(strStart);
	private JLabel lbScore = new JLabel(strScore + "0");
	private JLabel lbChangingScore = new JLabel("0");
	
	// points
	private int points;
	
	private int timerValue = 0; // значение таймера
	private int levelMaxTimer; // время на прохождение текущего уровня
	private List<Color> lstColors = new ArrayList<Color>();
	private ColorGradient cgTimer;
	
	// child frames
	private FrmConfiguration frmConfiguration = null;
	
	
	@SuppressWarnings("static-access")
	PikachuGame() {
		// Новый главный контейнер
		jfrm = new JFrame("Pikachu game");
		jfrm.setLayout(new BorderLayout());
		// Настройка размеров и положения
		jfrm.setMinimumSize(new Dimension(860, 720));
		jfrm.setSize(860, 720);
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
		
		// field = new Field(4, 3); // creating a field
		field = new Field(); // creating a field
		
		// **** Основное меню ****
		JMenuBar mainMenu = new JMenuBar(); // bar
		JMenu menuGame = new JMenu("Игра"); // item - game
		
		JMenuItem mitemNew = new JMenuItem("Новый расклад"); // subitem - new game
		menuGame.add(mitemNew);
		
		menuGame.addSeparator();
		
		JMenuItem mitemConfig = new JMenuItem("Настройки"); // subitem - configuration
		menuGame.add(mitemConfig);
		// mitemConfig.setEnabled(false); // потому что пока не реализовано
		
		mainMenu.add(menuGame); // adding to main frame
		jfrm.setJMenuBar(mainMenu); // linking
		
		// для компоновки по центру
		Box hBox = Box.createHorizontalBox();
		
		pbTimer = new JProgressBar(SwingConstants.VERTICAL);
		lstColors.add(Color.RED);
		lstColors.add(Color.YELLOW);
		lstColors.add(Color.GREEN);
		cgTimer = new ColorGradient(lstColors);
		// pbTimer.setPreferredSize(new Dimension(20, hBox.HEIGHT));
		pbTimer.setMinimum(0);
		hBox.add(pbTimer);
		
		tTimer = new Timer(1000, new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					changeTimer(--timerValue);
					if (timerValue == 0) {
						tTimer.stop();
		        		showMessage("Время вышло!",
								"Проигрыш");
					}
				}
		});
		tTimer.stop();
		
		hBox.add(Box.createGlue());
		
		// **** панель карточек ****
		JPanel panel = new JPanel();
		Dimension panelSize = new Dimension(field.getWidth() * field.cardWidth, field.getHeight() * field.cardHeight);
		panel.setLayout(new GridBagLayout());
		panel.setOpaque(false);
		panel.setPreferredSize(panelSize);
		panel.setSize(panelSize);
		panel.setMinimumSize(panelSize);
		panel.setMaximumSize(panelSize);
		GridBagConstraints gbConstr = new GridBagConstraints();
		
		for (PicturePanel i : field.getCards()) {
			i.addMouseListener(new CardMouseListener());
			gbConstr.fill = GridBagConstraints.HORIZONTAL;
			gbConstr.weightx = 0.5; // balance weight
			gbConstr.gridx = i.getPosition().width; // cell at X axis
			gbConstr.gridy = i.getPosition().height; // cell at Y axis
			panel.add(i, gbConstr);
		}
		
		newGame(1);
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
		lbStatus.setHorizontalAlignment(JLabel.RIGHT);
		lbStatus.setPreferredSize(new Dimension(300, lbStatus.getHeight()));
		hStatusBox.add(lbStatus);
		
		statusPanel.add(hStatusBox);
		
		jfrm.pack();
		
		// ===========================================================================
		// ================== ДЕЙСТВИЯ ===============================================
		
		mitemNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newGame(1);
			}
		});
		
		mitemConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmConfiguration = new FrmConfiguration(jfrm);
				tTimer.stop();
				if (frmConfiguration.execute()) {
					
				} else {
					
				}
			}
		});
		
		// Отображаем фрейм
		jfrm.setVisible(true);
	}
	
	// вывод сообщения
	private void showMessage(String messageText, String title) {
		Object[] options = {
				"Новая игра",
				"Выход"
		};
		int rslt = JOptionPane.showOptionDialog(jfrm,
				messageText,
				title,
				JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,
				options,
				options[1]);
		if (rslt == 0) {
			newGame(1);
		} else {
			jfrm.setVisible(false);
			System.exit(0);
		}
	}
	
	// счёт времени
	private void changeTimer(int value) {
		pbTimer.setValue(value);
		pbTimer.setForeground(cgTimer.getColorByPercent(((float) value) / (pbTimer.getMaximum() - pbTimer.getMinimum()), true));
	}
	
	public void changeScore(int changePoints) {
		if (changePoints > 0) {
			lbChangingScore.setText("+" + String.valueOf(changePoints));
		} else if (changePoints < 0) {
			lbChangingScore.setText("-" + String.valueOf(changePoints));
		} else
			lbChangingScore.setText(String.valueOf(0));
		points = points + changePoints;
		lbScore.setText(strScore + String.valueOf(points));
	}
	
	public void changeState(GameState state) {
		switch (state) {
		case START :
			lbStatus.setForeground(Color.BLACK);
			lbStatus.setText(strStart);
			break;
		case WRONG_CARDS :
			lbStatus.setForeground(Color.RED);
			lbStatus.setText(strWrongCards);
			break;
		case TOO_FAR :
			lbStatus.setForeground(Color.RED);
			lbStatus.setText(strTooFar);
			break;
		case DEFAULT :
			lbStatus.setForeground(Color.BLACK);
			lbStatus.setText("");
			break;
		}
		// lbStatus.setPreferredSize(new Dimension(300, lbStatus.getHeight()));
		// lbStatus.setSize(200, lbStatus.getHeight());
	}
	
	public void newGame(int level) {
		points = 0;
		changeScore(0);
		changeState(GameState.START);
		field.arrangeElements(level);
		levelMaxTimer = 400;
		pbTimer.setMaximum(levelMaxTimer);
		timerValue = levelMaxTimer;
		// tTimer.start();
	}
	
	// ======================= СОБЫТИЯ МЫШИ ДЛЯ КАРТОЧЕК ========================
	public class CardMouseListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
        	PicturePanel pp = (PicturePanel) e.getSource(); // caller object
        	Dimension clickPosition = new Dimension(0, 0); // card position
        	clickPosition = pp.getPosition();
        	if (pp.getImageIndex() == -1) // if empty card
        		return;
        	if (!tTimer.isRunning())
        		tTimer.start();
        	changeScore(0);
        	// if start not selected yet
        	if (!field.isStartSelected) {
        		field.coordFrom = clickPosition; // remember coords
        		pp.setBorder(field.brdBlackLine); // visual
        		field.isStartSelected = true;
        	} else {
        		field.coordTo = clickPosition;
        		PicturePanel ppFrom = field.getCardByPosition(field.coordFrom);
        		PicturePanel ppTo = field.getCardByPosition(field.coordTo);
        		// if the same card or wrong card, then skip
        		if ((field.coordFrom.equals(field.coordTo)) || (!field.isCardsEquals(field.coordTo, field.coordFrom))) {
        			ppFrom.setBorder(field.brdRaisedBevel);
        			// changeState(GameState.WRONG_CARDS);
        		} else {
        			int pathCost = field.getPathCost();
        			if (pathCost == -1) {
        				ppFrom.setBorder(field.brdRaisedBevel);
        				changeState(GameState.TOO_FAR);
        			} else {
	        			field.removeCard(ppFrom);
	        			field.removeCard(ppTo);
	        			changeScore(pathCost);
	        			changeState(GameState.DEFAULT);
        			}
        		}
        		field.isStartSelected = false;
        	}
        	if (field.getActiveCardCount() == 0) {
        		tTimer.stop();
        		showMessage("Вы выиграли!\n" +
							"Потраченное время: " + String.valueOf(levelMaxTimer - timerValue) + " сек.\n" +
							"Количество заработанных очков: " + String.valueOf(points),
							"Победа");
        	}
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