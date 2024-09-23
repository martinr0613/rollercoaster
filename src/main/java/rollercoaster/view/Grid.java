package rollercoaster.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;

import javax.swing.*;

import rollercoaster.controller.Board;
import rollercoaster.model.Building;
import rollercoaster.model.Game;
import rollercoaster.model.GameObject;
import rollercoaster.model.GameObjectType;
import rollercoaster.util.GameHelper;

public class Grid extends JPanel {

    private final ActionListener gameButtonActionListener = new GameButtonActionListener();
    private final MouseListener gameButtonMouseListener = new GameButtonMouseListener();

    private final int size = 60;

    private final Board board;

    public Grid(int N, int M, Board board) {
        this.board = board;
        newGame(N, M);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(ResourceLoader.getTexture((Resource.BACKGROUND)), 0, 0, null);

    }

    public final void newGame(int n, int m) {
        setupGamePanel(n, m);
    }

    public Board getBoard() {
        return board;
    }

    private void setupGamePanel(int n, int m) {
        removeAll();
        setLayout(new GridLayout(n, m));
        for (int row = 0; row < n; ++row) {
            for (int column = 0; column < m; ++column) {
                final JButton btn = new GridElement(row, column, size, board.getBoardElementTexture(row, column));
                btn.addActionListener(gameButtonActionListener);
                btn.addMouseListener(gameButtonMouseListener);
                add(btn);
            }
        }

        revalidate();
        repaint();
    }

    public void tick() {
        for (Component component : getComponents()) {
            GridElement btn = (GridElement) component;
            int row = btn.getRow();
            int col = btn.getCol();
            Optional<GameObject> optObj = board.getBoardElement(row, col);
            if (optObj.isEmpty()) {
                btn.setTexture(null);
                btn.setText("");
                btn.setToolTipText(null);
                continue;
            }
            updateGridElement(btn, optObj.get());
        }
    }

    private void updateGridElement(GridElement btn, GameObject obj) {
        btn.setTexture(obj.getTexture());
        // btn.setText(getButtonText(obj));
        setButtonBorder(btn, obj);
        if (obj.getType() == GameObjectType.GAME) {
            Game game = (Game) obj;
            btn.setToolTipText(
                    String.format("Várakozik: %s, játszik: %s", game.getQueue().size(), game.getCurrentUsing().size()));
        }
    }

    private String getButtonText(GameObject obj) {
        switch (obj.getStatus()) {
            case UNDER_CONSTRUCTION: {
                return "Épül";
            }
            case IN_USE: {
                return "Használatban";
            }
            default:
                if (!GameHelper.isBuilding(obj)) {
                    return "";
                }
                Building b = (Building) obj;
                if (b.getNeedsMaintenance()) {
                    return "Karbantartás";
                }
                return null;
        }
    }

