package com.deskapp.demo;

/** File: ControlPanel.java
 *����˹������Ϸ�Ŀ��Ʋ��ֵ�ʵ�� */
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
class ControlPanel extends JPanel {

    private JTextField tfLevel = new JTextField("" + ErsBlocksGame.DEFAULT_LEVEL);
	//��һ���ı�����ʾ�Ѷȼ���
	private JTextField
    //��һ���ı�����ʾ��ҵ÷�
	tfScore = new JTextField("0");
    	private JButton           //����һ����ư�ť
	        btPlay = new JButton("��ʼ"),
	        btPause = new JButton("��ͣ"),
	        btStop = new JButton("ֹͣ"),
	        btTurnLevelUp = new JButton("�����Ѷ�"),
	        btTurnLevelDown = new JButton("�����Ѷ�");
		//��ʾ��һ����Ϸ������
	private JPanel plTip = new JPanel(new BorderLayout());
	private TipPanel plTipBlock = new TipPanel();
	 	//��ʾ��Ϸ��ǰ��Ϣ����壬4��1��
	private JPanel plInfo = new JPanel(new GridLayout(4, 1));
        	//��ſ��ư�ť����壬5��1��
	private JPanel plButton = new JPanel(new GridLayout(5, 1));
   	private Timer timer;
		    //��ǰ����Ϸ��
	private ErsBlocksGame game;
     	//����ͻ����EtchedBorder���͵ı߿�
	private Border border = new EtchedBorder(
	        EtchedBorder.RAISED, Color.WHITE, new Color(148, 145, 140));
	     //�����������Ĺ��캯��
	public ControlPanel(final ErsBlocksGame game) {
		  //�������������3������壬�ڷ���1�У�ÿ�еļ��Ϊ4
		setLayout(new GridLayout(3, 1, 0, 4));
		this.game = game;
	      //Ԥ��ʾ���������������߽�
		plTip.add(new JLabel("��һ������"), BorderLayout.NORTH);
		plTip.add(plTipBlock);
		plTip.setBorder(border);
	        //��Ϸ��Ϣ��ʾ����������ǩ�������ı��򼰱߽�
		plInfo.add(new JLabel("�Ѷȼ���"));
		plInfo.add(tfLevel);
		plInfo.add(new JLabel("�÷�"));
		plInfo.add(tfScore);
		plInfo.setBorder(border);
		//�����ı����ǲ��ɱ༭�ģ�ֻ������ʾ��Ϣ
		tfLevel.setEditable(false);
		tfScore.setEditable(false);
		//��ť���������ť���߽�
		plButton.add(btPlay);
		plButton.add(btPause);
		plButton.add(btStop);
		plButton.add(btTurnLevelUp);
		plButton.add(btTurnLevelDown);
		plButton.setBorder(border);
	     	//��3���ϳ������뵽�������
		add(plTip);
		add(plInfo);
		add(plButton);
			//���Ӽ��̵ļ�����		
		addKeyListener(new ControlKeyListener());
		    //���Ӱ�ť�ļ�����
		btPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				game.playGame();
			}
		});
		btPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (btPause.getText().equals(new String("��ͣ"))) {
					game.pauseGame();
				} else {
					game.resumeGame();
				}
			}
		});
		btStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				game.stopGame();
			}
		});
		btTurnLevelUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					int level = Integer.parseInt(tfLevel.getText());
					if (level < ErsBlocksGame.MAX_LEVEL)
						tfLevel.setText("" + (level + 1));
				} catch (NumberFormatException e) {
				}
				requestFocus();
			}
		});
		btTurnLevelDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					int level = Integer.parseInt(tfLevel.getText());
					if (level > 1)
						tfLevel.setText("" + (level - 1));
				} catch (NumberFormatException e) {
				}
				//requestFocus();
			}
		});
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ce) {
				plTipBlock.fanning();
			}
		});
		timer = new Timer(500, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//��ʾ��ҵĵ÷�
				tfScore.setText("" + game.getScore());
				//���÷ֽ����Ѷȼ���
				int scoreForLevelUpdate =
				        game.getScoreForLevelUpdate();
	            //��ʾ���º���Ѷȼ���
				if (scoreForLevelUpdate >= ErsBlocksGame.PER_LEVEL_SCORE
				        && scoreForLevelUpdate > 0)
					game.levelUpdate();
			}
		});
		timer.start();//������ʱ��
	}
	    //����Ԥ�Դ��ڵ���ʽ��
	public void setTipStyle(int style) {
		plTipBlock.setStyle(style);
	}
	    //ȡ���û����õ���Ϸ�ȼ���
	public int getLevel() {
		int level = 0;
		try {
			level = Integer.parseInt(tfLevel.getText());
		} catch (NumberFormatException e) {	}
		return level;
	}
	    //���û��޸���Ϸ�Ѷȵȼ���
	public void setLevel(int level) {
		if (level > 0 && level < 11) tfLevel.setText("" + level);
	}
		//����"��ʼ"��ť��״̬��
	public void setPlayButtonEnable(boolean enable) {
		btPlay.setEnabled(enable);
	}
	    //����pause��ֵ���ð�ť����ʾ��ǩ
	public void setPauseButtonLabel(boolean pause) {
		btPause.setText(pause ? "��ͣ" : "����");
	}
		// ���ÿ�����壬�÷���Ϊ0
	public void reset() {
		tfScore.setText("0");
		plTipBlock.setStyle(0);
	}
		//���¼���TipPanel���boxes[][]��ķ���Ĵ�С
	public void fanning() {
		plTipBlock.fanning();
	}
		// ��ʾ��Ϣ����ʵ��ϸ����
	private class TipPanel extends JPanel {
		//��ʾ��Ϣ����ǰ���ͱ�����ɫ
		private Color backColor = Color.LIGHT_GRAY, frontColor = Color.red;
		//�����趨�������������Ķ���˹����
		private ErsBox[][] boxes =
		        new ErsBox[ErsBlock.BOXES_ROWS][ErsBlock.BOXES_COLS];
        //�����̬����Ⱥ͸߶�
		private int style, boxWidth, boxHeight;
			//isTiled�Ƿ�ƽ��
		private boolean isTiled = false;
	//Ԥ��ʾ�����๹�캯��
		public TipPanel() {
			for (int i = 0; i < boxes.length; i++) {
				for (int j = 0; j < boxes[i].length; j++)
					boxes[i][j] = new ErsBox(false);
			}
		}
		public TipPanel(Color backColor, Color frontColor) {
			this();
			this.backColor = backColor;
			this.frontColor = frontColor;
		}
			//����Ԥ�Դ��ڵķ�����ʽ
		public void setStyle(int style) {
			this.style = style;
			repaint();
		}
		// ����JComponent��ĺ������������
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (!isTiled) fanning();
			int key = 0x8000;
			for (int i = 0; i < boxes.length; i++) {
				for (int j = 0; j < boxes[i].length; j++) {
					Color color = (((key & style) != 0) ? frontColor : backColor);
					g.setColor(color);
					g.fill3DRect(j * boxWidth, i * boxHeight,
					        boxWidth, boxHeight, true);
					key >>= 1;
				}
			}
		}
		// ���ݴ��ڵĴ�С���Զ���������Ĵ�С
		public void fanning() {
			boxWidth = getSize().width / ErsBlock.BOXES_COLS;
			boxHeight = getSize().height / ErsBlock.BOXES_ROWS;
			isTiled = true;
		}
	}
		//��Ϸ�İ�ť���Ƽ��ļ�����
	private class ControlKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent ke) {
			if (!game.isPlaying()) return;
			ErsBlock block = game.getCurBlock();
			switch (ke.getKeyCode()) {
				case KeyEvent.VK_DOWN:
					block.moveDown();
					break;
				case KeyEvent.VK_LEFT:
					block.moveLeft();
					break;
				case KeyEvent.VK_RIGHT:
					block.moveRight();
					break;
				case KeyEvent.VK_UP:
					block.turnNext();
					break;
				default:
					break;
			}
		}
	}
}
