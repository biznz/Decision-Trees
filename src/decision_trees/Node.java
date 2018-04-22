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
    private ArrayList<Branch> values;
    private String label;

    public Node(Attribute attr){
        values = new ArrayList<Branch>();
        this.attr = attr;
    }
    
    public Node(){
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
    
}
