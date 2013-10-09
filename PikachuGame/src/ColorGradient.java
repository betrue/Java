/*
 * Class for graduating colors between points.
 * Written for decreasing timer.
*/

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


public class ColorGradient {
	
	private List<Color> lstColors; // ������ ������ (������� �����)
	private float[] colorPositions; // ������� ������ (�������, �� ������� ��� ���������) - �� 0.0 �� 1.0
	private int colorCount; // ���-�� ������
	
	ColorGradient(List<Color> lstColors) {
		colorCount = lstColors.size();
		this.lstColors = new ArrayList<Color>(lstColors);
		colorPositions = new float[colorCount];
		// ����� ����� �� �������
		for (int i = 0; i < colorCount; i++) {
			colorPositions[i] = (float) i / (colorCount - 1);
		}
	}
	
	public Color getColorByPercent(float percent, boolean revert) {
		for (int i = 1; i < colorCount; i++) {
			if (percent < colorPositions[i]) { // ���� �������, �� ������� ����� ��������
				Color firstColor = lstColors.get(i-1);
				Color secondColor = lstColors.get(i);
				float localPoint = (percent - colorPositions[i-1]) / (colorPositions[i] - colorPositions[i-1]);
				if (revert)
					localPoint = 1 - localPoint;
				return new Color(Math.round(firstColor.getRed()*localPoint + secondColor.getRed()*(1-localPoint)),
						Math.round(firstColor.getGreen()*localPoint + secondColor.getGreen()*(1-localPoint)),
						Math.round(firstColor.getBlue()*localPoint + secondColor.getBlue()*(1-localPoint)));
			}
		}
		// ����� �������� �������� >= 1.0
		return lstColors.get(colorCount-1);
	}
	
	public Color getColorByPercent(float percent) {
		return getColorByPercent(percent, false);
	}
	
}
