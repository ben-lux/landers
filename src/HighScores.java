import java.io.*;

public class HighScores implements Serializable {
    private int[] scores = new int[10];
    private String[] POSITION = {"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th"};
    private String[] names = new String[10];


    public HighScores() {
        for (int i = 0; i < 10; i++) {
            names[i] = "aaa";
        }
    }

    public int checkIfTopTen(int playerScore) {
        for (int i = 0; i < 10; i++) {
            if (scores[i] < playerScore) {
                return i;
            }
        }
        return -1;
    }

    public void addscore(int index, int playerScore, String playerName) {
        System.arraycopy(scores, index, scores, index + 1, 9 - index);
        scores[index] = playerScore;
        System.arraycopy(names, index, names, index + 1, 9 - index);
        names[index] = playerName;
    }

    public String getScores() {
        String formattedScores = "";
        for (int i = 0; i < 10; i++) {
            formattedScores += String.format("%-20s%-20d\t%s\n", POSITION[i], scores[i], names[i]);
        }
        return formattedScores;

    }
}