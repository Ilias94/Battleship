package battleship;

public class Ship {
    private Coordinates beginCoordinate;
    private Coordinates endCoordinate;
    private ShipTypes type;

    public Ship(Coordinates beginCoordinate, Coordinates endCoordinate, ShipTypes shipType) {
        this.beginCoordinate = beginCoordinate;
        this.endCoordinate = endCoordinate;
        this.type = shipType;
    }

    public Coordinates getBeginCoordinate() {
        return beginCoordinate;
    }

    public void setBeginCoordinate(Coordinates beginCoordinate) {
        this.beginCoordinate = beginCoordinate;
    }

    public Coordinates getEndCoordinate() {
        return endCoordinate;
    }

    public void setEndCoordinate(Coordinates endCoordinate) {
        this.endCoordinate = endCoordinate;
    }

    public ShipTypes getType() {
        return type;
    }

    public void setType(ShipTypes type) {
        this.type = type;
    }
}
