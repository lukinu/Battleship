package org.lukin.game.seabattle.View;

import org.lukin.game.seabattle.Controller.Main;
import org.lukin.game.seabattle.Model.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class GuiView implements Serializable {

    JFrame frame;
    JPanel controlPanel;
    JPanel names;
    JPanel leftPanel;
    JPanel rightPanel;
    JFileChooser fileChooser;
    ArrayList<JButton> leftField = new ArrayList<JButton>(100);
    ArrayList<JButton> rightField = new ArrayList<JButton>(100);
    JLabel outputString;
    JLabel firstPlayer;
    JLabel secondPlayer;
    JToolBar toolBar;
    JButton exitButton;
    JButton openButton;
    JButton saveButton;
    JButton restartButton;
    JButton setComputerModeButton;
    ImageIcon iExit;
    ImageIcon iSave;
    ImageIcon iOpen;
    ImageIcon iRestart;
    short pressed_x;
    short pressed_y;
    boolean isSmartMode = false;

    public boolean isSmartMode() {
        return isSmartMode;
    }

    public GuiView() {
        iExit = createImageIcon("images/Exit16.png", "exit");
        iSave = createImageIcon("images/SaveAll16.gif", "save");
        iOpen = createImageIcon("images/Open16.gif", "open");
        iRestart = createImageIcon("images/Refresh16.gif", "restart game");

        frame = new JFrame("SeaBattle");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setPreferredSize(new Dimension(450, 330));

        firstPlayer = new JLabel("player one");
        firstPlayer.setHorizontalAlignment(JLabel.CENTER);
        secondPlayer = new JLabel("player two");
        secondPlayer.setHorizontalAlignment(JLabel.CENTER);
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 1));
        names = new JPanel();
        names.setLayout(new GridLayout(1, 2));
        names.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        toolBar = new JToolBar();
        outputString = new JLabel(" ");
        outputString.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        setComputerModeButton = new JButton("smart");
        setComputerModeButton.setToolTipText("Computer's smart play on/off");
        setComputerModeButton.setBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED));
        setComputerModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                isSmartMode = !isSmartMode;
                if (isSmartMode)
                    setComputerModeButton.setBorder(BorderFactory.createBevelBorder(EtchedBorder.LOWERED));
                else
                    setComputerModeButton.setBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED));
            }
        });

        exitButton = new JButton(iExit);
        exitButton.setToolTipText("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        openButton = new JButton(iOpen);
        openButton.setToolTipText("Open saved game");
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                fileChooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("*.dat", "dat");
                fileChooser.addChoosableFileFilter(filter);
                int ret = fileChooser.showDialog(toolBar, "Open file");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        Main.readGame(file);
                    } catch (IOException e) {
                        outputMessage("Failed to open file " + file.getName());
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        saveButton = new JButton(iSave);
        saveButton.setToolTipText("Save the game");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                fileChooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("*.dat", "dat");
                fileChooser.addChoosableFileFilter(filter);
                int ret = fileChooser.showDialog(toolBar, "Save file");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        Main.writeGame(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        restartButton = new JButton(iRestart);
        restartButton.setToolTipText("Restart game");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Main.restartGame();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (Game.restartKey) {
                    Game.restartKey.notifyAll();
                }
            }
        });

        // filed panels
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(SeaBattleConstants.FIELD_SIZE, SeaBattleConstants.FIELD_SIZE));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(SeaBattleConstants.FIELD_SIZE, SeaBattleConstants.FIELD_SIZE));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // initialization of field panels
        for (int i = 0; i < SeaBattleConstants.FIELD_SIZE; i++) {
            for (int j = 0; j < SeaBattleConstants.FIELD_SIZE; j++) {
                leftField.add(i * SeaBattleConstants.FIELD_SIZE + j, new JButton());
                leftField.get(i * SeaBattleConstants.FIELD_SIZE + j).setPreferredSize(new Dimension(SeaBattleConstants.CELL_SIZE, SeaBattleConstants.CELL_SIZE));
                leftPanel.add(leftField.get(i * SeaBattleConstants.FIELD_SIZE + j));
                rightField.add(i * SeaBattleConstants.FIELD_SIZE + j, new JButton());
                rightField.get(i * SeaBattleConstants.FIELD_SIZE + j).setPreferredSize(new Dimension(SeaBattleConstants.CELL_SIZE, SeaBattleConstants.CELL_SIZE));
                rightField.get(i * SeaBattleConstants.FIELD_SIZE + j).addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        pressed_x = (short) (rightField.indexOf(actionEvent.getSource()) % SeaBattleConstants.FIELD_SIZE);
                        pressed_y = (short) (rightField.indexOf(actionEvent.getSource()) / SeaBattleConstants.FIELD_SIZE);
                        synchronized (Game.key) {
                            Game.key.notifyAll();
                        }
                    }
                });
                rightPanel.add(rightField.get(i * SeaBattleConstants.FIELD_SIZE + j));
            }
        }
        // composing
        toolBar.add(exitButton);
        toolBar.add(openButton);
        toolBar.add(saveButton);
        toolBar.add(restartButton);
        toolBar.add(setComputerModeButton);
        names.add(firstPlayer);
        names.add(secondPlayer);
        controlPanel.add(toolBar);
        controlPanel.add(names);
        frame.add(controlPanel, BorderLayout.PAGE_START);
        frame.add(leftPanel, BorderLayout.LINE_START);
        frame.add(rightPanel, BorderLayout.LINE_END);
        frame.add(outputString, BorderLayout.PAGE_END);
        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void drawField(Field ownField, Field foesField, Player whoseTurn) {
        firstPlayer.setText(ownField.getOwner().getName());
        secondPlayer.setText(foesField.getOwner().getName());
        if (whoseTurn.equals(ownField.getOwner())) {
            firstPlayer.setForeground(Color.BLACK);
            secondPlayer.setForeground(Color.LIGHT_GRAY);
        } else {
            firstPlayer.setForeground(Color.LIGHT_GRAY);
            secondPlayer.setForeground(Color.BLACK);
        }

        for (short i = 0; i < SeaBattleConstants.FIELD_SIZE; i++) {
            for (short j = 0; j < SeaBattleConstants.FIELD_SIZE; j++) {
                if (ownField.getCell(j, i).getCellState().equals(SeaBattleConstants.SHIP)) {
                    leftField.get(i * SeaBattleConstants.FIELD_SIZE + j).setBackground(Color.BLACK);
                    continue;
                }
                if (ownField.getCell(j, i).getCellState().equals(SeaBattleConstants.HIT)) {
                    leftField.get(i * SeaBattleConstants.FIELD_SIZE + j).setBackground(Color.RED);
                    continue;
                }
                if (ownField.getCell(j, i).getCellState().equals(SeaBattleConstants.MISS)) {
                    leftField.get(i * SeaBattleConstants.FIELD_SIZE + j).setBackground(Color.GRAY);
                    continue;
                }
                leftField.get(i * SeaBattleConstants.FIELD_SIZE + j).setBackground(Color.LIGHT_GRAY);
            }
        }
        for (short i = 0; i < SeaBattleConstants.FIELD_SIZE; i++) {
            for (short j = 0; j < SeaBattleConstants.FIELD_SIZE; j++) {
                if (foesField.getCell(j, i).getCellState().equals(SeaBattleConstants.HIT)) {
                    rightField.get(i * SeaBattleConstants.FIELD_SIZE + j).setBackground(Color.RED);
                    continue;
                }
                if (foesField.getCell(j, i).getCellState().equals(SeaBattleConstants.MISS)) {
                    rightField.get(i * SeaBattleConstants.FIELD_SIZE + j).setBackground(Color.LIGHT_GRAY);
                    continue;
                }
                rightField.get(i * SeaBattleConstants.FIELD_SIZE + j).setBackground(Color.DARK_GRAY);
            }
        }
    }

    private abstract class Dialog extends JDialog {
        JButton close;
        JLabel name;

        private Dialog() {
            add(Box.createRigidArea(new Dimension(0, 10)));
            name = new JLabel();
            name.setAlignmentX(0.5f);
            add(name, BorderLayout.PAGE_START);
            add(Box.createRigidArea(new Dimension(0, 10)));
            close = new JButton("Ok");
            close.setAlignmentX(0.5f);
            add(close, BorderLayout.LINE_END);
            setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setSize(170, 75);
        }
    }

    private class inputtingDialog extends Dialog {
        private JTextField inputText;
        private String userInput;

        private inputtingDialog(String labelString) {
            setTitle(labelString);
            name.setText(labelString);
            inputText = new JTextField(10);
            add(inputText, BorderLayout.CENTER);
            close.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    userInput = inputText.getText();
                    dispose();
                }
            });
            toFront();
        }

        public String getUserInput() {
            return userInput;
        }
    }

    private class choosingDialog extends Dialog {
        private JCheckBox choose;
        private boolean userInput;

        private choosingDialog(String labelString) {
            setTitle(labelString);
            name.setText(labelString);
            choose = new JCheckBox();
            choose.setSelected(true);
            add(choose, BorderLayout.CENTER);
            close.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    userInput = choose.isSelected();
                    dispose();
                }
            });
            toFront();
        }

        public boolean getUserInput() {
            return userInput;
        }
    }

    public String inputPlayerName() {
        inputtingDialog playerNameDialog = new inputtingDialog("Please input your name");
        playerNameDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        playerNameDialog.setVisible(true);
        return playerNameDialog.getUserInput();
    }

    public boolean isAutoPlacement() {
        choosingDialog choosePlacementModeDialog = new choosingDialog("place your ships automatically");
        choosePlacementModeDialog.setVisible(true);
        return choosePlacementModeDialog.getUserInput();
    }

    public void outputMessage(String message) {
        outputString.setText(message);
    }

    public FieldPoint getShot() {
        return new FieldPoint(pressed_x, pressed_y);
    }

    protected ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}