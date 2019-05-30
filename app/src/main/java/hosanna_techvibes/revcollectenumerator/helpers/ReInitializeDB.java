package hosanna_techvibes.revcollectenumerator.helpers;

import android.content.ContentValues;
import android.content.Context;

import hosanna_techvibes.revcollectenumerator.databases.DataDB;

public class ReInitializeDB {
    
    Context context;
    DataDB dataDB = new DataDB();

    public ReInitializeDB(Context context) {
        this.context = context;
    }

    public boolean clearDB(){
        boolean users = dataDB.myConnection(context).deleteRecords("users");
        boolean _apartment_types = dataDB.myConnection(context).deleteRecords("_apartment_types");
        boolean assessment_rules = dataDB.myConnection(context).deleteRecords("assessment_rules");
        //boolean asset_types = dataDB.myConnection(context).deleteRecords("asset_types");
        boolean building_completions = dataDB.myConnection(context).deleteRecords("building_completions");
        boolean building_functions = dataDB.myConnection(context).deleteRecords("building_functions");
        boolean building_images = dataDB.myConnection(context).deleteRecords("building_images");
        boolean building_occupancies = dataDB.myConnection(context).deleteRecords("building_occupancies");
        boolean building_occupancy_types = dataDB.myConnection(context).deleteRecords("building_occupancy_types");
        boolean building_ownerships = dataDB.myConnection(context).deleteRecords("building_ownerships");
        boolean building_purposes = dataDB.myConnection(context).deleteRecords("building_purposes");
        boolean building_types = dataDB.myConnection(context).deleteRecords("building_types");
        boolean business_categories = dataDB.myConnection(context).deleteRecords("business_categories");
        boolean business_images = dataDB.myConnection(context).deleteRecords("business_images");
        boolean business_operations = dataDB.myConnection(context).deleteRecords("business_operations");
        boolean business_sectors = dataDB.myConnection(context).deleteRecords("business_sectors");
        boolean business_sub_sectors = dataDB.myConnection(context).deleteRecords("business_sub_sectors");
        boolean business_types = dataDB.myConnection(context).deleteRecords("business_types");
        boolean land_functions = dataDB.myConnection(context).deleteRecords("land_functions");
        boolean land_ownership = dataDB.myConnection(context).deleteRecords("land_ownerships");
        boolean land_purposes = dataDB.myConnection(context).deleteRecords("land_purposes");
        boolean land_types = dataDB.myConnection(context).deleteRecords("land_types");
        boolean taxpayer_items = dataDB.myConnection(context).deleteRecords("taxpayer_items");
        boolean tax_payers = dataDB.myConnection(context).deleteRecords("tax_payers");
        boolean wards = dataDB.myConnection(context).deleteRecords("wards");
        boolean _apartments = dataDB.myConnection(context).deleteRecords("_apartments");
        boolean _apartment_occupants = dataDB.myConnection(context).deleteRecords("_apartments_occupants");
        boolean buildings = dataDB.myConnection(context).deleteRecords("buildings");
        boolean businesses = dataDB.myConnection(context).deleteRecords("businesses");
        boolean lands = dataDB.myConnection(context).deleteRecords("lands");
        boolean business_structures = dataDB.myConnection(context).deleteRecords("business_structures");
        boolean profiles = dataDB.myConnection(context).deleteRecords("profiles");
        boolean streets = dataDB.myConnection(context).deleteRecords("streets");


        if(users && _apartment_types && assessment_rules && building_completions && building_images &&
                building_functions && building_occupancies && building_occupancy_types && building_ownerships
                && building_purposes && building_types && business_categories && business_images
                && business_operations && business_sub_sectors && business_sectors && business_types
                && land_functions && land_ownership && land_purposes && land_types && taxpayer_items
                && tax_payers && wards && _apartments && _apartment_occupants && buildings && businesses
                && lands && business_structures && profiles && streets){

            ContentValues a = new ContentValues();
            a.put("apartment_type_id", "100000");
            a.put("apartment_type","--Select Apartment Type--");

            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(a, "_apartment_types");

           /* ContentValues b = new ContentValues();
            b.put("asset_type_id", "100000");
            b.put("asset_type","--Select Asset Type--");
            b.put("service_id","080808");
            b.put("asset_status", "1");

            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(b, "asset_types");
*/
            ContentValues c = new ContentValues();
            c.put("building_completion_id", "100000");
            c.put("building_completion","--Select Building Completion--");

            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(c, "building_completions");

            ContentValues d = new ContentValues();
            d.put("building_function_id", "100000");
            d.put("building_function","--Select Building Function--");

            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(d, "building_functions");

            ContentValues e = new ContentValues();
            e.put("building_occupancy_id", "100000");
            e.put("building_occupancy","--Select Building Occupancy--");

            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(e, "building_occupancies");


            ContentValues f = new ContentValues();
            f.put("building_occupancy_type_id", "100000");
            f.put("building_occupancy_type","--Select Occupancy Type--");

            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(f, "building_occupancy_types");


            ContentValues g = new ContentValues();
            g.put("building_ownership_id", "100000");
            g.put("building_ownership","--Select Building Ownership--");

            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(g, "building_ownerships");


            ContentValues h = new ContentValues();
            h.put("building_purpose_id", "100000");
            h.put("building_purpose","--Select Building Purpose--");

            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(h, "building_purposes");

            ContentValues i = new ContentValues();
            i.put("building_type_id", "100000");
            i.put("building_type","--Select Building Type--");
            //i.put("service_id", "234001");

            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(i, "building_types");


            ContentValues j = new ContentValues();
            j.put("service_id", "234001");
            j.put("business_category","--Select Business Category--");

            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(j, "business_categories");


            ContentValues k = new ContentValues();
            k.put("service_id", "234001");
            k.put("business_operation","--Select Business Operation--");


            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(k, "business_operations");


            ContentValues l = new ContentValues();
            l.put("business_sector_id", "0");
            l.put("business_sector","--Select Business Sector--");
            l.put("service_id","2343345");


            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(l, "business_sectors");

            ContentValues m = new ContentValues();
            m.put("business_size_id", "100000");
            m.put("business_size","--Select Business Size--");

            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(m, "business_sizes");


            ContentValues n = new ContentValues();
            n.put("business_sub_sector_id", "0");
            n.put("business_sub_sector","--Select Business Sub Sector--");
            n.put("service_id","2343345");


            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(n, "business_sub_sectors");


            ContentValues o = new ContentValues();
            o.put("business_type_id", "0");
            o.put("business_type","--Select Business Type--");
            o.put("service_id","2343345");


            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(o, "business_types");


            ContentValues p = new ContentValues();
            p.put("land_function_id", "100000");
            p.put("land_function","--Select Function--");
            p.put("service_id","2343345");


            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(p, "land_functions");

            ContentValues q = new ContentValues();
            q.put("land_ownership_id", "100000");
            q.put("land_ownership","--Select Ownership--");
            q.put("service_id","2343345");


            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(q, "land_ownerships");


            ContentValues r = new ContentValues();
            //r.put("land_ownership_id", "100000");
            r.put("business_structure","--Select Business Structure--");
            r.put("service_id","0000000");


            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(r, "business_structures");


            ContentValues s = new ContentValues();
            s.put("land_purpose_id", "100000");
            s.put("land_purpose","--Select Land Purpose--");
            s.put("service_id","0000000");


            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(s, "land_purposes");


            ContentValues t = new ContentValues();
            t.put("land_type_id", "100000");
            t.put("land_type","--Select Land Type--");
            t.put("service_id","0000000");


            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(t, "land_types");

            ContentValues u = new ContentValues();
            u.put("profile_id", "0");
            u.put("profile_ref","--Select Profile--");
            u.put("service_id","0000000");


            dataDB.myConnection(context.getApplicationContext()).onInsertOrUpdate(u, "profiles");



            return true;

        }
        return false;
    }

}
