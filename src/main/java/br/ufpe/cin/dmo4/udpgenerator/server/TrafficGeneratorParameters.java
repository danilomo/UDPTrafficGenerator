package br.ufpe.cin.dmo4.udpgenerator.server;

import br.ufpe.cin.dmo4.udpgenerator.distributions.RandomDistribution;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Danilo Oliveira
 */
public class TrafficGeneratorParameters {
    
    private RandomDistribution packageRate;
    private RandomDistribution packageSize;
    private double duration;
    
    
    private TrafficGeneratorParameters() {
    }

    public static TrafficGeneratorParameters parseJSON(String str) {

        TrafficGeneratorParameters config = new TrafficGeneratorParameters();

        try {
            JSONObject json = null;

            json = new JSONObject(str);

            Object rate = json.get("rate");
            config.packageRate = getDistribution(rate);

            Object pSize = json.get("pSize");
            config.packageSize = getDistribution(pSize);
            
            config.duration = json.getDouble("duration");

            return config;
        } catch (JSONException ex) {
            Logger.getLogger(TrafficGeneratorParameters.class.getName()).log(Level.SEVERE, null, ex);
            throw new InvalidParameterException("Bad JSON");
        }

    }

    public double getRandomThinkingTime() {
        return packageRate.random();
    }

    public double getRandomPackageSize() {
        return packageSize.random();
    }

    public long getDuration() {
        return (long) duration * 1000;
    }
        
    public RandomDistribution getPackageRate() {
        return packageRate;
    }

    public RandomDistribution getPackageSize() {
        return packageSize;
    }

    private static RandomDistribution getDistribution(Object val) throws JSONException {
        if (val instanceof Double || val instanceof Integer || val instanceof Long) {
            double d = Double.parseDouble(val.toString());

            return RandomDistribution.createRandomDistribution(d);
        } else if (val instanceof JSONObject) {
            JSONObject jon = (JSONObject) val;

            String type = jon.getString("type");

            Map<String, Double> parameters = jsonToMap(jon);

            return RandomDistribution.createRandomDistribution(type, parameters);
        }

        return null;
    }

    public static Map<String, Double> jsonToMap(JSONObject jObject) throws JSONException {

        HashMap<String, Double> map = new HashMap<>();
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (key.equals("type")) {
                continue;
            }
            Double value = jObject.getDouble(key);
            map.put(key, value);

        }

        return map;
    }

    public static void main(String[] args) {
        String str = "{"
                + "\"rate\": {\"type\": \"normal\", \"mean\": 100, \"sd\": 1}"
                + ", \"pSize\": {"
                + "\"type\": \"constant\""
                + ", \"value\": 100"
                + "}"
                + "}";

        TrafficGeneratorParameters conf = TrafficGeneratorParameters.parseJSON(str);

        System.out.println(conf.getRandomPackageSize());
        System.out.println(conf.getRandomThinkingTime());

    }

}
