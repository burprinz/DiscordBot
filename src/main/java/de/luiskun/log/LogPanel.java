package de.luiskun.log;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class LogPanel extends JPanel {

    private static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private static final int WIDTH = 900;
    private static final int HEIGHT = 500;

    private List<LogEntry> logs;

    public LogPanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setLayout(null);
    }

    public void updateLogs(List<LogEntry> logs) {
        this.logs = logs;
        repaint();
    }


    private static final int ID_WIDTH = 30;
    private static final int TIME_WIDTH = 140;
    private static final int TYPE_WIDTH = 55;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);


        g2.setColor(Color.WHITE);
        g2.setFont(Log.logFont.deriveFont(Font.PLAIN, 17));

        g2.drawLine(0, 16, WIDTH, 16);

        int x = 0;
        int y = 13;
        String text = "LINE";
        g2.drawString(text, x+getXForCenteredText(g2, text, ID_WIDTH), y);
        x += ID_WIDTH;
        g2.drawLine(x, 0, x, HEIGHT);
        text = "TIME";
        g2.drawString(text, x+getXForCenteredText(g2, text, TIME_WIDTH), y);
        x += TIME_WIDTH;
        g2.drawLine(x, 0, x, HEIGHT);
        text = "TYPE";
        g2.drawString(text, x+getXForCenteredText(g2, text, TYPE_WIDTH), y);
        x += TYPE_WIDTH;
        g2.drawLine(x, 0, x, HEIGHT);
        text = "MESSAGE";
        g2.drawString(text, x+getXForCenteredText(g2, text, WIDTH-x), y);
        y += 2;

        if (logs != null) {
            int start = logs.size()-20;
            if (start < 0) start = 0;
            for (int i = start; i < this.logs.size(); i++) {
                y += 16;
                LogEntry entry = this.logs.get(i);
                x = 0;
                text = ""+entry.entry_num;
                g2.drawString(text, x + getXForCenteredText(g2, text, ID_WIDTH), y);
                x += ID_WIDTH;
                text = entry.time.format(timeFormat);
                g2.drawString(text, x + getXForCenteredText(g2, text, TIME_WIDTH), y);
                x += TIME_WIDTH;
                g2.setColor(Log.typeColors[entry.type]);
                text = Log.typeStrings[entry.type];
                g2.drawString(text, x + getXForCenteredText(g2, text, TYPE_WIDTH), y);
                g2.setColor(Color.WHITE);
                x += TYPE_WIDTH;
                g2.drawString(entry.message, x+5, y);
            }
        }
    }


    public static int getXForCenteredText(Graphics2D g2, String text, int width) {
        return width / 2 - ((int) g2.getFontMetrics().getStringBounds(text, g2).getWidth()) / 2;
    }
}
