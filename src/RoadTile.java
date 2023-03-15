public class RoadTile extends Tile{

    private static String[] typeRoads = new String[]{"right-left", "up-down", "four-way", "three-way", "corner"};
    private byte[] directions;
    public RoadTile(String typeTile, int[] coords, byte[] directions, String typeRoad) {
        super(typeTile, coords);
        this.directions = directions;

    }
}
