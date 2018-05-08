/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decision_trees;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author user
 */
public class Attribute {
    public String text;
    public Set<Value> possibleValues;
    
    /**
     * class constructor
     * @param content the attribute String
     */
    public Attribute(String content){
        text = content;
        possibleValues = new HashSet<Value>();
    }
    
    /**
     * 
     * @return the attribute String
     */
    
    public String getText(){
        return text;
    }
    
    /**
     * adds a possible value to the list of values
     * @param val a valid value for the attribute
     */
    
    public void addPossibleValue(Value val){
        this.possibleValues.add(val);
    }
    
    /**
     * gets the set of possible values on an attribute
     * @return 
     */
    public Set<Value> getPossibleValues(){
        return this.possibleValues;
    }

    /**
     * methods returns the attribute String
     * @return the name of the String
     */
    @Override
    public String toString() {
        return "<" + text + '>';
    }

    /**
     * returns the hashCode of the object
     * @return an int with the object hashcode
     */
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.text);
        return hash;
    }

    /**
     * compares this object with another
     * 
     * @param obj the object to compare with
     * @return true if this object instance and the compared one have the same text attribute,
     * false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Attribute other = (Attribute) obj;
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        return true;
    }
    
    

}
