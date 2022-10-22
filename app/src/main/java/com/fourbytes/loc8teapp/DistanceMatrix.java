package com.fourbytes.loc8teapp;

import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class DistanceMatrix {

    private ArrayList<VertexInfo> vInfo = new ArrayList<>();
    private ArrayList<Vertex> V = new ArrayList<>();
    private ArrayList<Vertex> userDistance = new ArrayList<>();

    private final String TAG = "Matrix";

    public  DistanceMatrix(ArrayList<VertexInfo> vInfo){
        this.vInfo =  vInfo;
        initVertexMatrix();
    }

    public void getDistanceLongLat(ArrayList<VertexInfo> U){
        userDistance.clear();
        ArrayList<VertexInfo> vInfoCopy;
        ArrayList<VertexInfo> result;
        System.out.println(U.size());
        for(int i = 0; i < U.size(); i++){
            result = new ArrayList<>();
            vInfoCopy = new ArrayList<>();
            for(VertexInfo vertexClone : vInfo) {
                vInfoCopy.add(vertexClone.clone(vertexClone.getId(), vertexClone.getLongitude(), vertexClone.getLatitude()));
            }

            String originId = U.get(i).getId();
            double originLat = U.get(i).getLatitude();
            double originLong = U.get(i).getLongitude();

            for(int j = 0; j < vInfoCopy.size(); j++){
                double destinationLat = vInfoCopy.get(j).getLatitude();
                double destinationLong = vInfoCopy.get(j).getLongitude();

                double distanceLatLong = calculateLongLat(originLat, destinationLat, originLong, destinationLong);

                vInfoCopy.get(j).setDistance(distanceLatLong);
            }

            //sort list
            //get 4 nodes to return

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                vInfoCopy.sort((o1, o2) -> Double.compare(o1.getDistance(), o2.getDistance()));
            }

            for (int k = 0; k < 5; k++){
                result.add(vInfoCopy.get(k));
            }

            userDistance.add(new Vertex(
                    originId,
                    originLong,
                    originLat,
                    result
            ));
        }
    }

    public void setEdgeValue(String edgeID, String origin, String destination, double distance){

        for(int i = 0; i < V.size(); i++){
            String id = V.get(i).getId();
            try{
                if(origin.equals(id)){
                    for(int j = 0; j < V.get(i).getEdge().size(); j++){
                        if(destination.equals(V.get(i).getEdge().get(j).getId())){
                            V.get(i).getEdge().get(j).setDistance(distance);
                            return;
                        }
                    }
                }
            }catch (Exception e){
                Log.d("ERROR", origin);
            }

        }

        return;
    }

    public void setUserEdgeValue(String origin, String destination, double distance){
        ArrayList<VertexInfo> edge;
        for(int i = 0; i < userDistance.size(); i ++){
            if(userDistance.get(i).getId().equals(destination)){
                edge = new ArrayList<>();
                for(VertexInfo vertexClone : userDistance.get(i).getEdge()) {
                    edge.add(vertexClone.clone(vertexClone.getId(), vertexClone.getLongitude(), vertexClone.getLatitude()));
                }

                for(int j = 0; j < userDistance.get(i).getEdge().size(); j++){
                    if (userDistance.get(i).getEdge().get(j).getId().equals(origin)){
                        userDistance.get(i).getEdge().get(j).setDistance(distance);
                    }
                }

            }
        }
    }

    public boolean checkEdge(String checkId, String findId){

        for(int i = 0; i < V.size(); i++){

            if(V.get(i).getId().equals(checkId)){

            }
        }

        return false;
    }

    public void printUserDistance(){
        for(int i = 0; i < userDistance.size(); i++){

            Log.d("DISTANCE", userDistance.get(i).getId());

            for(int j = 0; j < userDistance.get(i).getEdge().size(); j++){

                Log.d("DISTANCE", userDistance.get(i).getEdge().get(j).getId());
                Log.d("DISTANCE", String.valueOf(userDistance.get(i).getEdge().get(j).getDistance()));
            }
        }
    }

    public double calculateLongLat(double lat1, double lat2, double lon1, double lon2){

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
        return (c * r) * 1000;
    }

    public void  initVertexMatrix(){
        ArrayList<VertexInfo> vInfoCopy;


        //init 1d as the source
        for(int i = 0; i < this.vInfo.size(); i++){
            vInfoCopy = new ArrayList<>();
            for(VertexInfo vertexClone : vInfo) {
                vInfoCopy.add(vertexClone.clone(vertexClone.getId(), vertexClone.getLongitude(), vertexClone.getLatitude() ));
            }

            String id = this.vInfo.get(i).getId();
            double longitude = this.vInfo.get(i).getLongitude();
            double latitude = this.vInfo.get(i).getLatitude();
            V.add(new Vertex(id, longitude, latitude, vInfoCopy));
        }

        Log.d(TAG,  "Vertex Init Done");
        return;

    }

    public void initEdgeValue(ArrayList<Edge> E){
        for(int i = 0; i < E.size(); i++){
            String id = E.get(i).getEdgeId();
            String origin = E.get(i).getOriginId();
            String destination = E.get(i).getDestinationId();
            double distance = E.get(i).getDistance();
            setEdgeValue(id, origin, destination, distance);
        }

        for(int i = 0; i < V.size(); i++){
            for(int j = 0; j < V.get(i).getEdge().size(); j++){
                if(V.get(i).getEdge().get(j).getDistance() > 0){
                }
            }
        }

        Log.d(TAG,  "Edge Init Done");
        return;
    }

    public void initUsers(ArrayList<VertexInfo> U){
        ArrayList<VertexInfo> vInfoCopy;

        for(int i = 0; i < U.size(); i++){
            vInfoCopy = new ArrayList<>();
            for(VertexInfo vertexClone : vInfo) {
                vInfoCopy.add(vertexClone.clone(vertexClone.getId(), vertexClone.getLongitude(), vertexClone.getLatitude()));
            }

            String id = U.get(i).getId();
            double longitude = U.get(i).getLongitude();
            double latitude = U.get(i).getLatitude();
            V.add(new Vertex(id, longitude, latitude, vInfoCopy));
        }
        Log.d(TAG,  "User Init Done");
        return;
    }

    public ArrayList<Vertex> getMatrix(){
        return V;
    }

    public ArrayList<Vertex> getUserDistance() {
        return userDistance;
    }
    //use memoization
    public void getStortestPath(){}
}
