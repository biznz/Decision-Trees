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
    
    public Branch(Value val){
        this.val = val;
    }

    public Value getVal() {
        return val;
    }
    
    
}
