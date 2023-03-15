public class TileGenerator {

    public static Tile getTile(String typeTile, int[]coords, String optionalRoadType){
        Tile newTile;
        if(typeTile.equals("abbey")){
            newTile = new AbbeyTile(typeTile, coords);
        }else if(typeTile.equals("city")){
            newTile = new CityTile(typeTile, coords);
        }else if(typeTile.equals("road")){
            newTile = new RoadTile(typeTile, coords, optionalRoadType);
        }else{
            newTile = new EmptyTile(typeTile, coords);
        }
        return newTile;
    }
}
