package wish_em_all;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author rajdeep
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		String url="http://wishemall.dnsd.me:7754/";
		String permissions="email,user_groups,friends_birthday,user_birthday,friends_photos,user_photos,publish_stream,publish_actions";
		
		FQueue.fb.setPermissions(permissions);
		
		final Jetty server=new Jetty();
		
		/*new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					server.startServer(7754);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}).start();*/
		
		server.startServer(7754);
		
		FQueue.enqueue("getAuthToken");
		FQueue.fb.showAuthDialog();
		
		Graph graph=new Graph();
		Map<String, List<String>> pics=graph.getBdayPics();
		
		for(Iterator<String> i=pics.keySet().iterator();i.hasNext();) {
			String uid=i.next();
			System.out.println(uid);
			
			for(Iterator<String> j=pics.get(uid).iterator();j.hasNext();) System.out.println(j.next());
		}
	}

}
