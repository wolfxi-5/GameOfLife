import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameGUI_fix extends JFrame implements ActionListener {

    private GameMap_fix map;
    private int maxLength = 0, maxWidth = 0; //长和宽
    private JButton[][] cell; //一个按钮表示一个细胞
    private JButton setting, nowGeneration, clearGeneration; //设置长宽，当前代数，代数重置
    private JButton randomInit, clearCell, startReproduce, nextGeneration, stop, exit; //细胞清零，下一代，开始繁衍，暂停，退出
    private JTextField lengthText, widthText;
    private boolean isRunning;
    private Thread thread;

    public GameGUI_fix(String name) throws HeadlessException {
        super(name);
        initGameGUI();
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    //GUI初始化
    public void initGameGUI() {

        if (maxWidth <= 0 || maxWidth >= 100) {
            maxWidth = 30;
            lengthText = new JTextField("30", 2);
        }
        if (maxLength <= 0 || maxLength >= 100) {
            maxLength = 30;
            widthText = new JTextField("30", 2);
        }
        map = new GameMap_fix(maxLength, maxWidth);


        //设置布局
        JPanel backPanel, centerPanel, rightPanel;
        JLabel jLength, jWidth, jNowGeneration;

        backPanel = new JPanel(new BorderLayout());
        centerPanel = new JPanel(new GridLayout(maxWidth, maxLength));
        rightPanel = new JPanel(new GridLayout(7, 1, 0, 25));

        this.setContentPane(backPanel);
        backPanel.add(centerPanel, "Center");
        backPanel.add(rightPanel, "East");


        // 初始化细胞按钮
        cell = new JButton[maxLength][maxWidth];
        for (int i = 0; i < maxLength; i++) {
            for (int j = 0; j < maxWidth; j++) {
                cell[i][j] = new JButton("");
                cell[i][j].setBackground(Color.WHITE);
                centerPanel.add(cell[i][j]);
            }
        }


        //设置标签，按钮
        jLength = new JLabel("长度：");
        jWidth = new JLabel("宽度：");
        setting = new JButton("确定");

        jNowGeneration = new JLabel(" 当前代数：");
        nowGeneration = new JButton("" + map.getGeneration());
        nowGeneration.setEnabled(false);
        clearGeneration = new JButton("重置");

        randomInit = new JButton("随机初始化");
        clearCell = new JButton("细胞清零");
        startReproduce = new JButton("开始繁衍");
        nextGeneration = new JButton("下一代");
        stop = new JButton("暂停");
        exit = new JButton("退出");

        JPanel panelSet = new JPanel(new GridLayout(3, 3));
        JLabel blank = new JLabel("");
        panelSet.add(jLength);
        panelSet.add(lengthText);
        panelSet.add(blank);
        panelSet.add(jWidth);
        panelSet.add(widthText);
        panelSet.add(setting);
        panelSet.add(jNowGeneration);
        panelSet.add(nowGeneration);
        panelSet.add(clearGeneration);

        rightPanel.add(panelSet);
        rightPanel.add(randomInit);
        rightPanel.add(clearCell);
        rightPanel.add(startReproduce);
        rightPanel.add(nextGeneration);
        rightPanel.add(stop);
        rightPanel.add(exit);

        rightPanel.setPreferredSize(new Dimension(200, 0));


        // 设置窗口
        this.setSize(1000, 650);
        this.setResizable(true);
        this.setLocationRelativeTo(null); // 让窗口在屏幕居中
        this.setVisible(true);// 将窗口设置为可见的
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        //绑定监听事件
        setting.addActionListener(this);
        clearGeneration.addActionListener(this);
        randomInit.addActionListener(this);
        clearCell.addActionListener(this);
        nextGeneration.addActionListener(this);
        startReproduce.addActionListener(this);
        stop.addActionListener(this);
        exit.addActionListener(this);
        for (int i = 0; i < maxLength; i++) {
            for (int j = 0; j < maxWidth; j++) {
                cell[i][j].addActionListener(this);
            }
        }

    }

    //更改此状态下细胞的状态
    public void showMap() {
        boolean[][] grid = map.getMap();
        for (int i = 0; i < maxLength; i++) {
            for (int j = 0; j < maxWidth; j++) {
                if (grid[i + 1][j + 1]) {
                    cell[i][j].setBackground(Color.BLACK);
                } else {
                    cell[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    //繁殖下一代
    public void goNextGeneration() {
        map.repoduce();
        showMap();
        nowGeneration.setText("" + map.getGeneration());
    }

    //点击cell改变事件
    public void changeCell(ActionEvent e) {
        boolean[][] grid = map.getMap();
        for (int i = 0; i < maxLength; i++) {
            for (int j = 0; j < maxWidth; j++) {
                if (e.getSource() == cell[i][j]) {
                    if (grid[i + 1][j + 1]) {
                        cell[i][j].setBackground(Color.WHITE);
                        grid[i + 1][j + 1] = false;
                    } else {
                        cell[i][j].setBackground(Color.BLACK);
                        grid[i + 1][j + 1] = true;
                    }
                    break;
                }
            }
        }
        map.setMap(grid);
    }

    //事件监听
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == setting) {
            int length = Integer.parseInt(this.lengthText.getText());
            int width = Integer.parseInt(this.widthText.getText());
            if (length <= 0 || width <= 0 || length >= 100 || width >= 100) {
                this.maxLength = 0;
                this.maxWidth = 0;
                initGameGUI();
            } else {
                this.maxLength = length;
                this.maxWidth = width;
                initGameGUI();
            }
        } else if (e.getSource() == clearGeneration) {
            map.setGeneration(0);
            nowGeneration.setText("" + map.getGeneration());
            isRunning = false;
            thread = null;
        } else if (e.getSource() == randomInit) {
            map.resetMap();
            showMap();
            isRunning = false;
            thread = null;
        } else if (e.getSource() == clearCell) {
            map.clearMap();
            showMap();
            map.setGeneration(0);
            nowGeneration.setText("" + map.getGeneration());
            isRunning = false;
            thread = null;
        } else if (e.getSource() == startReproduce) {
            isRunning = true;
            thread = new Thread(new Runnable() {
                public void run() {
                    boolean[][] grid = map.getMap();
                    boolean isDead;
                    while (isRunning) {
                        goNextGeneration();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }

                        isDead = true;
                        for (int i = 1; i <= maxLength; i++) {
                            for (int j = 0; j < maxWidth; j++) {
                                if (grid[i][j]) {
                                    isDead = false;
                                    break;
                                }
                            }
                        }
                        if (isDead) {
                            isRunning = false;
                            thread = null;
                        }
                    }
                }
            });
            thread.start();
        } else if (e.getSource() == nextGeneration) {
            goNextGeneration();
            isRunning = false;
            thread = null;
        } else if (e.getSource() == stop) {
            isRunning = false;
            thread = null;
        } else if (e.getSource() == exit) {
            System.exit(0);
        } else {
            changeCell(e);
        }
    }
}
