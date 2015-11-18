package functions;

import android.text.Html;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Functions {

	JSONParser json = new JSONParser();

    public static String url="http://phphosting.osvin.net/waveaxisNew/Web_API/";

	/**
	 * Login
	 * 
	 * @param localArrayList
	 * @return
	 */

	public HashMap login(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "login.php?", "POST",
							localArrayList)).toString());

			String status = localJSONObject.getString("ResponseCode");
			if (status.equalsIgnoreCase("true")) {
				localHashMap.put("ResponseCode", "true");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));
				localHashMap.put("user_id",
						localJSONObject.getString("user_id"));

				localHashMap.put("first_name",
						localJSONObject.getString("first_name"));
				localHashMap.put("last_name",
						localJSONObject.getString("last_name"));
				localHashMap.put("latitude",
						localJSONObject.getString("latitude"));
				localHashMap.put("longitude",
						localJSONObject.getString("longitude"));
				// localHashMap.put("access_token",localJSONObject.getString("access_token"));

				localHashMap.put("email", localJSONObject.getString("email"));
                localHashMap.put("login_via", localJSONObject.getString("login_via"));

			} else {
				localHashMap.put("ResponseCode", "false");
				localHashMap.put("Message",
						localJSONObject.getString("Message"));
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}


	/**
	 * category Listing
	 */

	public ArrayList<HashMap<String, String>> getCategoryList(
			ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "listCategories.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("ResponseCode");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray Data = localJSONObject.getJSONArray("result");
				for (int i = 0; i < Data.length(); i++) {
					HashMap<String, String> localhashMap = new HashMap<String, String>();
					localhashMap.put("id", Data.getJSONObject(i)
							.getString("id"));
					localhashMap.put("title",
							Data.getJSONObject(i).getString("title"));
					localhashMap.put("image",
							Data.getJSONObject(i).getString("image"));
					localhashMap.put("icon1",
							Data.getJSONObject(i).getString("icon1"));
					localhashMap.put("icon2",
							Data.getJSONObject(i).getString("icon2"));

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}


}
