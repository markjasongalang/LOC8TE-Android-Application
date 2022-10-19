package com.fourbytes.loc8teapp;

import android.util.Log;

import java.util.ArrayList;

public class DistanceMatrix {

    private ArrayList<VertexInfo> vInfo = new ArrayList<>();
    private ArrayList<Vertex> V = new ArrayList<>();
    public  DistanceMatrix(ArrayList<VertexInfo> vInfo){
        this.vInfo =  vInfo;
        initVertexMatrix();
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
            System.out.println(V.get(i).getId() + "--------");
            for(int j = 0; j < V.get(i).getEdge().size(); j++){
                if(V.get(i).getEdge().get(j).getDistance() > 0){
                    System.out.println(V.get(i).getEdge().get(j).getDistance());
                }

            }
        }
        return;
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

    public ArrayList<Vertex> getMatrix(){

        return V;
    }
}
