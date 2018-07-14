package com.fieryslug.simpletimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;

public class PanelTimer extends JPanel {

    public SlugTime time;
    private SlugTime copy;
    private JLabel labelTime;

    private static final String ACTION_TOGGLE_FULLSCREEN = "action-toggle-fullscreen";
    private static final String ACTION_TOGGLE_TIMER = "action-toggle-timer";
    private static final String ACTION_RESET_TIMER = "action-reset-timer";
    private static final String ACTION_EDIT_TIMER = "action-edit-timer";
    private static final String ACTION_CONFIRM_TIMER = "action-confirm-timer";
    private boolean isFullScreen = false;

    private long start;
    private boolean isRunning;
    private int caretPos = -1;

    private java.util.Timer timer;
    private TimerTask timerTask;

    public PanelTimer() {

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        double screenX = dimension.getWidth();
        double screenY = dimension.getHeight();
        System.out.println(screenX);

        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(Color.BLACK);

        time = new SlugTime(6, 0, 0);
        copy = (SlugTime)time.clone();


        timer = new java.util.Timer();


        InputMap inputMap = getInputMap();
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("F11"), ACTION_TOGGLE_FULLSCREEN);
        inputMap.put(KeyStroke.getKeyStroke("SPACE"), ACTION_TOGGLE_TIMER);
        inputMap.put(KeyStroke.getKeyStroke("R"), ACTION_RESET_TIMER);
        inputMap.put(KeyStroke.getKeyStroke("E"), ACTION_EDIT_TIMER);
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), ACTION_CONFIRM_TIMER);
        actionMap.put(ACTION_TOGGLE_FULLSCREEN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                Window window = SwingUtilities.getWindowAncestor(PanelTimer.this);
                if(device.getFullScreenWindow() == window) {
                    device.setFullScreenWindow(null);
                }
                else {
                    device.setFullScreenWindow(window);
                }
            }
        });
        actionMap.put(ACTION_TOGGLE_TIMER, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {


                if(!isRunning && caretPos == -1) {

                    isRunning = true;

                    labelTime.setForeground(Color.WHITE);

                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            time.decrementInDecis();
                            if(time.getDeci() == 0) {
                                if (time.isZero())
                                    labelTime.setForeground(new Color(192, 98, 89));
                                labelTime.setText(time.toStringWithoutDecis());
                            }

                        }
                    };
                    timer = new java.util.Timer();
                    timer.scheduleAtFixedRate(timerTask, 100, 100);
                }
                else {
                    isRunning = false;
                    timer.cancel();
                }

            }
        });
        actionMap.put(ACTION_RESET_TIMER, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                isRunning = false;
                time = (SlugTime)copy.clone();
                labelTime.setForeground(new Color(94, 214, 119));
                labelTime.setText(time.toStringWithoutDecis());
                timer.cancel();

            }
        });
        actionMap.put(ACTION_EDIT_TIMER, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!isRunning) {
                    caretPos = 0;
                    labelTime.setForeground(new Color(77, 118, 210));
                    labelTime.setText(colorStringHtml(time.toStringWithoutDecis(), 0, 1));
                }
            }
        });
        actionMap.put(ACTION_CONFIRM_TIMER, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(caretPos != -1) {

                    caretPos = -1;
                    String s = removeHtmlTags(labelTime.getText());

                    int hour = Integer.valueOf(s.substring(0, 2));
                    int minute = Math.min(59, Integer.valueOf(s.substring(3, 5)));
                    int second = Math.min(59, Integer.valueOf(s.substring(6, 8)));

                    time.setTime(hour, minute, second, 0);
                    copy = (SlugTime)time.clone();
                    isRunning = false;
                    labelTime.setForeground(new Color(94, 214, 119));
                    labelTime.setText(time.toStringWithoutDecis());


                }

            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

                if(caretPos != -1) {

                    char c = keyEvent.getKeyChar();
                    if(Character.isDigit(c)) {

                        int i = Integer.valueOf(String.valueOf(c));

                        char[] s = removeHtmlTags(labelTime.getText()).toCharArray();
                        s[caretPos] = c;
                        String n = new String(s);


                        if(caretPos % 3 == 0) {
                            caretPos += 1;
                        }
                        else if (caretPos != 7 && caretPos % 3 == 1) {
                            caretPos += 2;
                        }
                        labelTime.setText(colorStringHtml(n, caretPos, caretPos+1));


                    }

                }

            }
        });

        labelTime = new JLabel(time.toStringWithoutDecis(), SwingConstants.CENTER);
        labelTime.setForeground(new Color(94, 214, 119));
        labelTime.setPreferredSize(new Dimension(1000, (int)screenY));
        labelTime.setFont(new Font("MONOSPACED", Font.PLAIN, 200));




        add(labelTime);

        labelTime.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
                Point point = MouseInfo.getPointerInfo().getLocation();
                labelTime.getGraphics().drawString("hello", 100, 100);
            }
        });

    }

    private static String colorTimeHtml(String s, int i) {

        String tagHtml = "<html>";
        String tagCloseHtml = "</html>";
        String tagFont = "<font color=\"3ABFFF\">";
        String tagCloseFont = "</font>";


        switch (i) {

            case 0:
                return tagHtml + tagFont + s.substring(0, 2) + tagCloseFont + s.substring(2, 8) + tagCloseHtml;
            case 1:
                return tagHtml + s.substring(0, 3) + tagFont + s.substring(3, 5) + tagCloseFont + s.substring(5, 8) + tagCloseHtml;
            case 2:
                return tagHtml + s.substring(0, 6) + tagFont + s.substring(6, 8) + tagCloseFont + tagCloseHtml;

        }

        return tagHtml + s + tagCloseHtml;

    }

    private static String colorStringHtml(String s, int start, int end) {

        String tagHtml = "<html>";
        String tagCloseHtml = "</html>";
        String tagFont = "<font color=\"3ABFFF\">";
        String tagCloseFont = "</font>";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tagHtml);

        for(int i=0; i<s.length(); ++i) {

            if(i == start)
                stringBuilder.append(tagFont);
            if(i == end)
                stringBuilder.append(tagCloseFont);

            stringBuilder.append(s.charAt(i));

        }
        stringBuilder.append(tagCloseHtml);

        return stringBuilder.toString();

    }

    private static String colorTimeHtml(SlugTime time, int i) {

        return colorTimeHtml(time.toStringWithoutDecis(), i);

    }

    private static String removeHtmlTags(String s) {

        StringBuilder stringBuilder = new StringBuilder();
        boolean inTag = false;

        for(int i=0; i<s.length(); ++i) {

            char c = s.charAt(i);

            if(c == '<' && !inTag) {
                inTag = true;
            }

            if(!inTag) {
                stringBuilder.append(c);
            }

            if(c == '>' && inTag) {
                inTag = false;
            }


        }

        return stringBuilder.toString();

    }



}
