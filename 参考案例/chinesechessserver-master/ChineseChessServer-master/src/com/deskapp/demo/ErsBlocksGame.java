package com.deskapp.demo;

/*  ����˹����� Java ʵ��
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class ErsBlocksGame extends JFrame {
	//ÿ����һ�мƶ��ٷ�
	public final static int PER_LINE_SCORE = 100;
	//�����ٷ��Ժ�������
	public final static int PER_LEVEL_SCORE = PER_LINE_SCORE * 20;
	//�������10��
	public final static int MAX_LEVEL = 10;
	// Ĭ�ϼ�����5
	public final static int DEFAULT_LEVEL = 5;
	  //һ��GameCanvas�������ʵ�����ã�
 	private GameCanvas canvas;
       // һ�����浱ǰ���(ErsBlock)ʵ�������ã�
	private ErsBlock block;
	    // һ�����浱ǰ������壨ControlPanel��ʵ��������;
	private ControlPanel ctrlPanel;
    	private boolean playing = false;
	private JMenuBar bar = new JMenuBar();
	//�˵�������4���˵�
	private JMenu
	        mGame = new JMenu("��Ϸ"),
			mControl = new JMenu("����"),
			mWindowStyle = new JMenu("���ڷ��"),
			mInfo = new JMenu("����");
	//4���˵��зֱ�����Ĳ˵���
	private JMenuItem
	        miNewGame = new JMenuItem("����Ϸ"),
			miSetBlockColor = new JMenuItem("���÷�����ɫ"),
			miSetBackColor = new JMenuItem("���ñ�����ɫ"),
			miTurnHarder = new JMenuItem("�����Ѷ�"),
			miTurnEasier = new JMenuItem("�����Ѷ�"),
			miExit = new JMenuItem("�˳�"),

			miPlay = new JMenuItem("��ʼ"),
			miPause = new JMenuItem("��ͣ"),
			miResume = new JMenuItem("����"),
			miStop = new JMenuItem("ֹͣ"),

			miAuthor = new JMenuItem("���� : Java��Ϸ�����"),
			miSourceInfo = new JMenuItem("�汾��1.0");
         //���ô��ڷ��Ĳ˵�
        	private JCheckBoxMenuItem
	        miAsWindows = new JCheckBoxMenuItem("Windows"),
			miAsMotif = new JCheckBoxMenuItem("Motif"),
			miAsMetal = new JCheckBoxMenuItem("Metal", true);
	// ����Ϸ��Ĺ��캯��
	public ErsBlocksGame(String title) {
		super(title);
		//��ʼ���ڵĴ�С���û��ɵ���
		setSize(315, 392);
                this.setResizable(false);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
      	 //����Ϸ����������Ļ����
		setLocation((scrSize.width - getSize().width) / 2,
		        (scrSize.height - getSize().height) / 2);
      	createMenu();   //�����˵�
		Container container = getContentPane();
		// ���ֵ�ˮƽ����֮����6�����صľ���
		container.setLayout(new BorderLayout(6, 0));
      	 // ����20������ߣ�12����������Ϸ����
		canvas = new GameCanvas(20, 12);
	 	 //����һ���������
		ctrlPanel = new ControlPanel(this);
		 //��Ϸ�����Ϳ������֮�����Ұڷ�
		container.add(canvas, BorderLayout.CENTER);
		container.add(ctrlPanel, BorderLayout.EAST);
      	//���Ӵ��ڼ�����
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				stopGame();
				System.exit(0);
			}
		});
        //���ӹ�������������һ�������ı��С���͵���fanning()������
	     //�Զ���������ĳߴ�
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ce) {
				canvas.fanning();
			}
		});
		setVisible (true);		
		// ���ݴ��ڵĴ�С���Զ���������ĳߴ�
		canvas.fanning();
	}
	public void reset() {
		ctrlPanel.reset();      //���ƴ��ڸ�λ
		canvas.reset();        //��Ϸ���帴λ
	}
	public boolean isPlaying() {   //�ж���Ϸ�Ƿ��ڽ���
		return playing;
	}
	 //�õ���ǰ��Ŀ�
	public ErsBlock getCurBlock() {
		return block;
	}
	//�õ���ǰ����
	public GameCanvas getCanvas() {
		return canvas;
	}
	public void playGame() {    //��ʼ��Ϸ
		play();
		ctrlPanel.setPlayButtonEnable(false);
		miPlay.setEnabled(false);
		ctrlPanel.requestFocus();
	}
	public void pauseGame() {          //��Ϸ��ͣ
		if (block != null) block.pauseMove();
		ctrlPanel.setPauseButtonLabel(false);
		miPause.setEnabled(false);
		miResume.setEnabled(true);
	}
	public void resumeGame() {             //����ͣ�е���Ϸ����
		if (block != null) block.resumeMove();
		ctrlPanel.setPauseButtonLabel(true);
		miPause.setEnabled(true);
		miResume.setEnabled(false);
		ctrlPanel.requestFocus();
	}
	public void stopGame() {             //�û�ֹͣ��Ϸ
		playing = false;
		if (block != null) block.stopMove();
		miPlay.setEnabled(true);
		miPause.setEnabled(true);
		miResume.setEnabled(false);
		ctrlPanel.setPlayButtonEnable(true);
		ctrlPanel.setPauseButtonLabel(true);
	}
	 //�õ���ǰ��Ϸ�����õ���Ϸ�Ѷ�, ��Ϸ�Ѷ�1��MAX_LEVEL��10
	public int getLevel() {
		return ctrlPanel.getLevel();
	}
	//���û�������Ϸ�Ѷ�ϵ��, 
	public void setLevel(int level) {
		if (level < 11 && level > 0) ctrlPanel.setLevel(level);
	}
	public int getScore() {        //�õ���Ϸ����
		if (canvas != null) return canvas.getScore();
		return 0;
	}
	//�õ����ϴ�������������Ϸ���֣������Ժ󣬴˻�������
	public int getScoreForLevelUpdate() {
		if (canvas != null) return canvas.getScoreForLevelUpdate();
		return 0;
	}
	//�������ۼƵ�һ��������ʱ����һ�μ�
	public boolean levelUpdate() {
		int curLevel = getLevel();
		if (curLevel < MAX_LEVEL) {
			setLevel(curLevel + 1);
			canvas.resetScoreForLevelUpdate();
			return true;
		}
		return false;
	}
	//��Ϸ��ʼ
	private void play() {
		reset();
		playing = true;
		Thread thread = new Thread(new Game());
		thread.start();
	}
		//������Ϸ������
	private void reportGameOver() {
		JOptionPane.showMessageDialog(this, "��Ϸ����!");
	}
		//���������ô��ڲ˵�
	private void createMenu() {
		bar.add(mGame);
		bar.add(mControl);
		bar.add(mWindowStyle);
		bar.add(mInfo);
		mGame.add(miNewGame);
		mGame.addSeparator();
		mGame.add(miSetBlockColor);
		mGame.add(miSetBackColor);
		mGame.addSeparator();
		mGame.add(miTurnHarder);
		mGame.add(miTurnEasier);
		mGame.addSeparator();
		mGame.add(miExit);
		mControl.add(miPlay);
		mControl.add(miPause);
		mControl.add(miResume);
		mControl.add(miStop);
		mWindowStyle.add(miAsWindows);
		mWindowStyle.add(miAsMotif);
		mWindowStyle.add(miAsMetal);
		mInfo.add(miAuthor);
		mInfo.add(miSourceInfo);
		setJMenuBar(bar);
		miPause.setAccelerator(
		        KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
		miResume.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
		miNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				stopGame();
				reset();
				setLevel(DEFAULT_LEVEL);
			}
		});
        //JColorChooser���ṩһ����׼��Gui���������û�ѡ��ɫ��
		//ʹ��JColorChooser�ķ���ѡ�񷽿����ɫ
		miSetBlockColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Color newFrontColor =
				        JColorChooser.showDialog(ErsBlocksGame.this,
				                "���÷�����ɫ", canvas.getBlockColor());
				if (newFrontColor != null)
					canvas.setBlockColor(newFrontColor);
			}
		}); 
	    //ʹ��JColorChooser�ķ���ѡ����Ϸ���ı�����ɫ
		miSetBackColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Color newBackColor =
				        JColorChooser.showDialog(ErsBlocksGame.this,
				                "���ñ�����ɫ", canvas.getBackgroundColor());
				if (newBackColor != null)
					canvas.setBackgroundColor(newBackColor);
			}
		});
		//ʹ��Ϸ���Ѷȼ������ӵĲ˵���
		miTurnHarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int curLevel = getLevel();
				if (curLevel < MAX_LEVEL) setLevel(curLevel + 1);
			}
		});
		//ʹ��Ϸ���Ѷȼ��𽵵͵Ĳ˵���
		miTurnEasier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int curLevel = getLevel();
				if (curLevel > 1) setLevel(curLevel - 1);
			}
		});
        //�˳���Ϸ�Ĳ˵���
		miExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
        //��ʼ��Ϸ�Ĳ˵���
		miPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				playGame();
			}
		});
		//��ͣ��Ϸ�Ĳ˵���
		miPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pauseGame();
			}
		});
		//�ָ���Ϸ�Ĳ˵���
		miResume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				resumeGame();
			}
		});
		//ֹͣ��Ϸ�Ĳ˵���
		miStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				stopGame();
			}
		});
		//����������Ϸ�Ĵ��ڷ��������˵���
		miAsWindows.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String plaf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
				setWindowStyle(plaf);
				canvas.fanning();
				ctrlPanel.fanning();
				miAsWindows.setState(true);
				miAsMetal.setState(false);
				miAsMotif.setState(false);
			}
		});
		miAsMotif.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String plaf = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
				setWindowStyle(plaf);
				canvas.fanning();
				ctrlPanel.fanning();
				miAsWindows.setState(false);
				miAsMetal.setState(false);
				miAsMotif.setState(true);
			}
		});
		miAsMetal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String plaf = "javax.swing.plaf.metal.MetalLookAndFeel";
				setWindowStyle(plaf);
				canvas.fanning();
				ctrlPanel.fanning();
				miAsWindows.setState(false);
				miAsMetal.setState(true);
				miAsMotif.setState(false);
			}
		});
	}
	//�����ִ����ô������
	private void setWindowStyle(String plaf) {
		try {
            //�趨�û���������
			UIManager.setLookAndFeel(plaf);
      		 //���û�����ĳɵ�ǰ�趨�����
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
		}
	}
	private class Game implements Runnable {
		public void run() {    //������ɿ�ĳ�ʼ�е�λ��
			     			//������ɿ�ĳ�ʼ��̬��28��֮һ��
			int col = (int) (Math.random() * (canvas.getCols() - 3)),
			  style = ErsBlock.STYLES[(int) (Math.random() * 7)][(int) (Math.random() * 4)];
			while (playing) {
				if (block != null) {    //��һ��ѭ��ʱ��blockΪ��
					if (block.isAlive()) {
						try {
							Thread.currentThread().sleep(100);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
						continue;
					}
				}
				checkFullLine();        //����Ƿ���ȫ��������
				if (isGameOver()) {     //�����Ϸ�Ƿ�Ӧ�ý�����
					miPlay.setEnabled(true);
					miPause.setEnabled(true);
					miResume.setEnabled(false);
					ctrlPanel.setPlayButtonEnable(true);
					ctrlPanel.setPauseButtonLabel(true);
					reportGameOver();
					return;
				}
                //����һ����Ϸ��
				block = new ErsBlock(style, -1, col, getLevel(), canvas);
				//��Ϊ�߳̿�ʼ����
				block.start();
	     		//���������һ����ĳ�ʼ�е�λ��
		     	//���������һ����ĳ�ʼ��̬��28��֮һ��
				col = (int) (Math.random() * (canvas.getCols() - 3));
				style = ErsBlock.STYLES[(int) (Math.random()*7)][(int) (Math.random()*4)];
				//�ڿ����������ʾ��һ�������״
				ctrlPanel.setTipStyle(style);
			}
		}
		 //��黭�����Ƿ���ȫ�������У�����о�ɾ��֮
		public void checkFullLine() {
			for (int i = 0; i < canvas.getRows(); i++) {
				int row = -1;
				boolean fullLineColorBox = true;
				for (int j = 0; j < canvas.getCols(); j++) {
                           //����i�У���j���Ƿ�Ϊ��ɫ����
					if (!canvas.getBox(i, j).isColorBox()) {
						   //�ǲ�ɫ���飬
						fullLineColorBox = false;
						break;
						//�˳���ѭ���������һ��
					}
				}
				if (fullLineColorBox) {
					row = i--;
					canvas.removeLine(row);
					//��������������ȥ
				}
			}
		}
		//��������Ƿ�ռ���ж���Ϸ�Ƿ��Ѿ������ˡ�
		private boolean isGameOver() {
			for (int i = 0; i < canvas.getCols(); i++) {
				ErsBox box = canvas.getBox(0, i);
				if (box.isColorBox()) return true;
			}
			return false;
		}
	}
	public static void main(String[] args) {
		new ErsBlocksGame("����˹������Ϸ");
	}
}
