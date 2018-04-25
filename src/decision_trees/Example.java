/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decision_trees;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author user
 */
public class Example {
    HashMap<Attribute,Value> content;
    String id;
    
    public Example(){
        content = new HashMap<Attribute,Value>();
    }
    
    public void add(Attribute attr,Value val){
        content.put(attr, val);
    }
    
    public Value getValue(Attribute attr){
        return content.get(attr);
    }

    public HashMap<Attribute, Value> getContent() {
        return content;
    }

    public void setContent(HashMap<Attribute, Value> content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public Set<Attribute> getKeys(){
        return this.content.keySet();
    }

    @Override
    public String toString() {
        String content = "";
        for(Attribute attr:this.content.keySet()){
            content+=" "+attr+" : "+this.content.get(attr).getContent();
        }
        return "Example{id:" + id + "content=" + content + '}';
    }
    
    
}
