/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decision_trees;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author user
 */
public class Attribute {
    public String text;
    public Set<Value> possibleValues;
    
    public Attribute(String content){
        text = content;
        possibleValues = new HashSet<Value>();
    }
    
    public String getText(){
        return text;
    }
    
    public void addPossibleValue(Value val){
        this.possibleValues.add(val);
    }
    
    public Set<Value> getPossibleValues(){
        return this.possibleValues;
    }
}
