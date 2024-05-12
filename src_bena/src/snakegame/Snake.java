import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Snake {
    public static final int CELL_SIZE = 40;
    private LinkedList<Point> body;
    private int dx, dy;
    private Point food;
    private Random random;
    private int sWidth, sHeight;
    private boolean gameOver=false;
    private static final int INITIAL_LENGTH = 2;
    private boolean directionChanged = false;
    private int score;
    private boolean isBorderMode; // Biến xác định chế độ có biên hay không
    private boolean[][] grid;
    
    public void changeDirection(int newDx, int newDy) {
        // Thay đổi hướng di chuyển của rắn
        if (dx != newDx || dy != newDy) {
            dx = newDx;
            dy = newDy;
            directionChanged = true; // Đánh dấu rằng hướng di chuyển đã thay đổi
        }
    }

    public Snake(int pWidth, int pHeight, int gWidth, int gHeight, boolean isBorderMode) {
        this.sWidth = pWidth;
        this.sHeight = pHeight;
        this.isBorderMode = isBorderMode; // Gán giá trị chế độ có biên hay không
        body = new LinkedList<>();
        body.add(new Point(pWidth / 2, pHeight / 2)); // Khởi tạo con rắn ở giữa màn hình
        
        // Thêm các điểm cho thân rắn với độ dài ban đầu
        for (int i = 0; i < INITIAL_LENGTH; i++) {
            body.add(new Point(pWidth / 2 - i, pHeight / 2));
        }
        dx = 1;
        dy = 0;
        random = new Random();
        score = 0;
        placeFood();
        
        this.grid = new boolean[pWidth][pHeight];
        for (Point p : body) {
            grid[p.x][p.y] = true;
        }       
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void move() {
        if (!gameOver) {
            Point newHead = new Point((body.getFirst().x + dx * CELL_SIZE + sWidth) % sWidth,
                    (body.getFirst().y + dy * CELL_SIZE + sHeight) % sHeight);
            body.addFirst(newHead);
            if (!newHead.equals(food)) {
                body.removeLast();
            } else {
                score += 1;
                placeFood();
            }

            // Kiểm tra va chạm với chính nó
            if (checkSelfCollision()) {
                gameOver = true;
            }

            // Kiểm tra va chạm với biên
            if (!isBorderMode && isSnakeAtBorder()) {
                gameOver = true;
            }
        // Cập nhật grid
            for (int x = 0; x < sWidth; x++) {
                for (int y = 0; y < sHeight; y++) {
                    grid[x][y] = false;
                }
            }
            for (Point p : body) {
                grid[p.x][p.y] = true;
            }            
        }
    }

    public void placeFood() {
        do {
            food = new Point(random.nextInt(sWidth / CELL_SIZE) * CELL_SIZE,
                    random.nextInt(sHeight / CELL_SIZE) * CELL_SIZE);
        } while (body.contains(food) || (!isBorderMode && isFoodAtBorder())); // Kiểm tra food không xuất hiện ở biên nếu không ở chế độ dễ
    }

    public boolean checkSelfCollision() {
        Point head = body.getFirst();
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true; // Nếu đầu cắn vào phần thân khác, trả về true
            }
        }
        return false;
    }

    // Phương thức kiểm tra va chạm với biên (chỉ khi ở chế độ khó)
    private boolean checkBorderCollision(Point head) {
        return head.x < 0 || head.x >= sWidth || head.y < 0 || head.y >= sHeight;
    }

    public Point getFood() {
        return food;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public Point getHead() {
        return body.getFirst();
    }

    public void clearDirectionChanged() {
        directionChanged = false;
    }

    public boolean isDirectionChanged() {
        return directionChanged;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    public void setBorderMode(boolean isBorderMode) {
        this.isBorderMode = isBorderMode;
    }

    public boolean isBorderMode() {
        return isBorderMode;
    }
 
    // Phương thức kiểm tra thức ăn có nằm ở biên không (chỉ dùng khi không ở chế độ dễ)
    private boolean isFoodAtBorder() {
        return food.x == 0 || food.x == sWidth - CELL_SIZE || food.y == 0 || food.y == sHeight - CELL_SIZE;
    }  
    
    // Phương thức kiểm tra xem con rắn có đang nằm trên biên không (chỉ áp dụng cho chế độ khó)
    private boolean isSnakeAtBorder() {
        Point head = body.getFirst();
        return head.x == 0 || head.x == sWidth - CELL_SIZE || head.y == 0 || head.y == sHeight - CELL_SIZE;
    }    
    
    public boolean isGameFinished(int gridWidth, int gridHeight) {
    // Kiểm tra xem có ô nào trống không
    for (int x = 0; x < gridWidth; x++) {
        for (int y = 0; y < gridHeight; y++) {
            if (!grid[x][y]) {
                return false;
            }
        }
    }
    // Nếu tất cả các ô đều đã được rắn chiếm, trò chơi kết thúc
    return true;
}
}