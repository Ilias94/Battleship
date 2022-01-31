package battleship;

import java.util.List;
import java.util.Locale;

public class BattleField {
    public final static int FIRST_COLUMN = 1;
    public final static int LAST_COLUMN = 10;
    public final static int FIRST_ROW = 1;
    public final static int LAST_ROW = 10;

    private String[][] battleField;
    final private List<String> rowsLettersList = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
    final private List<String> columnNumbers = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    public BattleField() {
        String[][] battleField = createGameArray();
        this.battleField = battleField;
    }

    public String[][] createGameArray() {
        String[][] battleField = new String[11][11];
        int letterA = 65;

        for (int i = 0; i < battleField.length; i++) {
            if (i == 0) {
                for (int j = 0; j < battleField[i].length; j++) {
                    if (j == 0) {
                        battleField[i][j] = " ";
                    } else {
                        battleField[i][j] = String.valueOf(j);
                    }
                }
            } else {
                for (int j = 0; j < battleField[i].length; j++) {
                    if (j == 0) {
                        battleField[i][j] = String.valueOf((char) letterA);
                    } else {
                        battleField[i][j] = "~";
                    }
                }
                letterA++;
            }
        }
        return battleField;
    }

    public BattleField(String[][] battleField) {
        this.battleField = battleField;
    }

    public String[][] getBattleField() {
        return battleField;
    }

    public void setBattleField(String[][] battleField) {
        this.battleField = battleField;
    }

    public List<String> getRowsLettersList() {
        return rowsLettersList;
    }

    public boolean isCorrectHitInput(String inputHit) {
        if (inputHit.length() < 2 || inputHit.length() > 3) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return false;
        } else {
            StringBuilder stringBuilder = new StringBuilder(inputHit);
            if (rowsLettersList.contains(String.valueOf(stringBuilder.charAt(0)).toUpperCase(Locale.ROOT))) {
                if (columnNumbers.contains(String.valueOf(stringBuilder.deleteCharAt(0)))) {
                    return true;
                } else {
                    System.out.println("Error! You entered the wrong coordinates! Try again:");
                    return false;
                }
            } else {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                return false;
            }
        }
    }

    public void printMesssageAfterHit(boolean hitted) {
        if (hitted) {
            System.out.println();
            System.out.println("You hit a ship!");
        } else {
            System.out.println();
            System.out.println("You missed!");
        }
    }

    public boolean hit(Coordinates hitCoordinates, String[][] gameArray) {
        int y = hitCoordinates.getY();
        int x = hitCoordinates.getX();
        if ("O".equals(gameArray[y][x])) {
            gameArray[y][x] = "X";
            return true;
        } else if ("X".equals(gameArray[y][x])) {
            return true;
        } else if ("~".equals(gameArray[y][x])) {
            gameArray[y][x] = "M";
            return false;
        }
        return false;
    }

    public boolean isCorrectInput(String inputString) {
        if (inputString.length() < 5 || inputString.length() > 7) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return false;
        } else {
            String[] splittedInput = inputString.split(" ");
            for (String coordinate : splittedInput) {
                StringBuilder stringBuilder = new StringBuilder(coordinate);
                if (rowsLettersList.contains(String.valueOf(stringBuilder.charAt(0)).toUpperCase(Locale.ROOT))) {
                    if (columnNumbers.contains(String.valueOf(stringBuilder.deleteCharAt(0)))) {
                        return true;
                    } else {
                        System.out.println("Error! You entered the wrong coordinates! Try again:");
                        return false;
                    }
                } else {
                    System.out.println("Error! You entered the wrong coordinates! Try again:");
                    return false;
                }

            }
        }
        return false;
    }

    public void printGameField() {
        for (String[] row : battleField) {
            for (String element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }


}
