import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class Field {
	
	// const
	enum SearchDirection { VERTICAL, HORIZONTAL, UP, DOWN, LEFT, RIGHT }
	// borders for panels
	public final Border brdRaisedBevel = BorderFactory.createRaisedBevelBorder(); // -- free
	public final Border brdBlackLine = BorderFactory.createLineBorder(Color.BLUE); // -- selected
	// --
	
	private int imagesCount; // count of images for cards
	private int fieldWidth, fieldHeight; // field dimensions x(40;50) px
	public final int cardWidth = 40;
	public final int cardHeight = 50;
	private int activeCardCount; // ����� �������� � ���������� (��������)
	
	private BufferedImage[] images; // image array for cards
	
	List<PicturePanel> lstCards = new ArrayList<PicturePanel>(); // cards on field
	
	// states and conditions
	public boolean isStartSelected = false; // start selected
	public Dimension coordFrom = new Dimension(0, 0);
	public Dimension coordTo = new Dimension(0, 0); // -- coords of start and finish
	
	Field(int width, int height) {
		fieldWidth = width;
		fieldHeight = height;
		initComponents();
	}
	
	Field() {
		fieldWidth = 16;
		fieldHeight = 9;
		initComponents();
	}
	
	// ! ����� �������������
	private void initComponents() {
		activeCardCount = 0;
		// **** �������� �������� ��� �������� ****
		imagesCount = 36;
		images = new BufferedImage[imagesCount];
		try {
			for (int i = 0; i < imagesCount; i++) {
				images[i] = ImageIO.read(new java.io.File("images\\" + String.valueOf(i) + ".png"));
			}
		} catch (IOException ex) {
			System.err.println("������ �������� ��������");
			ex.printStackTrace();
		}
		
		// **** ����������� �������� � ������ ****
		PicturePanel currentPanel;
		// fieldMask = new byte[fieldHeight][fieldWidth];
		for (int i = 0; i <= fieldHeight + 1; i++)
			for (int j = 0; j <= fieldWidth + 1; j++) {
				currentPanel = new PicturePanel();
				currentPanel.setPosition(new Dimension(j, i));
				lstCards.add(currentPanel);
			}
	}
	
	// ���������� ������ ����
	public int getWidth() {
		return fieldWidth + 2;
	}
	
	// ���������� ������ ����
	public int getHeight() {
		return fieldHeight + 2;
	}
	
	// ���������� ������ ���� �������� �� ����
	public List<PicturePanel> getCards() {
		return lstCards;
	}
	
	// ���������� ���-�� �������� ��������
	public int getActiveCardCount() {
		return activeCardCount;
	}
	
	// ���������� �������� �� �������
	public PicturePanel getCardByPosition(Dimension pos) {
		for (PicturePanel i : lstCards)
			if (i.getPosition().equals(pos))
				return i;
		return null;
	}
	
	// ���������� �������� �� �������
	public PicturePanel getCardByPosition(int width, int height) {
		return getCardByPosition(new Dimension(width, height));
	}
	
	// ======================= ��������� �������� ========================
	public boolean isCardsEquals(Dimension coords1, Dimension coords2) {
		return getCardByPosition(coords1).getImageIndex() == getCardByPosition(coords2).getImageIndex();
	}
	
	// ======================= �������� �������� ========================
	public void removeCard(PicturePanel card) {
		if (card.getImageIndex() != -1) {
			activeCardCount--;
			card.clearImage();
		}
		card.setBorder(null);
	}
	
	// ======================= ����������� �������� �� ���� ========================
	public void arrangeElements(int level) {
		
		// this block will changed in future for loading samples
		byte[][] sampleField = new byte[fieldHeight][fieldWidth];
		for (int i = 0; i < fieldHeight; i++)
			for (int j = 0; j < fieldWidth; j++)
				sampleField[i][j] = 0;
		// -- /

		int cellCount = fieldWidth * fieldHeight; // cell count for filling
		activeCardCount = cellCount; // �����������, ��� ������ �� ��������� � ��� �������� ������ ���������
		Random rnd = new Random(); // initilize random generator
		PicturePanel currentPanel;
		int row, column; // vars for coordinating in array
		byte index = 0; // index for image on card
		boolean isOccupiedCell; // flg cell is occupied
		while (cellCount > 0) {
			index = (byte) rnd.nextInt(imagesCount);
			// ��������� ����� ����� ����
			for (int i = 0; i < 2; i++) {
				isOccupiedCell = true;
				while (isOccupiedCell) {
					row = rnd.nextInt(fieldHeight);
					column = rnd.nextInt(fieldWidth);
					if (sampleField[row][column] == 0) {
						sampleField[row][column] = (byte) (index + 1);
						currentPanel = this.getCardByPosition(column + 1, row + 1);
						currentPanel.setImage(images[index], index);
						currentPanel.setBorder(brdRaisedBevel);
						isOccupiedCell = false;
					}
				}
				cellCount--; // decrement free cells count
			}
		}
		isStartSelected = false;
		
	}
	
	// ======================= ������ ������ ���� ========================
	public int getPathCost() {
		return searchPath();
	}
	
	// ======================= ����������� ����� ���� �� ������ ========================
	private int searchLine(SearchDirection direction, Dimension startPoint, int level) {
		if (level > 3) // ����������� ��� ������ �����������
			return -1;
		int partCost = 0; // ��������� �� ���������� ����� �� ������� �����
		int retCost = 999999; // �������� ��� ��������; ������� ������������, ����� ���� � ��� ����������
		int curCost; // ��������� ��� ������ �������
		Dimension stepDirection = new Dimension(); // ��������� ����������� ��������
		switch (direction) {
		case VERTICAL : // ������������ ����� � ������� �������� �����
			stepDirection.height = startPoint.height < coordTo.height ? 1 : -1;
			break;
		case HORIZONTAL : // �������������� ����� � ������� �������� �����
			stepDirection.width = startPoint.width < coordTo.width ? 1 : -1;
			break;
		default : return -1;
		}
		// ���������� ��� ������� �����
		Dimension curPoint = new Dimension(startPoint.width + stepDirection.width, startPoint.height + stepDirection.height);
		PicturePanel curCard = getCardByPosition(curPoint); // �������� � ������� �����
		while (curCard != null) { // �������������, ���� �� ����� �� ���� ����
			partCost++; // ����������� ���� �� �������
			if (curPoint.equals(coordTo)) { // ���� ��� ��� �������� �����
				return partCost; // �� ���������� ���������� �� ������ ����
			} else if (curCard.getImageIndex() != -1) { // ���� ����������� ��������
				break; // �� ����������� ����� (�����)
			} else {
				// � ����������� �� �������� ����������� ��������, ���� ������ �� ������� �����
				if (direction == SearchDirection.VERTICAL)
					curCost = searchLine(SearchDirection.HORIZONTAL, curPoint, level + 1);
				else
					curCost = searchLine(SearchDirection.VERTICAL, curPoint, level + 1);
				if (curCost != -1)
					retCost = (partCost + curCost) < retCost ? (partCost + curCost) : retCost;
			}
			curPoint.width = curPoint.width + stepDirection.width;
			curPoint.height = curPoint.height + stepDirection.height;
			curCard = getCardByPosition(curPoint);
		}
		
		if (retCost == 999999)
			return -1;
		else
			return retCost;
	}
	
	
	// ======================= ����� �* ======================== -- ���
	// ======================= ����� ������� ��������� ========================
	private int searchPath() {
		final int maxResult = 999999;
		int result = maxResult;
		boolean is1Straight = true; // ���� ���������� ������ ������
		
		// I. ���� �� ����� �� ��������� ��� ����� �� ����� �����
		if ((coordFrom.height == coordTo.height) || (coordFrom.width == coordTo.width)) {
			Dimension curPoint = (Dimension) coordFrom.clone();
			Dimension step = new Dimension();
			if (coordFrom.height == coordTo.height) {
				step.width = (coordTo.width - coordFrom.width) > 0 ? 1 : -1;
			} else {
				step.height = (coordTo.height - coordFrom.height) > 0 ? 1 : -1;
			}
			curPoint.width = curPoint.width + step.width;
			curPoint.height = curPoint.height + step.height;
			while (!curPoint.equals(coordTo)) {
				if (getCardByPosition(curPoint).getImageIndex() != -1) { // ���� �� ���� ����������� ��������
					is1Straight = false; // ������� ����
					break; // ������� �� ����������� �����
				}
				curPoint.width = curPoint.width + step.width;
				curPoint.height = curPoint.height + step.height;
			}
			if (is1Straight) // ���� ���� ����������
				// �� ���� ������ � �� ������; �� ��������� �� ����� ��������, ������� ����� ���������� ���������
				return Math.abs(coordTo.height - coordFrom.height) + Math.abs(coordTo.width - coordFrom.width);
		}
		
		int tempResult = 0;
		
		// II. ������ ���� ���� � ����� ��� ����� ������
		// a) ������� ����� �� ���������
		// a1) �����
		for (int v = coordFrom.height - 1; v >= 0; v--) {
			if ((v != 0) && getCardByPosition(coordFrom.width, v).getImageIndex() != -1) {
				break;
			}
			tempResult = searchLine(SearchDirection.HORIZONTAL, new Dimension(coordFrom.width, v), 2);
			if (tempResult == -1) {
				continue;
			} else {
				result = (Math.abs(v - coordFrom.height) + tempResult) < result ?
						(Math.abs(v - coordFrom.height) + tempResult) : result;
			}
		}
		
		// a2) ����
		for (int v = coordFrom.height + 1; v <= fieldHeight + 1; v++) {
			if ((v != fieldHeight + 1) && getCardByPosition(coordFrom.width, v).getImageIndex() != -1) {
				break;
			}
			tempResult = searchLine(SearchDirection.HORIZONTAL, new Dimension(coordFrom.width, v), 2);
			if (tempResult == -1) {
				continue;
			} else {
				result = (Math.abs(v - coordFrom.height) + tempResult) < result ?
						(Math.abs(v - coordFrom.height) + tempResult) : result;
			}
		}
		
		// b) ������ ����� �� ���������
		// b1) �����
		for (int h = coordFrom.width - 1; h >= 0; h--) {
			if ((h != 0) && getCardByPosition(h, coordFrom.height).getImageIndex() != -1) {
				break;
			}
			tempResult = searchLine(SearchDirection.VERTICAL, new Dimension(h, coordFrom.height), 2);
			if (tempResult == -1) {
				continue;
			} else {
				result = (Math.abs(h - coordFrom.width) + tempResult) < result ?
						(Math.abs(h - coordFrom.width) + tempResult) : result;
			}
		}
		
		// b1) ������
		for (int h = coordFrom.width + 1; h <= fieldWidth + 1; h++) {
			if ((h != fieldWidth + 1) && getCardByPosition(h, coordFrom.height).getImageIndex() != -1) {
				break;
			}
			tempResult = searchLine(SearchDirection.VERTICAL, new Dimension(h, coordFrom.height), 2);
			if (tempResult == -1) {
				continue;
			} else {
				result = (Math.abs(h - coordFrom.width) + tempResult) < result ?
						(Math.abs(h - coordFrom.width) + tempResult) : result;
			}
		}
		
		return result == maxResult ? -1 : result;
	}
	
}
