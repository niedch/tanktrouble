package Utils;

import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class FizzXJSonLoader {
	private FileHandle fileHandle;
	private String content;
	private float PIXLES_TO_METERS;
	private int bodyIndex;
	
	public void loadContent() throws IOException{
		content = fileHandle.readString();
	}

	public List<Sprite> getPictureList() throws JSONException {
		String path = fileHandle.pathWithoutExtension().substring(0,fileHandle.pathWithoutExtension().indexOf(fileHandle.nameWithoutExtension()));
		List<Sprite> list = new ArrayList<Sprite>();
		JSONArray imageArr = new JSONObject(content).getJSONObject("box2d").getJSONObject("images").getJSONArray("image");

		for(int i = 0; i< imageArr.length(); i++){
			JSONObject image = imageArr.getJSONObject(i);
			Sprite sprite = new Sprite(new Texture(Gdx.files.internal(path+image.get("path"))));
			sprite.setScale(image.getInt("scaleX"),image.getInt("scaleY"));
			sprite.setRotation(image.getInt("rotation"));
			list.add(sprite);
		}
		return list;
	}
	
	public Body getBody(World world,Vector2 position,List<FixtureDef> fixtureList) throws JSONException {
		JSONObject bodyObj = getJSONBody().getJSONObject(bodyIndex);
		BodyDef bodyDef = new BodyDef();
		String typeValue = bodyObj.getString("type");
		if (typeValue.equals("kinematic")) {
			bodyDef.type = BodyType.KinematicBody;
		} else if (typeValue.equals("static")) {
			bodyDef.type = BodyType.StaticBody;
		} else if (typeValue.equals("dynamic")) {
			bodyDef.type = BodyType.DynamicBody;
		}
		bodyDef.position.set(position);
		Body body = world.createBody(bodyDef);
		for (FixtureDef fixtureDef : fixtureList) {
			body.createFixture(fixtureDef);
		}
		return body;
	}
	
	public List<FixtureDef> getFixtureList(int bodyIndex) throws JSONException{
		List<FixtureDef> fixtureList = new ArrayList<FixtureDef>();
		this.bodyIndex = bodyIndex;
		JSONArray fixtures = getJSONBody().getJSONObject(bodyIndex).getJSONObject("fixtures").getJSONArray("fixture");
		for(int i = 0; i < fixtures.length(); i++){
			JSONObject fixtureObj = fixtures.getJSONObject(i);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.density = (float) fixtureObj.getDouble("density");
			fixtureDef.restitution = (float) fixtureObj.getDouble("restitution");
			fixtureDef.friction = (float) fixtureObj.getDouble("friction");
			try {
				fixtureDef.isSensor = fixtureObj.getBoolean("isSensor");
			} catch (JSONException e) {
				fixtureDef.isSensor = false;
			}
			fixtureDef.shape = getShape(fixtureObj);
			fixtureList.add(fixtureDef);
		}
		return fixtureList;
	}
	
	private Shape getShape(JSONObject fixtureObj) throws JSONException{
		PolygonShape polygonShape;
		ChainShape chainShape;
		if(fixtureObj.getString("shapeType").equals("polygonShape")){
			JSONArray vertex = fixtureObj.getJSONArray("vertex");
			Vector2[] vertices = new Vector2[vertex.length()];
			for(int i = 0; i < vertex.length(); i++){
				vertices[i] = new Vector2(vertex.getJSONObject(i).getInt("x")/PIXLES_TO_METERS,vertex.getJSONObject(i).getInt("y")/PIXLES_TO_METERS);
			}
			polygonShape = new PolygonShape();
			polygonShape.set(vertices);
			return polygonShape;
		}else if(fixtureObj.getString("shapeType").equals("edgeShape")){
			JSONArray vertex = fixtureObj.getJSONArray("vertex");
			Vector2[] vertices = new Vector2[vertex.length()+1];
			for(int i = 0; i < vertex.length(); i++){
				vertices[i] = new Vector2(vertex.getJSONObject(i).getInt("x")/PIXLES_TO_METERS, vertex.getJSONObject(i).getInt("y")/PIXLES_TO_METERS);
			}
			vertices[vertex.length()] = vertices[0]; 
			chainShape = new ChainShape();
			chainShape.createChain(vertices);
			return chainShape;
		}
		return null;
	}
	
	private JSONArray getJSONBody() throws JSONException{
		return new JSONObject(content).getJSONObject("box2d").getJSONObject("bodies").getJSONArray("body");
	}
	
	public FizzXJSonLoader(){}
	
	public FizzXJSonLoader(FileHandle fileHandle,float PIXELS_TO_METERS) {
		this.fileHandle = fileHandle;
		this.PIXLES_TO_METERS = PIXELS_TO_METERS;
	}

	public FileHandle getFileHandle() {
		return fileHandle;
	}

	public void setFileHandle(FileHandle fileHandle) {
		this.fileHandle = fileHandle;
	}
}
