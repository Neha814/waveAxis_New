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
     * SignIn
     * @param localArrayList
     * @return
     */

    public HashMap SignIn(ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
        @SuppressWarnings("rawtypes")
        HashMap<String, String> localHashMap = new HashMap<String, String>();
        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "CheckCredentials.php?", "POST",
                            localArrayList)).toString());

            String status = localJSONObject.getString("Response");
            if (status.equalsIgnoreCase("true")) {

                localHashMap.put("Response", "true");
                localHashMap.put("MessageWhatHappen", localJSONObject.getString("MessageWhatHappen"));

            } else {
                localHashMap.put("Response", "false");
                localHashMap.put("MessageWhatHappen", localJSONObject.getString("MessageWhatHappen"));
            }
            return localHashMap;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localHashMap;

        }

    }

    /**
     * Reset Target Time
     * @param localArrayList
     * @return
     */


    public HashMap ResetTargetTime(ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
        @SuppressWarnings("rawtypes")
        HashMap<String, String> localHashMap = new HashMap<String, String>();
        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "ResetTargetTime.php?", "POST",
                            localArrayList)).toString());

            String status = localJSONObject.getString("ResponseCode");
            if (status.equalsIgnoreCase("true")) {

                localHashMap.put("Response", "true");
                localHashMap.put("MessageWhatHappen", localJSONObject.getString("MessageWhatHappen"));

            } else {
                localHashMap.put("Response", "false");
                localHashMap.put("MessageWhatHappen", localJSONObject.getString("MessageWhatHappen"));
            }
            return localHashMap;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localHashMap;

        }

    }

	/**
	 * Machine details
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
                localHashMap.put("default_part_no", features.getString("default_part_no"));
                localHashMap.put("default_operator", features.getString("default_operator"));
                localHashMap.put("default_operator_id", features.getString("default_operator_id"));
                localHashMap.put("default_part_id", features.getString("default_part_id"));
                localHashMap.put("default_part_image", features.getString("default_part_image"));

                Constants.partsList.clear();
                for(int i=0 ; i<parts.length();i++){
                    HashMap<String, String> localHashMap1 = new HashMap<String, String>();

                    localHashMap1.put("part_id", parts.getJSONObject(i).getString("part_id"));
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

	public ArrayList<HashMap<String, String>> getInterruptionList(
			ArrayList localArrayList) {
		ArrayList<HashMap<String, String>> localArrayList1 = new ArrayList<HashMap<String, String>>();

		try {

			JSONObject localJSONObject = new JSONObject(Html.fromHtml(
					this.json.makeHttpRequest(url + "getInterruptionList.php?",
							"POST", localArrayList)).toString());

			String resopnse = localJSONObject.getString("Response");
			if (resopnse.equalsIgnoreCase("true")) {

				JSONArray interruption = localJSONObject.getJSONObject("GetData").getJSONArray("interruption");
				for (int i = 0; i < interruption.length(); i++) {
					HashMap<String, String> localhashMap = new HashMap<String, String>();
					localhashMap.put("interruption_id", interruption.getJSONObject(i).getString("interruption_id"));
                    localhashMap.put("interruption_name", interruption.getJSONObject(i).getString("interruption_name"));
                    localhashMap.put("target_time", interruption.getJSONObject(i).getString("target_time"));

					localArrayList1.add(localhashMap);

				}

			}
			return localArrayList1;

		} catch (Exception ae) {
			ae.printStackTrace();
			return localArrayList1;

		}

	}

    /**
     * Fix Interrupt
     * @param localArrayList
     * @return
     */

    public HashMap fixInterrupt(ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
        @SuppressWarnings("rawtypes")
        HashMap<String, String> localHashMap = new HashMap<String, String>();
        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "InterruptionTimer.php?", "POST",
                            localArrayList)).toString());

            String status = localJSONObject.getString("ResponseCode");
            if (status.equalsIgnoreCase("true")) {

                localHashMap.put("Response", "true");
                localHashMap.put("MessageWhatHappen", localJSONObject.getString("MessageWhatHappen"));

            } else {
                localHashMap.put("Response", "false");
                localHashMap.put("MessageWhatHappen", localJSONObject.getString("MessageWhatHappen"));
            }
            return localHashMap;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localHashMap;

        }

    }


    /**
     * Settings
     * @param localArrayList
     * @return
     */

    public HashMap Settings(ArrayList localArrayList) {
        ArrayList<HashMap<String, String>> locallist = new ArrayList<HashMap<String, String>>();
        @SuppressWarnings("rawtypes")
        HashMap<String, String> localHashMap = new HashMap<String, String>();
        try {

            JSONObject localJSONObject = new JSONObject(Html.fromHtml(
                    this.json.makeHttpRequest(url + "SettingChange.php?", "POST",
                            localArrayList)).toString());

            String status = localJSONObject.getString("ResponseCode");
            if (status.equalsIgnoreCase("true")) {

                localHashMap.put("Response", "true");
                localHashMap.put("MessageWhatHappen", localJSONObject.getString("MessageWhatHappen"));

            } else {
                localHashMap.put("Response", "false");
                localHashMap.put("MessageWhatHappen", localJSONObject.getString("MessageWhatHappen"));
            }
            return localHashMap;

        } catch (Exception ae) {
            ae.printStackTrace();
            return localHashMap;

        }

    }



}
