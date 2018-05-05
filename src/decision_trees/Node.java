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
    private Value v;
    public int countDecision;
    
    public Node(Attribute attr){
        values = new ArrayList<Branch>();
        this.attr = attr;
        this.depth = 0;
        this.countDecision = 0;
    }
    
    public Node(){
        values = new ArrayList<Branch>();
        this.depth = 0;
        this.countDecision = 0;
    }
    
    public void setCountDecision(int v){
        this.countDecision = v;
    }
    
    public int getCountDecision(){
        return this.countDecision;
    }
    
    public Node(Value v){
        this.v = v;
        this.label = v.getContent();
    }
    public void SetDepth(int depth){
        this.depth = depth;
    }
    
    public int getDepth(){
        return this.depth;
    }
    
    public Value getValue(){
        return this.v;
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
    
    public void setValue(Value v){
        this.v = v;
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

    public int getCount(){
        return this.countDecision;
    }
    
    @Override
    public String toString() {
        if(attr!=null){
            return attr.toString();
        }
        return this.v.getContent();
        //return attr.text+":";
        //return "Node{" + "attr=" + attr + ", label=" + label + '}';
    }

//    @Override
//    public String toString() {
//        return "Node{" + "attr=" + attr + ", depth=" + depth + ", values=" + values + ", label=" + label + '}';
//    }
    
    
    
}
