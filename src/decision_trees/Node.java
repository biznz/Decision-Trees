/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decision_trees;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Node {
    private Attribute attr;
    private int depth;
    private ArrayList<Branch> values;
    private String label;

    public Node(Attribute attr){
        values = new ArrayList<Branch>();
        this.attr = attr;
        this.depth = -1;
    }
    
    public Node(){
        values = new ArrayList<Branch>();
        this.depth = -1;
    }
    
    public void SetDepth(int depth){
        this.depth = depth;
    }
    
    public int GetDepth(){
        return this.depth;
    }
    
    public void addBranch(Branch branch){
        values.add(branch);
    }

    public ArrayList<Branch> getValues() {
        return values;
    }
    
    public void setLabel(String l){
        this.label = l;
    }
    
    public String getLabel(){
        return this.label;
    }
    
    public void setAttribute(Attribute attr){
        this.attr = attr;
    }
    
    public Attribute getAttribute(){
        return this.attr;
    }

    
    @Override
    public String toString() {
        if(attr!=null){
            return attr.toString();
        }
        return this.getLabel();
        //return attr.text+":";
        //return "Node{" + "attr=" + attr + ", label=" + label + '}';
    }

//    @Override
//    public String toString() {
//        return "Node{" + "attr=" + attr + ", depth=" + depth + ", values=" + values + ", label=" + label + '}';
//    }
    
    
    
}
