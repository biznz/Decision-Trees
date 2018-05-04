package decision_trees;


import decision_trees.Value;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
public class Branch {
    public Value val;
    public Node leaf;
    public int depth;
    public int countDecision;
    
    public Branch(Value val){
        this.val = val;
        this.depth = 0;
        this.countDecision = 0;
    }

    public void setCount(int v){
        this.countDecision = v;
    }
    
    public int getCount(){
        return this.countDecision;
    }
    
    public Value getVal() {
        return val;
    }
    
    public void addLeaf(Node node){
        this.leaf = node;
    }
    
    public Node getLeaf(){
        return this.leaf;
    }
    
    public void setDepth(int depth){
        this.depth = depth;
    }
    
    public int getDepth(){
        return this.depth;
    }

    @Override
    public String toString() {
        return "Branch{" + "val=" + val + ", leaf=" + leaf + ", depth=" + depth + '}';
    }
    
    
    
    
}