    private void setButtonBorder(JButton b, GameObject obj) {
        switch (obj.getStatus()) {
            case UNDER_CONSTRUCTION: {
                b.setBorder(BorderFactory.createDashedBorder(Color.orange, 2, 2, 1, false));
                break;
            }
            case IN_USE: {
                b.setBorder(BorderFactory.createLineBorder(Color.cyan, 2));
                break;
            }
            default:
                if (!GameHelper.isBuilding(obj)) {
                    b.setBorder(null);

                } else {
                    Building bu = (Building) obj;
                    if (bu.getNeedsMaintenance()) {
                        b.setBorder(BorderFactory.createDashedBorder(Color.red, 2, 2, 1, false));
                    } else if (!bu.getHasPower()) {
                        b.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 2, 2, 1, false));
                    } else {
                        b.setBorder(null);
                    }
                }
                break;
        }
    }

    private class GameButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            GridElement gameButton = (GridElement) event.getSource();
            int row = gameButton.getRow();
            int col = gameButton.getCol();
            board.setSelectedGameObject(row, col);
            tick();
        }

    }

    private class GameButtonMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            GridElement gameButton = (GridElement) e.getSource();
            int row = gameButton.getRow();
            int col = gameButton.getCol();
            Optional<GameObject> go = board.getBoardElement(row, col);
            if (go.isEmpty() || !GameHelper.isBuilding(go.get())) {
                return;
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                if (e.isPopupTrigger())
                    doPop(e, row, col);
            }
        }

        public void mouseReleased(MouseEvent e) {
            GridElement gameButton = (GridElement) e.getSource();
            int row = gameButton.getRow();
            int col = gameButton.getCol();
            Optional<GameObject> go = board.getBoardElement(row, col);
            if (go.isEmpty() || !GameHelper.isBuilding(go.get())) {
                return;
            }
            if (e.isPopupTrigger())
                doPop(e, row, col);
        }

        private void doPop(MouseEvent e, int row, int col) {
            PopUp menu = new PopUp(row, col);
            menu.show(e.getComponent(), e.getX(), e.getY());
        }

    }

    private class PopUp extends JPopupMenu {
        private int row;
        private int col;
        private JMenuItem Item1;
        private JMenuItem Item2;

        public PopUp(int row, int col) {
            this.row = row;
            this.col = col;
            Item1 = new JMenuItem("Set minimum people to start");
            Item1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (board.getBoardElement(row, col).isEmpty()) {
                        return;
                    }
                    GameObject go = board.getBoardElement(row, col).get();
                    if (go.getType() != GameObjectType.GAME) {
                        return;
                    }
                    Game game = (Game) go;
                    int input = 0;
                    boolean validInput = true;
                    do {
                        validInput = true;
                        String inputString = JOptionPane.showInputDialog(null, "Adja meg a minimumlétszámot!",
                                game.getMinPeopleToStart());
                        if (inputString == null) {
                            continue;
                        }

                        try {
                            input = Integer.parseInt(inputString.trim());
                            if (input < 1) {
                                JOptionPane.showMessageDialog(null, "A minimumlétszám legalább 1");
                                validInput = false;
                                continue;
                            }
                            game.setMinPeopleToStart(input);
                        } catch (Exception ex) {
                            validInput = false;
                            JOptionPane.showMessageDialog(null, "Csak számokat adjon meg!");
                        }
                    } while (!validInput);

                }
            });
            // minPeopleToStart is only applicable to games, usecost is applicable to any
            // building
            if (board.getBoardElement(row, col).isPresent()
                    && board.getBoardElement(row, col).get().getType() == GameObjectType.GAME)
                add(Item1);
            Item2 = new JMenuItem("Set use cost");
            Item2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (board.getBoardElement(row, col).isPresent()) {
                        Building building = (Building) board.getBoardElement(row, col).get();
                        int input = building.getUseCost();
                        boolean validInput = true;
                        do {
                            validInput = true;
                            String inputString = JOptionPane.showInputDialog(null, "Adja meg az új használati díjat",
                                    building.getUseCost());
                            if (inputString == null) {
                                continue;
                            }

                            try {
                                input = Integer.parseInt(inputString.trim());
                                if (input > building.getUseCostDefault() * 3 || input < 0) {
                                    String msg = "Az új ár legfeljebb háromszorosa lehet az eredetinek, és pozitívnak kell lennie.\n Az eredeti ár: "
                                            + building.getUseCostDefault();
                                    JOptionPane.showMessageDialog(null, msg);
                                    validInput = false;
                                    continue;
                                }
                                building.setUseCost(input);
                            } catch (Exception ex) {
                                validInput = false;
                                JOptionPane.showMessageDialog(null, "Csak számokat adjon meg!");
                            }
                        } while (!validInput);

                    }
                }
            });

            add(Item2);
            JMenuItem Item3 = new JMenuItem("Get building stats");
            Item3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (board.getBoardElement(row, col).isPresent()
                            && GameHelper.isBuilding(board.getBoardElement(row, col).get())) {

                        Building b = (Building) board.getBoardElement(row, col).get();
                        // lehet label szebb lenne?
                        String time = formatTime(b.getCreationTime());
                        String msg = "Építés ideje: " + time + "\nEddig termelt pénz: " + b.getMoneyMade()
                                + "\nKiszolgált vendégek száma: " + b.getGuestsServed()
                                + "\nAlapértelmezett használati ár: " + b.getUseCostDefault()
                                + "\nJelen használati ár: " + b.getUseCost() + "\nÉpítési költség:" + b.getBuildPrice();
                        JOptionPane.showMessageDialog(null, msg);
                    }
                }
            });

            if (board.getBoardElement(row, col).isPresent()
                    && GameHelper.isBuilding(board.getBoardElement(row, col).get())) {
                add(Item3);
            }

        }

        private String formatTime(long cTime) {
            long hour = cTime / 3600;
            long minutes = (cTime % 3600) / 60;
            long seconds = (cTime % 3600) % 60;
            return String.format("%2s:%2s:%2s", hour, minutes, seconds).replace(" ", "0");
        }
    }

}
