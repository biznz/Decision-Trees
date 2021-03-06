/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decision_trees;

import java.util.Objects;

/**
 *
 * @author user
 */
public class Value implements Comparable{
    private String content;
    private Class classifier;
    
    public Value(String a){
        this.content = a;
    }
    
    public String getContent(){
        return this.content;
    }

    public void setClassifier(Class classifier) {
        this.classifier = classifier;
    }

    public Class getClassifier() {
        return classifier;
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.content);
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
        final Value other = (Value) obj;
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        Value v = (Value)o;
        float value;
        try{
            value = Float.parseFloat(v.getContent());
            float current=Float.parseFloat(this.getContent());
            if(current>value){
                return 1;
            }
            if(value==current){
                return 1;
            }
            if(current<value){
                return -1;
            }
        }
        catch(Exception ex){
            return 0;
        }
        return -1;
    }
    
    
}
