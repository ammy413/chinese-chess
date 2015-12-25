package com.deskapp.demo;

/** File: GameCanvas.java
*����˹�����ÿһ������Ļ��� */
import javax.swing.*;
import javax.swing.border.EtchedBorder;
         //EtchedBorderΪswing���е�ͻ���򰼽��ı߿�
import java.awt.*;
class GameCanvas extends JPanel {
//Ĭ�ϵķ������ɫΪ�ۻ�ɫ��������ɫΪ��ɫ
private Color backColor = Color.black, frontColor = Color.orange;
private int rows, cols, score = 0, scoreForLevelUpdate = 0;
private ErsBox[][] boxes;
private int boxWidth, boxHeight;
	//score���÷֣�scoreForLevelUpdate����һ��������Ļ���
// ������ĵ�һ���汾�Ĺ��캯��
public GameCanvas(int rows, int cols) {
	this.rows = rows;
	this.cols = cols;
	//��ʼ��rows*cols��ErsBox����
	boxes = new ErsBox[rows][cols];
	for (int i = 0; i < boxes.length; i++) {
		for (int j = 0; j < boxes[i].length; j++) {
			boxes[i][j] = new ErsBox(false);
		}
	}
        //���û����ı߽�
	setBorder(new EtchedBorder(
	        EtchedBorder.RAISED, Color.white, new Color(148, 145, 140)));
}
	//������ĵڶ����汾�Ĺ��캯��
public GameCanvas(int rows, int cols, Color backColor, Color frontColor) {
	this(rows, cols);     //���õ�һ���汾�Ĺ��캯��
	this.backColor = backColor;
	this.frontColor = frontColor;  //ͨ���������ñ�����ǰ����ɫ
}
	//������Ϸ����ɫ��
public void setBackgroundColor(Color backColor) {
	this.backColor = backColor;
}
	//ȡ����Ϸ����ɫ��
public Color getBackgroundColor() {
	return backColor;
}
	//������Ϸ����ɫ��
public void setBlockColor(Color frontColor) {
	this.frontColor = frontColor;
}
	//ȡ����Ϸ����ɫ��
public Color getBlockColor() {
	return frontColor;
}
    //ȡ�û����з��������
public int getRows() {
	return rows;
}
	//ȡ�û����з��������
public int getCols() {
	return cols;
}
	//ȡ����Ϸ�ɼ�
public int getScore() {
	return score;
}
    //ȡ������һ��������Ļ���
public int getScoreForLevelUpdate() {
	return scoreForLevelUpdate;
}
	//�����󣬽���һ�����������Ļ�����0
public void resetScoreForLevelUpdate() {
	scoreForLevelUpdate -= ErsBlocksGame.PER_LEVEL_SCORE;
}
	//�õ�ĳһ��ĳһ�еķ������á�
public ErsBox getBox(int row, int col) {
	if (row < 0 || row > boxes.length - 1
	        || col < 0 || col > boxes[0].length - 1)
		return null;
	return (boxes[row][col]);
}
     //����JComponent��ĺ������������
public void paintComponent(Graphics g) {
	super.paintComponent(g);
	g.setColor(frontColor);
	for (int i = 0; i < boxes.length; i++) {
		for (int j = 0; j < boxes[i].length; j++) {
            //��ǰ����ɫ�򱳾���ɫ����ÿ������
			g.setColor(boxes[i][j].isColorBox() ? frontColor : backColor);
			g.fill3DRect(j * boxWidth, i * boxHeight,
			        boxWidth, boxHeight, true);
		}
	}
}
    //���ݴ��ڵĴ�С���Զ���������ĳߴ�
public void fanning() {
	boxWidth = getSize().width / cols;
	boxHeight = getSize().height / rows;
}
	//��һ�б���Ϸ�ߵ����󣬽������������Ϊ��Ϸ�߼ӷ�
public synchronized void removeLine(int row) {
	for (int i = row; i > 0; i--) {
		for (int j = 0; j < cols; j++)
			boxes[i][j] = (ErsBox) boxes[i - 1][j].clone();
	}
	score += ErsBlocksGame.PER_LINE_SCORE;
	scoreForLevelUpdate += ErsBlocksGame.PER_LINE_SCORE;
	repaint();
}
	//���û������û���Ϊ0
public void reset() {
	score = 0;
	scoreForLevelUpdate = 0;
	for (int i = 0; i < boxes.length; i++) {
		for (int j = 0; j < boxes[i].length; j++)
			boxes[i][j].setColor(false);
	}
	repaint();
}
}