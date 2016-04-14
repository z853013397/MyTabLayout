package com.zeyuan.kyq.utils;


import android.content.Context;

import com.squareup.okhttp.Request;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.TNMObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-11-09
 * Time: 14:11
 * FIXME
 * 手动解析 配置信息的类
 */


public final class PraSync {

    public interface PraCallback {
        void isFinish(boolean isFinish);

        void isFailed();
    }

    private static final String CANCER2_STEP = "Cancer2Step";
    private static final String DATA = "data";
    private static final String MAXTIMESTAMP = "maxtimestamp";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String STEP = "step";

    private static final String CIRCLE_ID_DATA = "CircleIDData";
    private static final String Citydata = "Citydata";
    private static final String Cancerdata = "Cancerdata";
    private static final String CircleID = "CircleID";
    private static final String CircleName = "CircleName";

    private static final String Provincecode = "Provincecode";
    private static final String Citycode = "Citycode";

    private static final String Genetic_Mutation = "GeneticMutation";
    private static final String Cancer_Transfer_Pos = "CancerTransferPos";
    private static final String PERFORM = "perform";
    private static final String BODYPOS = "BodyPos";
    private static final String Perform = "Perform";
    private static final String Cancer = "Cancer";
    private static final String ParentID = "ParentID";
    private static final String CityIDData = "CityIDData";
    private static final String CITY = "City";
    private static final String OtherStep = "OtherStep";


    /**
     * 分期字段
     */
    private static final String DIGIT = "digit";
    private static final String DIGIT_DATA = "digitdata";
    private static final String DIGIT_CANCER = "digitcancer";
    private static final String TNM = "TNM";
    private static final String TNM_DIGIT = "TNMDigit";
    private static final String DIGIT_ID_ALL = "DigitIDAll";
    private static final String CANCER_ID_ALL = "CancerIDAll";

    private static final String T = "T";
    private static final String N = "N";
    private static final String M = "M";

    private static final String CONTENT = "content";
    private static final String CANCER_ID = "CancerID";
    private static final String TNM_ID = "TNMID";
    private static final String DIGIT_ID = "DigitID";
    private static final String T_ID = "Tid";
    private static final String M_ID = "Mid";
    private static final String N_ID = "Nid";
    private static final String GeneticMutation = "GeneticMutation";
    private static final String gene = "gene";
    private static final String StepUnion = "StepUnion";
    private static final String CURECONFID = "cureconfid";
    private static final String ALL = "0";
    private static final String stepunionid = "stepunionid";
    private static final String cureconfid = "cureconfid";
    private static final String stepallname = "stepallname";


