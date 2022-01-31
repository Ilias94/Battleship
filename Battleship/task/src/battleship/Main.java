package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        BattleField battleField1 = new BattleField();
        BattleField battleField2 = new BattleField();
        String[][] gameArray1 = battleField1.getBattleField();
        String[][] gameArray2 = battleField2.getBattleField();
        String[][] fogOFWar1 = battleField1.createGameArray();
        String[][] fogOFWar2 = battleField2.createGameArray();
        ShipTypes[] shipTypesArray = ShipTypes.values();
        List<Ship> aliveShipList1 = new ArrayList<>();
        List<Ship> aliveShipList2 = new ArrayList<>();
        List<Ship> toRemoveShipList1 = new ArrayList<>();
        List<Ship> toRemoveShipList2 = new ArrayList<>();

        System.out.println("PLayer 1, place your ships on the game field");
        System.out.println();
        for (ShipTypes shipType : shipTypesArray) {
            inputShip(scanner, battleField1, gameArray1, aliveShipList1, shipType);
        }
        System.out.println("");
        System.out.println("Press Enter and pass the move to another player");
        System.out.println("...");
        String press = scanner.nextLine();
        System.out.println("Player 2, place your ships on the game field");
        for (ShipTypes shipType : shipTypesArray) {
            inputShip(scanner, battleField2, gameArray2, aliveShipList2, shipType);
        }
        System.out.println();
        System.out.println("Press Enter and pass the move to another player");
        System.out.println("...");
        String Press = scanner.nextLine();

        boolean correctHitInput;
        String inputHit;


        boolean livingShip1;
        boolean livingShip2;
        do {
            do {
                System.out.println();
                printGameArray(fogOFWar1);
                System.out.println("-----------");
                printGameArray(gameArray1);
                System.out.println();
                System.out.println("PLayer 1 it's your turn:");
                System.out.println();
                inputHit = scanner.nextLine();
                correctHitInput = battleField1.isCorrectHitInput(inputHit);
            } while (!correctHitInput);

            Coordinates hitCoordinate1 = createCoordinatesFromString(inputHit);
            boolean hitted1 = battleField1.hit(hitCoordinate1, gameArray2);
            if (hitted1) {
                fogOFWar1[hitCoordinate1.getY()][hitCoordinate1.getX()] = "X";
                gameArray2[hitCoordinate1.getY()][hitCoordinate1.getX()] = "X";
            } else {
                fogOFWar1[hitCoordinate1.getY()][hitCoordinate1.getX()] = "M";
                gameArray2[hitCoordinate1.getY()][hitCoordinate1.getX()] = "M";
            }

            boolean sanked2 = checkIsSankShip(aliveShipList2, toRemoveShipList2, gameArray2, scanner);
            if (!sanked2) {
                battleField2.printMesssageAfterHit(hitted1);
                System.out.println("Press Enter and pass the move to another player");
                System.out.println("...");
            }
            livingShip2 = checkIsLivingShip(gameArray2);


            do {
                System.out.println();
                printGameArray(fogOFWar2);
                System.out.println("-----------");
                printGameArray(gameArray2);
                System.out.println();
                System.out.println("PLayer 2 it's your turn:");
                System.out.println();
                inputHit = scanner.nextLine();
                correctHitInput = battleField2.isCorrectHitInput(inputHit);
            } while (!correctHitInput);

            Coordinates hitCoordinate2 = createCoordinatesFromString(inputHit);
            boolean hitted2 = battleField1.hit(hitCoordinate2, gameArray1);
            if (hitted2) {
                fogOFWar2[hitCoordinate2.getY()][hitCoordinate2.getX()] = "X";
                gameArray1[hitCoordinate2.getY()][hitCoordinate2.getX()] = "X";
            } else {
                fogOFWar2[hitCoordinate2.getY()][hitCoordinate2.getX()] = "M";
                gameArray1[hitCoordinate2.getY()][hitCoordinate2.getX()] = "M";
            }

            boolean sanked1 = checkIsSankShip(aliveShipList1, toRemoveShipList1, gameArray1, scanner);
            if (!sanked1) {
                battleField1.printMesssageAfterHit(hitted2);
            }
            livingShip1 = checkIsLivingShip(gameArray1);
        } while (livingShip1 || livingShip2);
    }

    private static void inputShip(Scanner scanner, BattleField battleField, String[][] gameArray, List<Ship> aliveShipList, ShipTypes shipType) {
        boolean correctShipPut;
        printGameArray(gameArray);
        do {
            correctShipPut = false;
            boolean isCorrectInputString;
            String inputString;
            do {
                System.out.println();
                System.out.printf("Enter the coordinates of the %s (%d cells)", shipType.getName(), shipType.getSize());
                System.out.println();
                inputString = scanner.nextLine();
                isCorrectInputString = battleField.isCorrectInput(inputString);
            } while (!isCorrectInputString);


            String[] coordinatesStringArray = inputString.split(" ");
            Coordinates firstCoordinates = createCoordinatesFromString(coordinatesStringArray[0]);
            Coordinates secondCoordinates = createCoordinatesFromString(coordinatesStringArray[1]);
            Ship ship = new Ship(firstCoordinates, secondCoordinates, shipType);


            if (isDiagonal(ship)) {
                if (isDiagonalCorrectLengthBeetweenCoordinatesAsShipRequest(ship)) {
                    int x = firstCoordinates.getX();
                    int minY = Math.min(firstCoordinates.getY(), secondCoordinates.getY());
                    int maxY = Math.max(firstCoordinates.getY(), secondCoordinates.getY());
                    boolean isOccupiedSpaceBetweenCoordinates = false;

                    for (int i = minY; i <= maxY; i++) {
                        if (gameArray[i][x].equals("O")) {
                            printErrorTooClose();
                            isOccupiedSpaceBetweenCoordinates = true;
                            break;
                        }
                    }
                    if (!isOccupiedSpaceBetweenCoordinates) {
                        if (isOccupiedSpaceAboveDiagonalShip(ship, gameArray)) {
                            printErrorTooClose();
                        } else if (isOccupiedSpaceUnderDiagonalShip(ship, gameArray)) {
                            printErrorTooClose();
                        } else if (isOccupiedLeftSideOfDiagonalShip(ship, gameArray)) {
                            printErrorTooClose();
                        } else if (isOccupiedWrightSideOfDiagonalShip(ship, gameArray)) {
                            printErrorTooClose();
                        } else {
                            for (int i = minY; i <= maxY; i++) {
                                gameArray[i][x] = "O";
                            }
                            correctShipPut = true;
                            aliveShipList.add(ship);
                        }
                    }
                } else {
                    System.out.println("Error! Incorrect length of the ship");
                }
            } else if (isHorizontal(ship)) {
                if (isHorizontalCorrectSpaceBetweenCoordinatesAsShipRequest(ship)) {
                    int y = ship.getBeginCoordinate().getY();
                    int minX = Math.min(ship.getBeginCoordinate().getX(), ship.getEndCoordinate().getX());
                    int maxX = Math.max(ship.getBeginCoordinate().getX(), ship.getEndCoordinate().getX());
                    boolean isOccupiedSpaceBetweenCoordinates = false;

                    for (int i = minX; i <= maxX; i++) {
                        if ("O".equals(gameArray[y][i])) {
                            printErrorTooClose();
                            isOccupiedSpaceBetweenCoordinates = true;
                            break;
                        }
                    }

                    if (!isOccupiedSpaceBetweenCoordinates) {
                        if (isOccupiedSpaceAboveHorizontalShip(ship, gameArray)) {
                            printErrorTooClose();
                        } else if (isOccupiedSpaceUnderHorizontalShip(ship, gameArray)) {
                            printErrorTooClose();
                        } else if (isOccupiedLeftSideOfHorizontalShip(ship, gameArray)) {
                            printErrorTooClose();
                        } else if (isOccupiedWrightSideOfHorizontalShip(ship, gameArray)) {
                            printErrorTooClose();
                        } else {
                            for (int i = minX; i <= maxX; i++) {
                                gameArray[y][i] = "O";
                            }
                            correctShipPut = true;
                            aliveShipList.add(ship);
                        }
                    }
                } else {
                    System.out.println("Error! Incorrect length of the ship");
                }
            } else {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
            }
        } while (!correctShipPut);

        printGameArray(gameArray);
    }

    private static boolean checkIsSankShip(List<Ship> shipList, List<Ship> toRemoveShipList, String[][] gameArray, Scanner scanner) {
        boolean sanked = false;
        for (Ship ship : shipList) {
            boolean horizontal = false;
            boolean diagonal = false;
            if (ship.getBeginCoordinate().getY() == ship.getEndCoordinate().getY()) {
                horizontal = true;
            } else {
                diagonal = true;
            }
            if (horizontal) {
                boolean isAlive = false;
                int y = ship.getBeginCoordinate().getY();
                int minX = Math.min(ship.getBeginCoordinate().getX(), ship.getEndCoordinate().getX());
                int maxX = Math.max(ship.getBeginCoordinate().getX(), ship.getEndCoordinate().getX());
                for (int i = minX; i <= maxX; i++) {
                    if (gameArray[y][i].equals("O")) {
                        isAlive = true;
                        break;
                    }
                }
                if (!isAlive) {
                    toRemoveShipList.add(ship);
                    if (shipList.size() == 1) {
                        System.out.println();
                        System.out.println("You sank the last ship. You won. Congratulations!");
                    } else {
                        System.out.println();
                        System.out.println("You sank a ship! Specify a new target:");
                        System.out.println("Press Enter and pass the move to another player");
                        System.out.println("...");
                        scanner.nextLine();

                    }
                    sanked = true;
                    break;

                }
            } else if (diagonal) {
                boolean isAlive = false;
                int x = ship.getBeginCoordinate().getX();
                int minY = Math.min(ship.getBeginCoordinate().getY(), ship.getEndCoordinate().getY());
                int maxY = Math.max(ship.getBeginCoordinate().getY(), ship.getEndCoordinate().getY());
                for (int i = minY; i <= maxY; i++) {
                    if (gameArray[i][x].equals("O")) {
                        isAlive = true;
                        break;
                    }
                }
                if (!isAlive) {
                    toRemoveShipList.add(ship);
                    if (shipList.size() == 1) {
                        System.out.println();
                        System.out.println("You sank the last ship. You won. Congratulations!");
                    } else {
                        System.out.println();
                        System.out.println("You sank a ship! Specify a new target:");
                    }
                    sanked = true;
                    break;
                }
            }
        }
        shipList.removeAll(toRemoveShipList);
        return sanked;
    }


    private static boolean checkIsLivingShip(String[][] gameArray) {
        for (String[] row : gameArray) {
            for (String column : row) {
                if (column.equals("O")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void printGameArray(String[][] gameArray) {
        for (String[] row : gameArray) {
            for (String element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

    private static boolean isOccupiedWrightSideOfHorizontalShip(Ship ship, String[][] gameArray) {
        int y = ship.getBeginCoordinate().getY();
        int maxX = Math.max(ship.getBeginCoordinate().getX(), ship.getEndCoordinate().getX());

        if (maxX != BattleField.LAST_COLUMN) {
            if ("O".equals(gameArray[y][maxX + 1])) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean isOccupiedLeftSideOfHorizontalShip(Ship ship, String[][] gameArray) {
        int y = ship.getBeginCoordinate().getY();
        int minX = Math.min(ship.getBeginCoordinate().getX(), ship.getEndCoordinate().getX());

        if (minX != BattleField.FIRST_COLUMN) {
            if ("O".equals(gameArray[y][minX - 1])) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean isOccupiedSpaceUnderHorizontalShip(Ship ship, String[][] gameArray) {
        int y = ship.getBeginCoordinate().getY();
        int minX = Math.min(ship.getBeginCoordinate().getX(), ship.getEndCoordinate().getX());
        int maxX = Math.max(ship.getBeginCoordinate().getX(), ship.getEndCoordinate().getX());
        if (y != BattleField.LAST_ROW) {
            for (int i = minX; i <= maxX; i++) {
                if ("O".equals(gameArray[y + 1][i])) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private static boolean isOccupiedSpaceAboveHorizontalShip(Ship ship, String[][] gameArray) {
        int y = ship.getBeginCoordinate().getY();
        int minX = Math.min(ship.getBeginCoordinate().getX(), ship.getEndCoordinate().getX());
        int maxX = Math.max(ship.getBeginCoordinate().getX(), ship.getEndCoordinate().getX());
        if (y != BattleField.FIRST_ROW) {
            for (int i = minX; i <= maxX; i++) {
                if ("O".equals(gameArray[y - 1][i])) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }

    }

    private static boolean isOccupiedLeftSideOfDiagonalShip(Ship ship, String[][] gameArray) {
        int minY = Math.min(ship.getBeginCoordinate().getY(), ship.getEndCoordinate().getY());
        int maxY = Math.max(ship.getBeginCoordinate().getY(), ship.getEndCoordinate().getY());
        int x = ship.getEndCoordinate().getX();
        if (x != BattleField.FIRST_COLUMN) {
            for (int i = minY; i <= maxY; i++) {
                if ("O".equals(gameArray[i][x - 1])) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private static boolean isOccupiedSpaceUnderDiagonalShip(Ship ship, String[][] gameArray) {
        int x = ship.getBeginCoordinate().getX();
        int maxY = Math.max(ship.getBeginCoordinate().getY(), ship.getEndCoordinate().getY());

        if (maxY != 10) {
            return "O".equals(gameArray[maxY + 1][x]);
        } else {
            return false;
        }
    }

    private static boolean isOccupiedSpaceAboveDiagonalShip(Ship ship, String[][] gameArray) {
        int x = ship.getBeginCoordinate().getX();
        int minY = Math.min(ship.getBeginCoordinate().getY(), ship.getEndCoordinate().getY());
        if (minY != 1) {
            return "O".equals(gameArray[minY - 1][x]);
        } else {
            return false;
        }
    }

    private static boolean isHorizontalCorrectSpaceBetweenCoordinatesAsShipRequest(Ship ship) {

        return Math.abs(ship.getBeginCoordinate().getX() - ship.getEndCoordinate().getX()) == ship.getType().getSize() - 1;
    }

    private static boolean isHorizontal(Ship ship) {
        return ship.getBeginCoordinate().getY() == ship.getEndCoordinate().getY();
    }

    private static boolean isDiagonalCorrectLengthBeetweenCoordinatesAsShipRequest(Ship ship) {
        return Math.abs(ship.getBeginCoordinate().getY() - ship.getEndCoordinate().getY()) == ship.getType().getSize() - 1;
    }

    private static boolean isOccupiedWrightSideOfDiagonalShip(Ship ship, String[][] battleField) {
        int minY = Math.min(ship.getBeginCoordinate().getY(), ship.getEndCoordinate().getY());
        int maxY = Math.max(ship.getBeginCoordinate().getY(), ship.getEndCoordinate().getY());
        int x = ship.getEndCoordinate().getX();

        if (x != BattleField.LAST_COLUMN) {
            for (int i = minY; i <= maxY; i++) {
                if ("O".equals(battleField[i][x + 1])) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private static void printErrorTooClose() {
        System.out.println("Error! You placed it too close to another one. Try again:");
    }

    private static boolean isDiagonal(Ship ship) {
        return ship.getBeginCoordinate().getX() == ship.getEndCoordinate().getX();
    }

    private static Coordinates createCoordinatesFromString(String coordinates) {
        StringBuilder stringBuilder = new StringBuilder(coordinates);
        char rowLetterChar = coordinates.charAt(0);
        String rowLetterString = String.valueOf(rowLetterChar);
        int y = convertRowLetterStringToIntCoordinates(rowLetterString);
        stringBuilder.deleteCharAt(0);

        int x = Integer.parseInt(stringBuilder.toString());

        return new Coordinates(x, y);
    }

    private static int convertRowLetterStringToIntCoordinates(String letter) {
        switch (letter.toUpperCase(Locale.ROOT)) {
            case "A":
                return 1;
            case "B":
                return 2;
            case "C":
                return 3;
            case "D":
                return 4;
            case "E":
                return 5;
            case "F":
                return 6;
            case "G":
                return 7;
            case "H":
                return 8;
            case "I":
                return 9;
            case "J":
                return 10;
        }
        return 0;
    }
}
