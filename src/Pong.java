import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Pong extends JFrame implements ActionListener {
    private final int BALL_SIZE=40, BOARD_SIZE=1000, PADDLE_WIDTH=20, PADDLE_HEIGHT=100,
        BALL_CENTER=BALL_SIZE/2;
    private int difficulty;
    JButton b;

    public Pong(){
        setUpJFrame();
        String[] options={"Easy","Meadium","Hard"};
        difficulty=JOptionPane.showOptionDialog(this,"Select a difficulty",
                "Difficulty", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null,options,options)*2+2;
        playerPaddle();
        compPaddle();
    }

    private void setUpJFrame(){
        setTitle("Pong");
        setSize(BOARD_SIZE,BOARD_SIZE);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        b = new JButton("Start");
        add(b, BorderLayout.NORTH);
        b.addActionListener(this);
        setVisible(true);
    }

    private final int PLAYER_PADDLE_X = BOARD_SIZE - 50;
    private int playerPaddleY = BOARD_SIZE/2-PADDLE_HEIGHT/2;
    private int wheelRotation = 0;
    private void playerPaddle(){
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                wheelRotation = e.getWheelRotation() * 20;
            }
        });

        Timer timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics g = getGraphics();
                g.setColor(getBackground());
                g.fillRect(PLAYER_PADDLE_X, playerPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);

                playerPaddleY += wheelRotation;
                if (playerPaddleY >= BOARD_SIZE - PADDLE_HEIGHT)
                    playerPaddleY = BOARD_SIZE - PADDLE_HEIGHT - 10;
                else if(playerPaddleY <= 0){
                    playerPaddleY = 10;
                }

                wheelRotation = 0;
                g.setColor(Color.BLACK);
                g.fillRect(PLAYER_PADDLE_X, playerPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
            }
        });
        timer.start();

    }

    private final int COMP_PADDLE_X = 50;
    private int compPaddleY = BOARD_SIZE/2-PADDLE_HEIGHT/2;
    private void compPaddle(){

        Timer timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics g = getGraphics();
                g.setColor(getBackground());
                g.fillRect(COMP_PADDLE_X,compPaddleY,PADDLE_WIDTH,PADDLE_HEIGHT);

                if (ballX < BOARD_SIZE/2){
                    compMovement();
                }

                if (compPaddleY >= BOARD_SIZE - PADDLE_HEIGHT)
                    compPaddleY = BOARD_SIZE - PADDLE_HEIGHT - 10;
                else if(compPaddleY <= 0){
                    compPaddleY = 10;
                }

                g.setColor(Color.BLACK);
                g.fillRect(COMP_PADDLE_X,compPaddleY,PADDLE_WIDTH,PADDLE_HEIGHT);
            }
        });
        timer.start();
    }

    private void compMovement(){
        if(ballX <= BOARD_SIZE/2){
            compPaddleY +=
                    (ballY+BALL_CENTER) > (compPaddleY+PADDLE_HEIGHT/2)
                            ? difficulty:-difficulty;
        }
    }

    private int ballX = BOARD_SIZE/2, ballY= BOARD_SIZE/2-BALL_CENTER;
    private int yVel=0,xVel=5;

    @Override
    public void actionPerformed(ActionEvent e) {
        remove(b);
        repaint();
        Timer timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics g = getGraphics();
                g.setColor(getBackground());
                g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);
                g.setColor(Color.BLACK);

                if (ballX + BALL_SIZE >= PLAYER_PADDLE_X || ballX <= COMP_PADDLE_X + PADDLE_WIDTH ||
                        ballY <= 0 || ballY >= BOARD_SIZE) {
                    checkBounce();
                }

                ballX += xVel;
                ballY += yVel;
                g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);
            }
        });
        timer.start();
    }


    private int bounceScore;
    private void checkBounce(){
        if (ballX>PLAYER_PADDLE_X||ballX<COMP_PADDLE_X)
            checkScore();
        //hits top or bottom
        if (ballY <= 10 || ballY >= BOARD_SIZE-BALL_SIZE-10){
            yVel *= (-1);
        }
        //hits player paddle
        if (ballX + BALL_SIZE >= PLAYER_PADDLE_X && ballX < PLAYER_PADDLE_X + PADDLE_WIDTH &&
                ballY+BALL_CENTER>= playerPaddleY &&
                ballY+BALL_CENTER <= playerPaddleY + PADDLE_HEIGHT) {
            yVel = ((ballY + BALL_CENTER)
                    - (playerPaddleY + (PADDLE_HEIGHT / 2))) / 7;
            xVel *= (-1);
            bounceScore+=1;
        }
        //hits comp paddle
        else if (ballX <= COMP_PADDLE_X + PADDLE_WIDTH && ballX > COMP_PADDLE_X  &&
                ballY+BALL_CENTER  >= compPaddleY &&
                ballY+BALL_CENTER <= compPaddleY + PADDLE_HEIGHT) {
            yVel = ((ballY + BALL_CENTER)
                    - (compPaddleY + (PADDLE_HEIGHT / 2))) / 7;
            xVel *= (-1);
        }
    }

    private int cmpScore=0, playerScore=0;
    private String score;
    private void checkScore(){
        if (ballX<=0||ballX >= BOARD_SIZE-BALL_SIZE) {
            if (ballX<=0){
                playerScore++;
            }
            else {
                cmpScore++;
            }
            score="Computer: "+cmpScore+"  |  Player: "+playerScore;
            checkGameOver();
            JOptionPane.showMessageDialog(this, score);
            ballX = ballY = BOARD_SIZE/2-BALL_CENTER;

            yVel=0;
            compPaddleY = playerPaddleY = BOARD_SIZE/2-PADDLE_HEIGHT/2;
            repaint();
        }
    }

    private void checkGameOver(){
        if (cmpScore>=10||playerScore>=10){
            String winner = cmpScore<playerScore?"Player Wins!":"Computer Wins!";
            JOptionPane.showMessageDialog(this,
                    "Game over! "+winner+"\nFinal Score: "+score);
            try {
                new HighScoresGraphics(this.getContentPane(),bounceScore);
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(this,
                        "Something Broke");
                System.exit(0);
            }
            System.exit(0);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Pong();
    }
}