    public static void praConifg(String path, Context context, final PraCallback callback) {
        Map<String, String> map = new HashMap<>();
//        map.put(Contants.InfoID, DataUtils.getInfoID(context));
        OkHttpClientManager.postAsyn(path, map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                parseJson(response, callback);
            }

        });
    }

    private static void parseJson(String response, final PraCallback callback) {
        try {
            JSONObject object = new JSONObject(response);

            /**
             * 解析cancer2step
             * 存储的是2个map
             * map<cancerid, cureidlist>
             * map<cureid,curelist>
             *cureValues 存折键值对
             **/
            JSONObject cure = object.getJSONObject(CANCER2_STEP);
            JSONArray cureData = cure.getJSONArray(DATA);
            String cureMaxtimestamp = cure.getString(MAXTIMESTAMP);//cure的时间戳
//            LogUtil.i("MainActivity","cure数据的时间戳"+cureMaxtimestamp);
            for (int i = 0; i < cureData.length(); i++) {
                JSONObject dataItem = cureData.getJSONObject(i);
                String cancerId = dataItem.getString("CancerID");
                JSONArray cureArray = dataItem.getJSONArray("cure");
                LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
                for (int j = 0; j < cureArray.length(); j++) { //到达 cure节点
                    JSONObject cureItem = cureArray.getJSONObject(j);
                    String cureId = cureItem.getString("id");
                    String curename = cureItem.getString("name");
                    GlobalData.cureValues.put(cureId, curename);
                    JSONObject step = cureItem.getJSONObject("step");
//                    LogUtil.i("MainActivity", step.toString());
                    List<String> cureidList = new ArrayList<>();
                    Iterator<String> itemKey = step.keys();
//                    List<String>
                    String key = "";
                    while (itemKey.hasNext()) {//循环的取key和values
                        key = itemKey.next();
//                    list.add(key);
                        cureidList.add(key);
                        GlobalData.cureValues.put(key, step.getString(key));
                        GlobalData.cureId.put(key, cureId);
                    }
                    map.put(cureId, cureidList);
                }
                GlobalData.cancerCure.put(cancerId, map);

            }

            /**
             * 解析cancer 注释可以看下面的解析city
             */
            JSONObject cancer = object.getJSONObject(Cancer);
            JSONArray cancerData = cancer.getJSONArray(DATA);
            String cancerMaxtimestamp = cancer.getString(MAXTIMESTAMP);//cancer的时间戳
//            LogUtil.i("MainActivity", "癌肿数据的时间戳" + cancerMaxtimestamp);
            GlobalData.cancerValues.put("0", "全部");

            for (int i = 0; i < cancerData.length(); i++) {
                JSONObject dataItem = cancerData.getJSONObject(i);
                String id = dataItem.getString(ID);
                String name = dataItem.getString(NAME);
                if ("1".equals(id)) {
                    continue;
                }
                GlobalData.cancerValues.put(id, name);
                String parentId = dataItem.getString(ParentID);
                if (GlobalData.cancerData.containsKey(parentId)) {
                    List<String> list = GlobalData.cancerData.get(parentId);
                    list.add(id);
                } else {
                    List<String> datalist = new ArrayList<>();
                    datalist.add(id);
                    GlobalData.cancerData.put(parentId, datalist);
                }
            }


            /**
             * 解析city
             */
            JSONObject cityIDData = object.getJSONObject(CityIDData);
            JSONArray cityData = cityIDData.getJSONArray(DATA);//city字段下的data
            String cityMaxtimestamp = cityIDData.getString(MAXTIMESTAMP);//时间戳
            for (int i = 0; i < cityData.length(); i++) {
                JSONObject dataItem = cityData.getJSONObject(i);//data下的每个item
                String id = dataItem.getString(ID);//省份的id
                String name = dataItem.getString(NAME);//对应省份的name
                GlobalData.cityValues.put(id, name);
                JSONObject city = dataItem.getJSONObject(CITY);//这个object里面装着一个省下的城市
                List<String> keyList = new ArrayList<>();
                String key;
                Iterator<String> iteratorKey = city.keys();//
                while (iteratorKey.hasNext()) {//这儿不是顺序的.
                    key = iteratorKey.next();
                    keyList.add(key);
                    GlobalData.cityValues.put(key, city.getString(key));
                }
                GlobalData.cityData.put(id, keyList);//
            }


            /**
             * 解析perform
             */
            JSONObject perfrom = object.getJSONObject(PERFORM);
            JSONObject perfromData = perfrom.getJSONObject(DATA);
            String maxtimestamp = perfrom.getString(MAXTIMESTAMP);
            Iterator<String> itemKey1 = perfromData.keys();
            String key4 = "";
            while (itemKey1.hasNext()) {
                key4 = itemKey1.next();
                GlobalData.performValues.put(key4, perfromData.getString(key4));//这儿put的时候根据需求来写
            }


            /**
             * 解析bodypos
             */
            JSONObject bodypos = object.getJSONObject(BODYPOS);
            JSONArray bodyposData = bodypos.getJSONArray(DATA);
            String bodyposMaxtimestamp = bodypos.getString(MAXTIMESTAMP);//c的时间戳
            for (int i = 0; i < bodyposData.length(); i++) {
                JSONObject dataItem = bodyposData.getJSONObject(i);
                String id = dataItem.getString(ID);
                String name = dataItem.getString(NAME);
                GlobalData.performValues.put(id, name);
                JSONArray perform = dataItem.getJSONArray(Perform);
                List<String> list = new ArrayList<>();
                for (int j = 0; j < perform.length(); j++) {
                    String item = (String) perform.get(j);
                    list.add(item);
                }
                GlobalData.bodyPos.put(id, list);
            }

            /**
             * 解析digit
             */
            JSONObject digit = object.getJSONObject(DIGIT);//节点digit
            String digitmaxtimestamp = digit.getString(MAXTIMESTAMP);//时间戳
            JSONObject digitData = digit.getJSONObject(DIGIT_DATA);//节点digit的digitData
            Iterator<String> keysIterator = digitData.keys();
            String key;
            while (keysIterator.hasNext()) {
                key = keysIterator.next();
                GlobalData.DigitValues.put(key, digitData.getString(key));
            }
            GlobalData.DigitValues.put("0", "未知");

            //-----------------------节点digit的digitCancer
            JSONArray digitCancer = digit.getJSONArray(DIGIT_CANCER);//节点digit的digitCancer
            for (int i = 0; i < digitCancer.length(); i++) {
                JSONObject digitCancerItem = digitCancer.getJSONObject(i);
                String cancerId = digitCancerItem.getString(CANCER_ID);
                JSONArray digitId = digitCancerItem.getJSONArray(DIGIT_ID);
                List<String> digitIdList = new ArrayList<>();
                for (int j = 0; j < digitId.length(); j++) {
                    String digitIdItem = (String) digitId.get(j);
                    digitIdList.add(digitIdItem);
                }
                GlobalData.digitData.put(cancerId, digitIdList);
            }
            //--------------------------------------------------------------------
            //-----------------------节点digit的TNM
            JSONArray tnm = digit.getJSONArray(TNM);
            for (int i = 0; i < tnm.length(); i++) {
                JSONObject tnmItem = tnm.getJSONObject(i);
                String cancerId = tnmItem.getString(CANCER_ID);

                JSONArray t = tnmItem.getJSONArray(T);
                List<String> listT = new ArrayList<>();
                for (int j = 0; j < t.length(); j++) {
                    JSONObject titem = t.getJSONObject(j);
                    String id = titem.getString(ID);
                    String name = titem.getString(NAME);
                    String content = titem.getString(CONTENT);
                    GlobalData.DigitValues.put(id, name + " " + content);
                    listT.add(id);
                }
                GlobalData.digitT.put(cancerId, listT);

                JSONArray n = tnmItem.getJSONArray(N);
                List<String> listN = new ArrayList<>();
                for (int j = 0; j < n.length(); j++) {
                    JSONObject titem = n.getJSONObject(j);
                    String id = titem.getString(ID);
                    String name = titem.getString(NAME);
                    String content = titem.getString(CONTENT);
                    listN.add(id);
                    GlobalData.DigitValues.put(id, name + " " + content);
                }
                GlobalData.digitN.put(cancerId, listN);

                JSONArray m = tnmItem.getJSONArray(M);
                List<String> listM = new ArrayList<>();
                for (int j = 0; j < m.length(); j++) {
                    JSONObject titem = m.getJSONObject(j);
                    String id = titem.getString(ID);
                    String name = titem.getString(NAME);
                    String content = titem.getString(CONTENT);
                    listM.add(id);
                    GlobalData.DigitValues.put(id, name + " " + content);
                }
                GlobalData.digitM.put(cancerId, listM);
            }
            //-----------------------

            //-----------------------节点digit的TNMDigit
            JSONArray tnmDigit = digit.getJSONArray(TNM_DIGIT);
            for (int i = 0; i < tnmDigit.length(); i++) {
                JSONObject tnmDigitItem = tnmDigit.getJSONObject(i);
                String cancerId = tnmDigitItem.getString(CANCER_ID);
                JSONArray data = tnmDigitItem.getJSONArray(DATA);
                TNMObj obj = null;
                for (int j = 0; j < data.length(); j++) {
                    obj = new TNMObj();
                    obj.setCancerId(cancerId);
                    JSONObject dataItem = data.getJSONObject(j);
                    String tnmId = dataItem.getString(TNM_ID);
                    String tid = dataItem.getString(T_ID);
                    String nid = dataItem.getString(N_ID);
                    String mid = dataItem.getString(M_ID);
                    String digitId = dataItem.getString(DIGIT_ID);
                    obj.setTid(tid);
                    obj.setNid(nid);
                    obj.setMid(mid);
                    obj.setDigitId(digitId);
                    GlobalData.tnmObjs.add(obj);
//                    ZYApplication.getDigit.put(cancerId + "," + tid + "," + nid + "," + mid, digitId);
                }
            }
            /**
             * 解析CancerTransferPos
             */
            JSONObject cancerTransferPos = object.getJSONObject("CancerTransferPos");
            JSONObject transferposdData = cancerTransferPos.getJSONObject(DATA);
            String cancer_TPosMTM = cancerTransferPos.getString(MAXTIMESTAMP);//circleIddData的时间戳
            Iterator<String> transferposdDataKey = transferposdData.keys();
            String transferposKey = "";
            while (transferposdDataKey.hasNext()) {
                transferposKey = transferposdDataKey.next();
                GlobalData.transferpos.put(transferposKey, transferposdData.getString(transferposKey));
            }

            /**
             * 解析突变情况 GeneticMutation
             */
            JSONObject genes = object.getJSONObject(GeneticMutation);
            JSONArray geneData = genes.getJSONArray(DATA);
            for (int i = 0; i < geneData.length(); i++) {
                JSONObject geneItem = geneData.getJSONObject(i);//得到data中每个item项目
                String cancerId = geneItem.getString(CANCER_ID);
                JSONObject gene2 = geneItem.getJSONObject(gene);
                List<String> list = new ArrayList<>();
                Iterator<String> iterator2 = gene2.keys();
                String key1 = "";
                while (iterator2.hasNext()) {
                    key1 = iterator2.next();
                    list.add(key1);
                    GlobalData.geneValues.put(key1, gene2.getString(key1));
                }
                GlobalData.gene.put(cancerId, list);
            }
            /**
             * 解析联合用药StepUnion
             */
            JSONObject stepunion = object.getJSONObject(StepUnion);
            String stepuinonmax = stepunion.getString(MAXTIMESTAMP);
            JSONArray arrayData = stepunion.getJSONArray(DATA);
            for (int i = 0, j = arrayData.length(); i < j; i++) {
                //存成Map<cancerid,List step> Map<stepunionid,cureconfid>  Map<stepunionid,values>
                JSONObject obj = arrayData.getJSONObject(i);
                String id = obj.getString("CancerID");
                JSONArray step = obj.getJSONArray(STEP);
//                List<String> tempList = new ArrayList<>();
                for (int k = 0, m = step.length(); k < m; k++) {//遍历list
                    JSONObject stepItem = step.getJSONObject(k);
                    String str_stepunionid = stepItem.getString(stepunionid);//联合用药的id
//                    tempList.add(str_stepunionid);
                    String str_cureconfid = stepItem.getString(cureconfid);
                    GlobalData.cureId.put(str_stepunionid, str_cureconfid);//得到cureid
                    String str_stepallname = stepItem.getString(stepallname);
                    GlobalData.cureValues.put(str_stepunionid, str_stepallname);
                    if (GlobalData.cancerCure.containsKey(id)) {//
                        // 这个癌种既有单药又有联药
                        //1.根据cureid 添加到cancercure中
                        //cancerCure.get(id).get(cureid).add(stepuid)
                        GlobalData.cancerCure.get(id).get(str_cureconfid).add(str_stepunionid);
                    } else {
                        //只有联药
                        //1。造《string，list》数据string 为联药的cureid list中 直接有一个联药的id
                        // 在。根据cancer put到cancercure中
                        List<String> lists1 = new ArrayList<>();
                        lists1.add(str_stepunionid);
                        LinkedHashMap<String, List<String>> tempMap = new LinkedHashMap<>();
                        tempMap.put(str_cureconfid, lists1);
                        GlobalData.cancerCure.put(id, tempMap);

                        //cancercure.put(id,new hashMap().put(cureid,new ArrayList().add(stepuinonid)
                    }
                }

//                GlobalData.stepUinon.put(id, tempList);
            }
            /**
             * 添加cancer为0的情况
             * <String, LinkedHashMap<String, List<String>>> cancerCure
             */
//          LinkedHashMap<String, List<String>> stepUinon <cancerid,stepuinid>;
            LinkedHashMap<String, List<String>> allCureData = GlobalData.cancerCure.get(ALL);//id为0的情况
            Set<String> leftSet = GlobalData.cancerCure.keySet();//拿出
            for (String str : leftSet) {//1.遍历cancerCure把cancerid 为0的情况添加到其他的癌肿中
                if (ALL.equals(str)) {
                    continue;
                }
                LinkedHashMap<String, List<String>> temp = GlobalData.cancerCure.get(str);//这个键是cureid 值是对应的药物
                addCure(allCureData, temp);//
//                /**
//                 * 要添加cancerid 为0的情况
//                 */
//                List<String> list = GlobalData.stepUinon.get(str);//得到联合用药的list 里面装的是联合用药的id
//                List<String> list1 = GlobalData.stepUinon.get(ALL);//cancerid为0的list
//                if (list == null || list.size() == 0) {
//                    continue;
//                }
//                if (list1 != null && list1.size() > 0) {//把2个list的数据组合起来
//                    list.addAll(list1);
//                }
//
//                for (String item : list) {
//                    String cureid = MapDataUtils.getAllCureconfID(item);
//                    if (temp.containsKey(cureid)) {
//                        temp.get(cureid).add(item);
//                    }
//                }
            }

            /**
             * 解析 circleiddata
             */
            JSONObject circleIddData = object.getJSONObject(CIRCLE_ID_DATA);
            JSONArray cityArray = circleIddData.getJSONArray(Citydata);
            JSONArray cancerArray = circleIddData.getJSONArray(Cancerdata);

            praCity(cityArray, true);
            praCity(cancerArray, false);

            /**
             * 解析otherstep
             */
            JSONObject otherStep = object.getJSONObject(OtherStep);
            JSONObject oSData = otherStep.getJSONObject(DATA);
            String oskey = "";
            Iterator<String> oskeyset = oSData.keys();
            while (oskeyset.hasNext()) {
                oskey = oskeyset.next();//左边的key
                //右边的values
                GlobalData.otherStep.put(oskey, oSData.getString(oskey));
            }


            callback.isFinish(true);
        } catch (JSONException e) {//
            e.printStackTrace();
            callback.isFailed();


        }
    }

    private static void praCity(JSONArray cityArray, boolean city) throws JSONException {

        for (int i = 0, j = cityArray.length(); i < j; i++) {
            JSONObject item = cityArray.getJSONObject(i);
            String circleID = item.getString(CircleID);
            String circleName = item.getString(CircleName);
            if (city) {
                GlobalData.circleValues.put(circleID, circleName);
            } else {
                GlobalData.circleValues.put(circleID, circleName);
            }


        }
    }

    /**
     * 把2个map合并 相同拥有的键就合并 没有相同的键 就添加
     *
     * @param allCureData 要合并的子map
     * @param temp        最后合并成的map
     */
    private static void addCure(LinkedHashMap<String, List<String>> allCureData, LinkedHashMap<String, List<String>> temp) {
        Set<String> set = allCureData.keySet();//1.遍历 all
        for (String keyset : set) {
            if (temp.containsKey(keyset)) {//2.如果temp中有何all的key一致的 就直接add
                temp.get(keyset).addAll(allCureData.get(keyset));
            } else { //没有就put
                temp.put(keyset, allCureData.get(keyset));
            }
        }
    }
}
