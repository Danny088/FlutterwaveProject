/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.flutterwaveassignment;

/**
 *
 * @author Daniel
 */
import org.json.JSONArray;
import org.json.JSONObject;
/*
    
    I am currently running a tutoria on java programming languate that was why I did this the assignment with java
    I have not gotten to the part of writing a webservice, because of time, i did not do the webservice part
    I used knowledged based on what I have learned so far to answer the question 
    The paylaod is hardcoded, but I expect it to be sent to me via a webservice
    I am printing out the response on the console but once I have learnt webservice part of java, I should be able to return the response through webservice
  
    
     */
public class Flutterwaveassignment {

    public static void main(String[] args) {
        String request = "{\n"
                + "    \"ID\": 1308,\n"
                + "    \"Amount\": 12580,\n"
                + "    \"Currency\": \"NGN\",\n"
                + "    \"CustomerEmail\": \"anon8@customers.io\",\n"
                + "    \"SplitInfo\": [\n"
                + "        {\n"
                + "            \"SplitType\": \"FLAT\",\n"
                + "            \"SplitValue\": 45,\n"
                + "            \"SplitEntityId\": \"LNPYACC0019\"\n"
                + "        },\n"
                + "        {\n"
                + "            \"SplitType\": \"RATIO\",\n"
                + "            \"SplitValue\": 3,\n"
                + "            \"SplitEntityId\": \"LNPYACC0011\"\n"
                + "        },\n"
                + "        {\n"
                + "            \"SplitType\": \"PERCENTAGE\",\n"
                + "            \"SplitValue\": 3,\n"
                + "            \"SplitEntityId\": \"LNPYACC0015\"\n"
                + "        }\n"
                + "    ]\n"
                + "}";
        JSONObject parseReq = new JSONObject(request);
        double initAmount = parseReq.optDouble("Amount");
        JSONArray slitps = parseReq.optJSONArray("SplitInfo");
        JSONObject response = new JSONObject();
        JSONArray resArray = new JSONArray();

        // calculate FLAT split 
        for (int t = 0; t < slitps.length(); t++) {
            JSONObject item = slitps.getJSONObject(t);
            String SplitEntityId = item.optString("SplitEntityId");
            String SplitType = item.optString("SplitType");
            int SplitValue = item.optInt("SplitValue");
            JSONObject resItem = new JSONObject();
            if (SplitType.equals("FLAT")) {
                double resAmount = SplitValue;
                resItem.put("SplitEntityId", SplitEntityId);
                resItem.put("Amount", resAmount);
                initAmount = initAmount - resAmount;
                resArray.put(resItem);
            }
        }

        // calculate PERCENTAGE split 
        for (int t = 0; t < slitps.length(); t++) {
            JSONObject item = slitps.getJSONObject(t);
            String SplitEntityId = item.optString("SplitEntityId");
            String SplitType = item.optString("SplitType");
            double SplitValue = item.optInt("SplitValue");
            JSONObject resItem = new JSONObject();
            if (SplitType.equals("PERCENTAGE")) {
                double resAmount = (SplitValue / 100) * initAmount;
                resItem.put("SplitEntityId", SplitEntityId);
                resItem.put("Amount", resAmount);
                initAmount = initAmount - resAmount;
                resArray.put(resItem);
            }

        }

        // get the number of ratio in the payload
        double totalRatio = 0;
        double ratioAmt = initAmount;// save the ratio amount
        for (int t = 0; t < slitps.length(); t++) {
            JSONObject item = slitps.getJSONObject(t);
            String SplitType = item.optString("SplitType");
            int SplitValue = item.optInt("SplitValue");
            if (SplitType.equals("RATIO")) {
                totalRatio = totalRatio + SplitValue;
            }

        }

        // calculate RATIO split 
        for (int t = 0; t < slitps.length(); t++) {
            JSONObject item = slitps.getJSONObject(t);
            String SplitEntityId = item.optString("SplitEntityId");
            String SplitType = item.optString("SplitType");
            double SplitValue = item.optInt("SplitValue");
            JSONObject resItem = new JSONObject();
            if (SplitType.equals("RATIO")) {
                double resAmount = (SplitValue / totalRatio) * ratioAmt;
                resItem.put("SplitEntityId", SplitEntityId);
                resItem.put("Amount", resAmount);
                initAmount = initAmount - resAmount;
                resArray.put(resItem);
            }

        }

        response.put("ID", parseReq.optInt("ID"));
        response.put("Balance", initAmount);
        response.put("SplitBreakdown", resArray);

        System.out.println(response.toString());// Display the result  
    }
}
