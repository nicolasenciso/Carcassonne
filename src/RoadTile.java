public class RoadTile extends Tile{

    private static String[] typeRoads = new String[]{"right-left", "up-down", "four-way", "three-way", "corner"};
    public RoadTile(String typeTile, String[] coords, byte[] directions, String typeRoad) {
        super(typeTile, coords, directions);

    }
}
