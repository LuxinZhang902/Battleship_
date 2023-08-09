package battleship;

import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GridCanvas extends Canvas {
    private static final int BOARD_SIZE = 8; // 棋盘大小
    private static final int SQUARE_SIZE = 50; // 格子大小

    private String boardString; // 棋盘数据字符串

    public GridCanvas(String boardString) {
        super(SQUARE_SIZE * BOARD_SIZE, SQUARE_SIZE * BOARD_SIZE);
        this.boardString = boardString;
    }

//    @Override
    public void paint(GraphicsContext gc) {
        // 清空画布
        gc.clearRect(0, 0, getWidth(), getHeight());

        // 绘制棋盘背景
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());

        // 绘制棋盘格子
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                gc.setFill(getSquareColor(row, col));
                gc.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }

        // 绘制棋子
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                char pieceChar = boardString.charAt(row * BOARD_SIZE + col);
                if (pieceChar != ' ') {
                    Image pieceImage = getPieceImage(pieceChar);
                    gc.drawImage(pieceImage, col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                }
            }
        }
    }

    // 根据行列号获取棋盘格子的颜色
    private Color getSquareColor(int row, int col) {
        if ((row + col) % 2 == 0) {
            return Color.WHITE;
        } else {
            return Color.GREEN;
        }
    }

    // 根据棋子字符获取棋子图像
    private Image getPieceImage(char pieceChar) {
        // 根据棋子字符加载相应的图像文件
        // 省略代码
        Image image = new Image("/pic/carrier.png");
        return image;
    }

}
