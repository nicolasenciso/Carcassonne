public class RoadTile extends Tile{

    private static String[] typeRoads = new String[]{"right-left", "up-down", "four-way", "three-way", "corner"};
    private byte[] directions;
    public RoadTile(String typeTile, int[] coords, String typeRoad) {
        super(typeTile, coords);
        this.directions = new byte[]{1,1}; //check directions according to typeRoad

    }
}
