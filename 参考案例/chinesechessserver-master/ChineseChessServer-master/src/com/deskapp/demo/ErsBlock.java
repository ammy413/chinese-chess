package com.deskapp.demo;

/* File: ErsBlock.java
 *����˹����� Java ʵ�� */
class ErsBlock extends Thread {
	 // һ����ռ��������4��
	public final static int BOXES_ROWS = 4;
	// һ����ռ��������4��
	public final static int BOXES_COLS = 4;
	// �������仯ƽ�������ӣ�������󼸼�֮����ٶ�����һ��
	public final static int LEVEL_FLATNESS_GENE = 3;
	//���������֮�䣬��ÿ����һ�е�ʱ����Ϊ����(����)
	public final static int BETWEEN_LEVELS_DEGRESS_TIME = 50;
	//�������ʽ��ĿΪ7
	private final static int BLOCK_KIND_NUMBER = 7;
	//ÿһ����ʽ�ķ���ķ�ת״̬����Ϊ4
	private final static int BLOCK_STATUS_NUMBER = 4;
	//�ֱ��Ӧ��7��ģ�͵ķ�ת�õ���28��״̬
	public final static int[][] STYLES = {// ��28��״̬
		{0x0f00, 0x4444, 0x0f00, 0x4444}, // �����͵�����״̬
		{0x04e0, 0x0464, 0x00e4, 0x04c4}, // 'T'�͵�����״̬
		{0x4620, 0x6c00, 0x4620, 0x6c00}, // ��'Z'�͵�����״̬
		{0x2640, 0xc600, 0x2640, 0xc600}, // 'Z'�͵�����״̬
		{0x6220, 0x1700, 0x2230, 0x0740}, // '7'�͵�����״̬
		{0x6440, 0x0e20, 0x44c0, 0x8e00}, // ��'7'�͵�����״̬
		{0x0660, 0x0660, 0x0660, 0x0660}, // ���������״̬
	};
	private GameCanvas canvas;
	private ErsBox[][] boxes = new ErsBox[BOXES_ROWS][BOXES_COLS];
	private int style, y, x, level;
	private boolean pausing = false, moving = true;
	//���캯��������һ���ض��Ŀ�
	public ErsBlock(int style, int y, int x, int level, GameCanvas canvas) {
		this.style = style;
		this.y = y;
		this.x = x;
		this.level = level;
		this.canvas = canvas;
		int key = 0x8000;
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				boolean isColor = ((style & key) != 0);
				boxes[i][j] = new ErsBox(isColor);
				key >>= 1;
			}
		}
		display();
	}
	public void run() {
		while (moving) {
			try {
			   sleep(BETWEEN_LEVELS_DEGRESS_TIME*
				   (ErsBlocksGame.MAX_LEVEL- level + LEVEL_FLATNESS_GENE));
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			//��ߵ�moving�Ǳ�ʾ�ڵȴ���100����䣬movingû���ı�
			if (!pausing) moving = (moveTo(y + 1, x) && moving);
		}
	}
	//�������ƶ�һ��
	public void moveLeft() {
		moveTo(y, x - 1);
	}
	//�������ƶ�һ��
	public void moveRight() {
		moveTo(y, x + 1);
	}
	//��������һ��
	public void moveDown() {
		moveTo(y + 1, x);
	}
	//�����
	public void turnNext() {
		for (int i = 0; i < BLOCK_KIND_NUMBER; i++) {
			for (int j = 0; j < BLOCK_STATUS_NUMBER; j++) {
				if (STYLES[i][j] == style) {
					int newStyle = STYLES[i][(j + 1) % BLOCK_STATUS_NUMBER];
					turnTo(newStyle);
					return;
				}
			}
		}
	}
	//��ͣ������䣬��Ӧ��Ϸ��ͣ
	public void pauseMove() {
		pausing = true;
	}
	    //����������䣬��Ӧ��Ϸ����
	public void resumeMove() {
		pausing = false;
	}
		//ֹͣ������䣬��Ӧ��Ϸֹͣ
	public void stopMove() {
		moving = false;
	}
	//����ǰ��ӻ����Ķ�Ӧλ��Ĩȥ��Ҫ�ȵ��´��ػ�����ʱ���ܷ�ӳ����
	private void earse() {
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j].isColorBox()) {
					ErsBox box = canvas.getBox(i + y, j + x);
					if (box == null) continue;
					box.setColor(false);
				}
			}
		}
	}
	//�õ�ǰ���ڻ����Ķ�Ӧλ������ʾ������Ҫ�ȵ��´��ػ�����ʱ���ܿ���
	private void display() {
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j].isColorBox()) {
					ErsBox box = canvas.getBox(y + i, x + j);
					if (box == null) continue;
					box.setColor(true);
				}
			}
		}
	}
	//��ǰ���ܷ��ƶ���newRow/newCol��ָ����λ��
	private boolean isMoveAble(int newRow, int newCol) {
		earse();
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j].isColorBox()) {
					ErsBox box = canvas.getBox(newRow + i, newCol + j);
	                //�����ǰλ�ò����Ƿ�����������ķ��飬�����ƶ�
					if (box == null || (box.isColorBox())) {
						display();
						return false;
					}
				}
			}
		}
		display();
		return true;
	}
	    //����ǰ���ƶ���newRow/newCol��ָ����λ��
	private synchronized boolean moveTo(int newRow, int newCol) {
		//������µ��в����ƶ���ֹͣ�ƶ��򷵻ؼ�
		if (!isMoveAble(newRow, newCol) || !moving) return false;
		earse();   //Ĩ���ɵĵĺۼ�
		y = newRow;   //���µ�λ���ػ�
		x = newCol;
		display();
		canvas.repaint();
		return true;
	}
	       //��ǰ���ܷ���newStyle��ָ���Ŀ���ʽ����Ҫ��Ҫ����
	private boolean isTurnAble(int newStyle) {
		int key = 0x8000;
		earse();
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				//���4��4�ĸ����ڣ���ģʽ�ĸ����Ƿ�Ϊ����
				if ((newStyle & key) != 0) {
					//��鵱ǰ�����Ƿ�Ϊ������ڲ��Ҳ�������
					//������ת��
					ErsBox box = canvas.getBox(y + i, x + j);
					if (box == null || box.isColorBox()) {
						display();
						return false;
					}
				}
				key >>= 1;
			}
		}
		display();
		return true;
	}
	    //����ǰ����newStyle��ָ���Ŀ���ʽ
	private boolean turnTo(int newStyle) {
		if (!isTurnAble(newStyle) || !moving) return false;
		earse();
		int key = 0x8000;
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				boolean isColor = ((newStyle & key) != 0);
				boxes[i][j].setColor(isColor);
				key >>= 1;
			}
		}
		style = newStyle;
		display();
		canvas.repaint();
		return true;
	}
}
