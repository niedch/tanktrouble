package Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import Tank.Tank;


public class Map {
    private TiledMap map;
    private TiledMapRenderer tmr;
    private OrthographicCamera cam;

    public Map(World world, OrthographicCamera cam){
        this.cam = cam;

        FileHandle[] maps = Gdx.files.internal(Constants.General.MAP_PATH).list(".tmx");

        FileHandle mapFile = maps[MathUtils.random(0, maps.length-1)];

        System.out.println(mapFile.name());
        map = new TmxMapLoader().load(Constants.General.MAP_PATH + mapFile.name());
        tmr = new OrthogonalTiledMapRenderer(map);


        TiledMapTileLayer layer0 = (TiledMapTileLayer) map.getLayers().get(0);

        Vector3 center = new Vector3(layer0.getWidth() * layer0.getTileWidth()/ 2, layer0.getHeight() * layer0.getTileHeight() / 2, 0);
        cam.position.set(center);

        float mapWidth = layer0.getWidth() * layer0.getTileWidth();
        float mapHeight = layer0.getHeight() * layer0.getTileHeight();

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        System.out.println(mapWidth + " : " + mapHeight);

        if(mapWidth > mapHeight){
            cam.zoom = mapHeight / screenWidth;

        }else if(mapWidth <= mapHeight){
            cam.zoom = mapHeight / screenHeight;
        }

        cam.update();

        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        BodyDef bodyDef = new BodyDef();
        Body body;

        for(MapObject object: map.getLayers().get("Collision").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX()+ rect.getWidth()/2),(rect.getY()+rect.getHeight()/2) );

            body = world.createBody(bodyDef);

            polygonShape.setAsBox(rect.getWidth()/2,rect.getHeight()/2);
            fixtureDef.shape = polygonShape;
            fixtureDef.restitution = 0f;
            body.createFixture(fixtureDef);
        }

        List<Integer> tmpArray = new ArrayList<Integer>();
        for(Tank tank :Tank.tanks){
            int randomNumber = ThreadLocalRandom.current().nextInt(0, map.getLayers().get("TankSpawnPoints").getObjects().getCount());
            while(tmpArray.contains(randomNumber))
                randomNumber = ThreadLocalRandom.current().nextInt(0, map.getLayers().get("TankSpawnPoints").getObjects().getCount());

            tmpArray.add(new Integer(randomNumber));


            Rectangle rect = ((RectangleMapObject)map.getLayers().get("TankSpawnPoints").getObjects().getByType(RectangleMapObject.class).get(randomNumber)).getRectangle();
            tank.create(new Vector2((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2)));
        }
    }

    public Map(World world, OrthographicCamera cam, String file, JSONArray spawnPoints){
        this.cam = cam;
        map = new TmxMapLoader().load(file);
        tmr = new OrthogonalTiledMapRenderer(map);

        TiledMapTileLayer layer0 = (TiledMapTileLayer) map.getLayers().get(0);

        Vector3 center = new Vector3(layer0.getWidth() * layer0.getTileWidth()/ 2, layer0.getHeight() * layer0.getTileHeight() / 2, 0);
        cam.position.set(center);

        float mapWidth = layer0.getWidth() * layer0.getTileWidth();
        float mapHeight = layer0.getHeight() * layer0.getTileHeight();

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        if(mapWidth > mapHeight){
            cam.zoom = mapHeight / screenWidth;

        }else if(mapWidth <= mapHeight){
            cam.zoom = mapHeight / screenHeight;
        }

        cam.update();

        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        BodyDef bodyDef = new BodyDef();
        Body body;

        for(MapObject object: map.getLayers().get("Collision").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX()+ rect.getWidth()/2),(rect.getY()+rect.getHeight()/2) );

            body = world.createBody(bodyDef);

            polygonShape.setAsBox(rect.getWidth()/2,rect.getHeight()/2);
            fixtureDef.shape = polygonShape;
            fixtureDef.restitution = 0f;
            body.createFixture(fixtureDef);
        }

        for(int i = 0; i< spawnPoints.length(); i++){
            JSONObject obj = spawnPoints.getJSONObject(i);
            for( Tank tank : Tank.tanks){
                if(tank.getTankName().equals(obj.getString("name"))){
                    tank.create(new Vector2((obj.getInt("posX") + obj.getInt("width")/2),(obj.getInt("posY")+obj.getInt("height")/2)));
                }
            }
        }
    }

    public void draw(){
        tmr.setView(cam);
        tmr.render();
    }
}
