public class TileGenerator {

    public static Tile getTile(String typeTile, int[]coords, boolean withDirection){
        Tile newTile;
        if(typeTile.equals("abbey")){
            newTile = new AbbeyTile(typeTile, coords);
        }else if(typeTile.equals("city")){
            newTile = new CityTile(typeTile, coords);
        }else if(typeTile.equals("road")){
            newTile = new RoadTile(typeTile, coords, withDirection);
        }else if(typeTile.equals("empty")){
            newTile = new EmptyTile(typeTile, coords);
        }else{
            newTile = new DealtTile(typeTile, coords);
        }
        return newTile;
    }
}
