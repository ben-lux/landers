import javax.swing.*;
import java.awt.*;
import java.io.*;

public class HighScoresGraphics {
    private HighScores hs=new HighScores();

    public HighScoresGraphics(Container c,int playerScore) throws IOException, ClassNotFoundException {
        File f = new File("highscores.bin");
        if (!f.exists()) save();
        load();

        String playerName="";
        int highScorePos=hs.checkIfTopTen(playerScore);
        if (highScorePos>=0){
            while (playerName.length()!=3) {
                //the "" and + makes it a null check
                playerName="";
                playerName += JOptionPane.showInputDialog(c, "High score!\n" +
                        "Enter your name(3 letters):");
                hs.addscore(highScorePos, playerScore, playerName);
            }
        }
        JOptionPane.showMessageDialog(c,
                hs.getScores());
        save();
    }
    public void save() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("highscores.bin"));
        oos.writeObject(hs);
        oos.close();
    }

    public void load() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("highscores.bin"));

        hs = (HighScores) ois.readObject();
        ois.close();
    }
}
