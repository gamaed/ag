import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Schi extends JFrame {

    public Schi() {
        setTitle("шипучка");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        CirclePanel panel = new CirclePanel();
        add(panel);
        panel.startAnimation();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Schi frame = new Schi();
            frame.setVisible(true);
        });
    }

    static class Ball {
        int x, y, dx, dy, radius;
        Color color;

        public Ball(int x, int y, int dx, int dy, int radius) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.radius = radius;
            Random r = new Random();
            this.color = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
        }

        void move(JPanel panel) {
            x += dx;
            y += dy;

            if (x < 0 || x + radius > panel.getWidth()) dx = -dx;
            if (y < 0 || y + radius > panel.getHeight()) dy = -dy;
        }

        boolean isClicked(int mx, int my) {
            int cx = x + radius / 2;
            int cy = y + radius / 2;
            return Math.hypot(mx - cx, my - cy) < radius;
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillOval(x, y, radius, radius);
        }
    }

    static class CirclePanel extends JPanel implements MouseListener {
        private final ArrayList<Ball> balls = new ArrayList<>();
        private final Random random = new Random();

        public CirclePanel() {

            for (int i = 0; i < 4; i++) {
                balls.add(new Ball(random.nextInt(500), random.nextInt(500),
                        random.nextInt(5) + 1, random.nextInt(5) + 1, 40));
            }
            addMouseListener(this);
        }

        public void startAnimation() {
            Timer timer = new Timer(20, e -> {
                for (Ball ball : balls) {
                    ball.move(this);
                }
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (Ball ball : balls) {
                ball.draw(g2d);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Iterator<Ball> it = balls.iterator();
            while (it.hasNext()) {
                Ball b = it.next();
                if (b.isClicked(e.getX(), e.getY())) {
                    it.remove();
                    explode(b);
                    break;
                }
            }
        }

        private void explode(Ball b) {
            int num = 10 + random.nextInt(10);
            for (int i = 0; i < num; i++) {
                int dx = random.nextInt(7) - 3;
                int dy = random.nextInt(7) - 3;
                if (dx == 0 && dy == 0) dx = 1;
                balls.add(new Ball(b.x, b.y, dx, dy, 15));
            }
        }
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }
}
