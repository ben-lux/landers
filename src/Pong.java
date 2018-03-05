import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Pong extends JFrame implements ActionListener {
    final int BALL_SIZE=40, BOARD_SIZE=1000, PADDLE_WIDTH=20, PADDLE_HEIGHT=100;
    JButton b;

    public Pong(){
      setUpJFrame();
        playerPaddle();
        compPaddle();

    }

    final int PLAYER_PADDLE_X = BOARD_SIZE - 50;
    int playerPaddleY = BOARD_SIZE/2-PADDLE_HEIGHT/2;
    int wheelRotation = 0;

    public void playerPaddle(){

        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                //if (playerPaddleY != BOARD_SIZE - PADDLE_HEIGHT)
                    wheelRotation = e.getWheelRotation() * 20;

            }
        });
        Timer timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics g = getGraphics();
                g.setColor(getBackground());
                g.fillRect(PLAYER_PADDLE_X, playerPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);

                g.setColor(Color.BLACK);

                playerPaddleY += wheelRotation;
                if (playerPaddleY >= BOARD_SIZE - PADDLE_HEIGHT)
                    playerPaddleY = BOARD_SIZE - PADDLE_HEIGHT - 10;
                else if(playerPaddleY <= 0){
                    playerPaddleY = 10;
                }

                wheelRotation = 0;
                g.fillRect(PLAYER_PADDLE_X, playerPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
            }
        });
        timer.start();

    }

    final int COMP_PADDLE_X = 50;
    int compPaddleY = BOARD_SIZE/2-PADDLE_HEIGHT/2;
    public void compPaddle(){

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

    public void compMovement(){
        if(ballX <= BOARD_SIZE/2){
            compPaddleY += (ballY+BALL_SIZE/2) > (compPaddleY+PADDLE_HEIGHT/2) ? 3:-3;
        }

    }

    int ballX = BOARD_SIZE/2, ballY= BOARD_SIZE/2;
    int yVel=0,xVel=5;
    Timer timer ;

    @Override
    public void actionPerformed(ActionEvent e) {
        remove(b);
        repaint();
        timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics g = getGraphics();
                g.setColor(getBackground());
                g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);
                g.setColor(Color.BLACK);

                if(ballX+BALL_SIZE>=PLAYER_PADDLE_X||ballX<=COMP_PADDLE_X
                        ||ballY <= 0 || ballY >= BOARD_SIZE) {
                    checkBounce();
                }

                ballX += xVel;
                ballY += yVel;
                g.fillOval(ballX,ballY,BALL_SIZE,BALL_SIZE);
            }
        });
        timer.start();
    }


    public void checkBounce(){
        if (ballX>PLAYER_PADDLE_X||ballX<COMP_PADDLE_X)
            checkScore();
        //hits top or bottom
        if (ballY <= 0 || ballY >= BOARD_SIZE){
            yVel *= (-1);
        }
        //hits paddle
        if (ballX + BALL_SIZE >= PLAYER_PADDLE_X && ballX < PLAYER_PADDLE_X+PADDLE_WIDTH&&
                    ballY >= playerPaddleY && ballY <= playerPaddleY + PADDLE_HEIGHT) {
                yVel = ((ballY + (BALL_SIZE / 2)) - (playerPaddleY + (PADDLE_HEIGHT / 2))) / 7;
                xVel*=(-1);
        } else if ( ballX <= COMP_PADDLE_X && ballX > COMP_PADDLE_X-PADDLE_WIDTH&&
                (ballY >= compPaddleY && ballY <= compPaddleY + PADDLE_HEIGHT)) {
                yVel = ((ballY + (BALL_SIZE / 2)) - (compPaddleY + (PADDLE_HEIGHT / 2))) / 7;
                xVel*=(-1);
            }
    }


    int cmpScore=0, playerScore=0;
    String score;
    public void checkGameOver(){
        if (cmpScore>=10||playerScore>=10){
            String winner = cmpScore<playerScore?"Player Wins!":"Computer Wins!";
            JOptionPane.showMessageDialog(this, "Game over! "+winner+"\nFinal Score: "+score);
            System.exit(0);
        }
    }

    private void checkScore(){
        if (ballX<=0||ballX >= BOARD_SIZE-BALL_SIZE) {//i changed it from ballY to ballX
            if (ballX<=0){
                playerScore++;
            }
            else {
                cmpScore++;
            }
            score="Computer: "+cmpScore+"  |  Player: "+playerScore;
            checkGameOver();
            JOptionPane.showMessageDialog(this, score);
            ballX = BOARD_SIZE/2;
            ballY= BOARD_SIZE/2;
            yVel=0;
            compPaddleY = BOARD_SIZE/2;
            playerPaddleY = BOARD_SIZE/2;
            repaint();
        }
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

    public static void main(String[] args) {
        new Pong();
    }

}