package wish_em_all;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author rajdeep
 *
 */
public class Graph {
	
	public List<String[]> getLiked(String uid) throws IOException {
		
		String query="SELECT page_id,pic,name FROM page where page_id IN (SELECT page_id FROM page_fan WHERE uid = "+uid+") and (  type=\"Food\" or type=\"Bag\" or type=\"Luggage\" or type=\"Clothing\" or type=\"Beverage\" or type=\"Games\" or type=\"Toys\" or type=\"Jewelery\" or type=\"Watches\" or type=\"kitchen\" or type=\"Outdoor Gear\" or type=\"sporting Goods\" or type=\"Video Game\" or type=\"Music\")";
		
		JSONObject json=FQueue.fb.fql(query);
		JSONArray likes=json.getJSONArray("data");
		
		List<String[]> objects=new ArrayList<String[]>();
		
		for(int i=0;i<likes.length();i++) {
			JSONObject obj=(JSONObject)likes.get(i);
			String set[]=new String[2];
			set[0]=(String)obj.get("name");
			set[1]=(String)obj.get("pic");
			objects.add(set);
		}
		
		return objects;
	}
	
	public Map<String,List<String>> getBdayPics() throws IOException {
		
		String date="10/02";
		
		String query="SELECT uid,name,birthday_date,pic_square FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1 = me()) and (strpos(birthday_date,\""+date+"/\")>=0) ORDER BY name";
		
		JSONObject json=FQueue.fb.fql(query);
		JSONArray list=json.getJSONArray("data");
		int len=list.length();
		
		Map<String,List<String>> map=new HashMap<String,List<String>>();
		
		//For each user having a birthday on that day.
		for(int i=0;i<len;i++) {
			JSONObject user=(JSONObject)list.get(i);
			query="SELECT object_id, pid,src_big,like_info.like_count FROM photo WHERE object_id IN (SELECT object_id FROM photo_tag WHERE subject =\""+user.get("uid")+"\") and pid IN(SELECT pid FROM photo WHERE object_id IN (SELECT object_id FROM photo_tag WHERE subject =me())) order by like_info.like_count desc";
			
			JSONObject data=FQueue.fb.fql(query);
			JSONArray photos=data.getJSONArray("data");
			
			List<String> pics=new ArrayList<String>();
			
			for(int j=1;j<photos.length();j++) {
				json=(JSONObject)photos.get(j);
				pics.add((String)json.get("src_big"));
			}
			
			map.put(""+user.get("uid"), pics);
		}
		
		return map;
	}

}
