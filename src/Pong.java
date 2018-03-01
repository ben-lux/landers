import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Pong extends JFrame implements ActionListener {
    final int CIRCLE_SIZE=40, BOARD_SIZE=1000, RECT_WIDTH=20, RECT_HEIGHT=100;
    JButton b;

    public Pong(){
        setTitle("Pong");
        setSize(BOARD_SIZE,BOARD_SIZE);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        b = new JButton("Start");
        add(b, BorderLayout.NORTH);
        b.addActionListener(this);

        setVisible(true);
        playerPaddle();
        compPaddle();

    }

    final int PLAYER_PADDLE_X=BOARD_SIZE-50;
    int playerPaddleY = BOARD_SIZE/2;
    int wheelRotation=0;
    public void playerPaddle(){

        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (playerPaddleY!=BOARD_SIZE-RECT_HEIGHT) wheelRotation=e.getWheelRotation()*15;

        }
        });
        Timer timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics g = getGraphics();
                g.setColor(getBackground());
                g.fillRect(PLAYER_PADDLE_X,playerPaddleY,RECT_WIDTH,RECT_HEIGHT);

                g.setColor(Color.BLACK);

                playerPaddleY += wheelRotation;
                if (playerPaddleY>=BOARD_SIZE-RECT_HEIGHT)
                    playerPaddleY=BOARD_SIZE-RECT_HEIGHT-10;
                else if(playerPaddleY<=70){
                    playerPaddleY=80;
                }


                wheelRotation=0;
                g.fillRect(PLAYER_PADDLE_X,playerPaddleY,RECT_WIDTH,RECT_HEIGHT);
            }
        });
        timer.start();

    }

    final int COMP_PADDLE_X=50;
    int compPaddleY=BOARD_SIZE/2;
    public void compPaddle(){

        Timer timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics g = getGraphics();
                g.setColor(getBackground());
                g.fillRect(COMP_PADDLE_X,compPaddleY,RECT_WIDTH,RECT_HEIGHT);

                if (ballX<BOARD_SIZE/2){
                    compMovement();
                }

                g.setColor(Color.BLACK);
                g.fillRect(COMP_PADDLE_X,compPaddleY,RECT_WIDTH,RECT_HEIGHT);
            }
        });
        timer.start();
    }

    public void compMovement(){

    }

    int ballX = BOARD_SIZE/2, ballY= BOARD_SIZE/2;
    int yVel=0,xVel=5;
    Timer timer ;

    @Override
    public void actionPerformed(ActionEvent e) {
        remove(b);
        timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics g = getGraphics();
                g.setColor(getBackground());
                g.fillOval(ballX,ballY,CIRCLE_SIZE,CIRCLE_SIZE);

                g.setColor(Color.BLACK);
                checkScore();
                checkBounce();
                ballX+=xVel;
                ballY+=yVel;
                g.fillOval(ballX,ballY,CIRCLE_SIZE,CIRCLE_SIZE);
            }
        });
        timer.start();
    }


    public void checkBounce(){

        //hits top or bottom
        if (ballX==80||ballX==BOARD_SIZE){
            yVel=yVel*(-1);
        }
        //hits paddle
        if (ballX+CIRCLE_SIZE==PLAYER_PADDLE_X||ballX==COMP_PADDLE_X){

            if (ballY>=playerPaddleY&&ballY<=playerPaddleY+RECT_HEIGHT){
                xVel=xVel*(-1);
            }
        }
    }

    int cmpScore=0, playerScore=0;
    String score;
    public void checkGameOver(){
      if (cmpScore>=10||playerScore>=10){
          String winner = cmpScore<playerScore?"Player Wins!":"Computer Wins!";
          JOptionPane.showMessageDialog(this, "Game over! "+winner);
          System.exit(0);
      }
    }

    private void checkScore(){
        checkGameOver();
        if (ballX==0||ballY==BOARD_SIZE-CIRCLE_SIZE) {
            if (ballX==0){
                playerScore++;
            }
            else {
                cmpScore++;
            }
            score="Computer: "+cmpScore+"  |  Player: "+playerScore;
            JOptionPane.showMessageDialog(this, score);
            ballX = BOARD_SIZE/2;
            ballY= BOARD_SIZE/2;
        }
    }
    public static void main(String[] args){
        new Pong();
    }
}