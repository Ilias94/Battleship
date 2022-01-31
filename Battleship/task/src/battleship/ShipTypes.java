package battleship;

public enum ShipTypes {
    AIRCAFT_CARRIER("Aircraft Carrier", 5), BATTLESHIP("Battleship", 4), SUBMARINE("Submarine", 3),
    CRUISER("Cruiser", 3), DESTROYER("Destroyer", 2);

    private final String name;
    private final int size;

    ShipTypes(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
