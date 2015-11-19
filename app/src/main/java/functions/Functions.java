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
	 * Add device
	 * @param localArrayList
	 * @return
	 */

	public HashMap AddDevice(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "addDevice.php?", "POST",
							localArrayList)).toString());

			String status = localJSONObject.getString("Response");
			if (status.equalsIgnoreCase("true")) {
				JSONObject GetData = localJSONObject.getJSONObject("GetData");


				localHashMap.put("Response", "true");
				localHashMap.put("deviceid", GetData.getString("deviceid"));

			} else {
				localHashMap.put("Response", "false");
			}
			return localHashMap;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localHashMap;

		}

	}

	/**
	 * Login
	 * 
	 * @param localArrayList
	 * @return
	 */

	public HashMap GetMachineDetails(ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
		@SuppressWarnings("rawtypes")
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "getMachinenModule.php?", "POST",
							localArrayList)).toString());

			String status = localJSONObject.getString("Response");
			if (status.equalsIgnoreCase("true")) {
				JSONObject GetData = localJSONObject.getJSONObject("GetData");
				JSONObject machine = GetData.getJSONObject("machine");
				JSONObject features = GetData.getJSONObject("features");
				JSONArray parts = GetData.getJSONArray("parts");
                JSONArray operators = GetData.getJSONArray("operators");


                localHashMap.put("Response", "true");
                localHashMap.put("machine_id", machine.getString("machine_id"));
                localHashMap.put("machine_name", machine.getString("machine_name"));

                localHashMap.put("equipment_effectiveness", features.getString("equipment_effectiveness"));
                localHashMap.put("no_of_spindleRun", features.getString("no_of_spindleRun"));
                localHashMap.put("cp", features.getString("cp"));
                localHashMap.put("cpk", features.getString("cpk"));
                localHashMap.put("quality_issue", features.getString("quality_issue"));

                Constants.partsList.clear();
                for(int i=0 ; i<parts.length();i++){
                    HashMap<String, String> localHashMap1 = new HashMap<String, String>();

                    localHashMap1.put("part_no", parts.getJSONObject(i).getString("part_no"));
                    localHashMap1.put("part_image", parts.getJSONObject(i).getString("part_image"));
                    localHashMap1.put("created", parts.getJSONObject(i).getString("created"));

                    Constants.partsList.add(localHashMap1);
                }

                Constants.operatorList.clear();
                for(int i=0 ; i<operators.length();i++){
                    HashMap<String, String> localHashMap2 = new HashMap<String, String>();

                    localHashMap2.put("operator_id", operators.getJSONObject(i).getString("operator_id"));
                    localHashMap2.put("operator_name", operators.getJSONObject(i).getString("operator_name"));

                    Constants.operatorList.add(localHashMap2);
                }


			} else {
				localHashMap.put("Response", "false");
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
