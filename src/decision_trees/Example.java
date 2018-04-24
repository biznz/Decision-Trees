/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decision_trees;

import java.util.HashMap;

/**
 *
 * @author user
 */
public class Example {
    HashMap<Attribute,Value> content;
    
    public Example(){
        content = new HashMap<Attribute,Value>();
    }
    
    public void add(Attribute attr,Value val){
        content.put(attr, val);
    }
    
    public Value getValue(Attribute attr){
        return content.get(attr);
    }

    @Override
    public String toString() {
        String content = "";
        for(Attribute attr:this.content.keySet()){
            content+=" "+this.content.get(attr).getContent();
        }
        return "Example{" + "content=" + content + '}';
    }
    
    
}
