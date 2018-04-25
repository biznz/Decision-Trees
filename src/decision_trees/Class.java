/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decision_trees;

/**
 *
 * @author b1z
 */
public class Class {
    private Value value;
    private int counter;
    
    public Class(String val){
        this.value = new Value(val);
        this.counter=0;
    }
    
    public Class(){
        this.counter = 0;
    }
    
    public void setValue(Value v){
        this.value = v;
    }
    
    public void setCounter(int val){
        this.counter = val;
    }
    
    public void incrementCounter(){
        this.counter+=1;
    }

    public Value getValue() {
        return value;
    }

    public int getCounter() {
        return counter;
    }
    
    
}
