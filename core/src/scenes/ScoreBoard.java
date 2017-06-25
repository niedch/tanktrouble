package scenes;

import com.badlogic.gdx.scenes.scene2d.Stage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Tank.*;

/**
 * Created by Christoph on 17.03.2016.
 */
public class ScoreBoard{
    protected HashMap<String, Integer> scoreMap;
    public ScoreBoard(){
        scoreMap = new HashMap<String, Integer>();
    }

    public ScoreBoard(List<String> players){
        scoreMap = new HashMap<String, Integer>();
        for(String s : players){
            scoreMap.put(s,new Integer(0));
        }
    }

    public ScoreBoard(JSONArray arr){
        scoreMap = new HashMap<String, Integer>();
        for(int i = 0; i < arr.length(); i++){
            scoreMap.put(arr.getJSONObject(i).getString("name"),arr.getJSONObject(i).getInt("value"));
        }
    }

    public void addRow(String string, int value){
        scoreMap.put(string,new Integer(value));
    }

    public void addWin(String string){
        scoreMap.put(string, new Integer(scoreMap.get(string).intValue()+1));
    }

    public Object[] toArray(){
        List<String> list = new ArrayList<String>();
        for(Map.Entry<String,Integer> entry : scoreMap.entrySet()){
            list.add(entry.getKey() + " : "+entry.getValue().intValue());
        }

        return list.toArray();
    }

    public JSONArray toJSONArray(){
        JSONArray arr = new JSONArray();
        for(Map.Entry<String, Integer> entry : scoreMap.entrySet()){
            JSONObject obj = new JSONObject();
            obj.put("name",entry.getKey());
            obj.put("value", entry.getValue().intValue());
            arr.put(obj);
        }
        return arr;
    }

    public Map<String, Integer> getScoreMap() {
        return scoreMap;
    }
}
