import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class GameBoard extends JPanel implements ActionListener {

    private final int TILE_SIZE = 25;
    private final int WIDTH = 20;
    private final int HEIGHT = 20;
    private final int DELAY = 150;

    private final LinkedList<Point> snake = new LinkedList<>();
    private Point food;
    private char direction = 'R';
    private boolean inGame = true;
    private Timer timer;

    public GameBoard() {
        setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        initGame();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char key = e.getKeyChar();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') direction = 'L';
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') direction = 'R';
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != 'D') direction = 'U';
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') direction = 'D';
                        break;
                }
            }
        });
    }

    private void initGame() {
        snake.clear();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));
        placeFood();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void placeFood() {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH);
        int y = rand.nextInt(HEIGHT);
        food = new Point(x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();
            checkCollision();
            checkFood();
        }
        repaint();
    }

    private void move() {
        Point head = snake.getFirst();
        Point newPoint = new Point(head);
        switch (direction) {
            case 'L':
                newPoint.x--;
                break;
            case 'R':
                newPoint.x++;
                break;
            case 'U':
                newPoint.y--;
                break;
            case 'D':
                newPoint.y++;
                break;
        }
        snake.addFirst(newPoint);
        snake.removeLast();
    }

    private void checkCollision() {
        Point head = snake.getFirst();
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            inGame = false;
        }
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                inGame = false;
                break;
            }
        }
        if (!inGame) {
            timer.stop();
        }
    }

    private void checkFood() {
        Point head = snake.getFirst();
        if (head.equals(food)) {
            snake.addLast(new Point(snake.getLast()));
            placeFood();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            g.setColor(Color.RED);
            g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            g.setColor(Color.GREEN);
            for (Point p : snake) {
                g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        } else {
            String msg = "Game Over";
            Font font = new Font("Helvetica", Font.BOLD, 20);
            FontMetrics metrics = getFontMetrics(font);
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString(msg, (getWidth() - metrics.stringWidth(msg)) / 2, getHeight() / 2);
        }
    }
}
