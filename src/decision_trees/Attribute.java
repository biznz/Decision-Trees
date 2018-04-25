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

    @Override
    public String toString() {
        return "Attribute{" + "text=" + text + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.text);
        return hash;
    }

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
