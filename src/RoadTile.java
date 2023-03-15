public class RoadTile extends Tile{

    private byte[] directions;
    private enum DirectionsPairs {
        RIGHT_LEFT("1,3"),
        UP_DOWN("0,2"),
        FOUR_WAY("0,1,2,3"),
        THREE_WAY_1("0,1,3"),
        THREE_WAY_2("0,1,2"),
        THREE_WAY_3("1,2,3"),
        THREE_WAY_4("0,2,3"),
        CORNER_1("1,2"),
        CORNER_2("2,3"),
        CORNER_3("0,3"),
        CORNER_4("0,1");

        private String directions;
        private DirectionsPairs(String directions){
            this.directions = directions;
        }
        public String getDirections(){
            return directions;
        }

    }
    public RoadTile(String typeTile, int[] coords, boolean withDirection) {
        super(typeTile, coords);
        if(withDirection){
            // random selection of type road, all same probability
            byte a = (byte)DirectionsPairs.values().length;
        }


    }
}
