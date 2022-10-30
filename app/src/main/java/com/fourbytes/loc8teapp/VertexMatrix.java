package com.fourbytes.loc8teapp;

import android.content.Context;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class VertexMatrix {
    private HashMap<String, VertexData> Vertex = new HashMap<>();
    private ArrayList<Edge> Edges = new ArrayList<>();
    private Context context;
    public VertexMatrix(Context context) throws IOException, JSONException {
        this.context = context;
        initVertex();
        initEdges();

    }

    public void initVertex() throws JSONException, IOException {
        JSONObject vertex = new JSONObject(retrieveJSON(context.getResources().openRawResource(R.raw.vertex)));

        Iterator<String> keys = vertex.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            JSONObject vertex_object = vertex.getJSONObject(key);
            JSONObject path_object = vertex_object.getJSONObject("path");

            Iterator<String> path_keys = path_object.keys();


            HashMap<String, PathVertex> path_map = new HashMap<>();

            while(path_keys.hasNext()){
                ArrayList<String> path = new ArrayList<>();
                String path_key = path_keys.next();
                JSONObject path_data = path_object.getJSONObject(path_key);
                JSONArray path_array = path_data.getJSONArray("path");

                String distance = String.valueOf(path_data.get("distance"));

                for(int i = 0; i < path_array.length(); i++){
                    path.add(String.valueOf(path_array.get(i)));
                }
                path_map.put(path_key, new PathVertex(
                        Double.parseDouble(distance),
                        path
                ));
            }

            String latitude = String.valueOf(vertex_object.get("latitude"));
            String longitude = String.valueOf(vertex_object.get("longitude"));
            Vertex.put(key, new VertexData(
                    Double.parseDouble(latitude),
                    Double.parseDouble(longitude),
                    path_map
            ));


        }

        //TODO: Delete Later
//        VertexData test = (VertexData) vertex.get("vertex66");
//
//        HashMap<String, PathVertex> test_path = test.getPath();
//
//        if(test_path.get("vertex1") == null){
//            System.out.println("------------------------not found");
//        }else{
//            ArrayList<String> test_array = test_path.get("vertex1").getPath();
//            for(int i = 0; i < test_array.size(); i++){
//                System.out.println(test_array.get(i));
//            }
//        }
    }

    public void initEdges() throws JSONException, IOException {
        JSONObject edge = new JSONObject(retrieveJSON(context.getResources().openRawResource(R.raw.edges)));

        Iterator<String> keys = edge.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            if (edge.get(key) instanceof JSONObject) {
                JSONObject edge_object = edge.getJSONObject(key);

                String originId = String.valueOf(edge_object.get("origin"));
                String destinationId = String.valueOf(edge_object.get("destination"));
                String distance = String.valueOf(edge_object.get("distance"));

                Edges.add(new Edge(
                        originId,
                        destinationId,
                        Double.parseDouble(distance),
                        key
                ));
            }
        }
    }

    public List<VertexInfo> getDistanceLatLong(double originLat, double originLong){

        ArrayList<VertexInfo> distanceLatLong = new ArrayList<>();

        for (String key : Vertex.keySet()) {
            double desLat = Vertex.get(key).getLatitude();
            double desLong = Vertex.get(key).getLongitude();

            double distance = calculateDistance(originLat, desLat, originLong, desLong);

            distanceLatLong.add(new VertexInfo(
                    key,
                    desLong,
                    desLat,
                    distance
            ));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            distanceLatLong.sort((o1, o2) -> Double.compare(o1.getDistance(), o2.getDistance()));
        }

        return distanceLatLong.subList(0, 25);

    }

    public double calculateDistance(double lat1, double lat2, double lon1, double lon2){

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return (c * r);
    }


    public String retrieveJSON(InputStream resource) throws IOException {
        InputStream is = resource;
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }

        String jsonString = writer.toString();

        return jsonString;
    }

    public HashMap<String, VertexData> getVertex() {
        return Vertex;
    }

    public void setVertex(HashMap<String, VertexData> vertex) {
        this.Vertex = vertex;
    }

    public ArrayList<Edge> getEdges() {
        return Edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        Edges = edges;
    }
}
