package hosanna_techvibes.revcollectenumerator.helpers;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import hosanna_techvibes.revcollectenumerator.R;
import hosanna_techvibes.revcollectenumerator.model.AuthorizationHttpResponse;

public class PullGuestData {

    private Context context;
    private JSONObject jsonObject;
    private MyJSON pull = new MyJSON();
    private String TAG = "PullGuestData";
    JSONObject savedData;


    public PullGuestData(Context context, JSONObject jsonObject) {
        this.context = context;
        this.jsonObject = jsonObject;
        savedData = new JSONObject();
    }

    public JSONObject pullData(){
        try {
            if (jsonObject.length() > 0) {
                //success
                String apartmenttypeids = pull.AnalyzeApartmentType(context, jsonObject.getJSONArray("synch_apartment_types"));
                String areacodeids = pull.AnalyzeAreaCodes(context, jsonObject.getJSONArray("synch_areacodes"));
                String buildingcompletionids = pull.AnalyzeBuildingCompletions(context, jsonObject.getJSONArray("synch_building_completions"));
                String buildingfunctionids = pull.AnalyzeBuildingFunctions(context, jsonObject.getJSONArray("synch_building_functions"));
                String buldingoccupancyids = pull.AnalyzeBuildingOccupancies(context, jsonObject.getJSONArray("synch_building_occupancies"));
                String occupancytypeids = pull.AnalyzeBuildingOccupancyType(context, jsonObject.getJSONArray("synch_building_occupancy_types"));
                String buildingownershipids = pull.AnalyzeBuildingOwnership(context, jsonObject.getJSONArray("synch_building_ownerships"));
                String buildingpurposeids = pull.AnalyzeBuildingPurpose(context, jsonObject.getJSONArray("synch_building_purposes"));
                String businesscategoryids = pull.AnalyzeBusinessCategory(context, jsonObject.getJSONArray("synch_business_categories"));
                String businessoperationids = pull.AnalyzeBusinessOperations(context, jsonObject.getJSONArray("synch_business_operations"));
                String businesssectorids = pull.AnalyzeBusinessSectors(context, jsonObject.getJSONArray("synch_business_sectors"));
                String businesssizeids = pull.AnalyzeBusinessSize(context, jsonObject.getJSONArray("synch_business_sizes"));
                String businessstructureids = pull.AnalyzeBusinessStructure(context, jsonObject.getJSONArray("synch_business_structures"));
                String businesssubsectorids = pull.AnalyzeBusinessSubStructure(context, jsonObject.getJSONArray("synch_business_sub_sectors"));
                String businesstypesids = pull.AnalyzeBusinessTypes(context, jsonObject.getJSONArray("synch_business_types"));
                String landfunctionids = pull.AnalyzeLandFunctions(context, jsonObject.getJSONArray("synch_land_functions"));
                String landownershipids = pull.AnalyzeLandOwnership(context, jsonObject.getJSONArray("synch_land_ownerships"));
                String landpurposeids = pull.AnalyzeLandPurposes(context, jsonObject.getJSONArray("synch_land_purposes"));
                String landtypeids = pull.AnalyzeLandTypes(context, jsonObject.getJSONArray("synch_land_types"));
                String streetids = pull.AnalyzeStreet(context, jsonObject.getJSONArray("synch_streets"));
                String buildingtypeids = pull.AnalyzeBuildingTypes(context, jsonObject.getJSONArray("synch_building_types"));
                String profileids = pull.AnalyzeProfiles(context, jsonObject.getJSONArray("synch_profiles"));
                String assessmentruleids = pull.AnalyzeAssessmentRule(context, jsonObject.getJSONArray("synch_assessment_rules"));
                String wardids = pull.AnalyzeWards(context, jsonObject.getJSONArray("synch_wards"));



                savedData.put("synch_apartment_types", apartmenttypeids);
                savedData.put("synch_areacodes", areacodeids);
                savedData.put("synch_building_completions", buildingcompletionids);
                savedData.put("synch_building_functions", buildingfunctionids);
                savedData.put("synch_building_occupancies", buldingoccupancyids);
                savedData.put("synch_building_occupancy_types", occupancytypeids);
                savedData.put("synch_building_ownerships", buildingownershipids);
                savedData.put("synch_building_purposes", buildingpurposeids);
                savedData.put("synch_business_categories", businesscategoryids);
                savedData.put("synch_business_operations", businessoperationids);
                savedData.put("synch_business_sectors", businesssectorids);
                savedData.put("synch_business_sizes", businesssizeids);
                savedData.put("synch_business_structures", businessstructureids);
                savedData.put("synch_business_sub_sectors", businesssubsectorids);
                savedData.put("synch_business_types", businesstypesids);
                savedData.put("synch_land_functions", landfunctionids);
                savedData.put("synch_land_ownerships", landownershipids);
                savedData.put("synch_land_purposes", landpurposeids);
                savedData.put("synch_land_types", landtypeids);
                savedData.put("synch_streets", streetids);
                savedData.put("synch_building_types", buildingtypeids);
                savedData.put("synch_profiles", profileids);
                savedData.put("synch_assessment_rules", assessmentruleids);
                savedData.put("synch_wards", wardids);



                Log.e(TAG, "saveddata ::: " + savedData.toString());

//                callServiceMod(getString(R.string.verifysync), savedData);

                //db code


                return savedData;
                //return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return savedData;
    }




}
