/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decision_trees;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author user
 */
public class Decision_Trees {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            BufferedReader in = new BufferedReader(new FileReader("foo.in")); //restaurant.csv,weather.csv,
            String[] Attributes = in.readLine().split(",");
            ArrayList<Attribute> attributes = new ArrayList<Attribute>();
            for(String s:Attributes){
                attributes.add(new Attribute(s));
            }
            Set<Example> examples = new HashSet<Example>();
            while(in.readLine()!=null){
                String[] line = in.readLine().split(",");
            }
        }
        catch(IOException FnF){
            
        }
    }
    
    /*
        Decision Tree represents a function that takes as input 
        a vector of attribute values and returns a "decision" - a single output value.
    */
    
    /*
            SUDO CODE
    
            ID3(Examples, Target_Attribute, Attributes)
            Create a root node for the tree
            If all examples are positive,
            Return the single-node tree Root, with label = +.
            If all examples are negative,
            Return the single-node tree Root, with label = -.
            If number of predicting attributes is empty,
            Return the single node tree Root,
            with label = most common value of the
            target attribute in the examples.
            Else
            A = Attribute that best classifies examples
            Decision Tree attribute for Root = A
            For each possible value, vi, of A,
            Add a new tree branch below Root,
            corresponding to the test A = vi.
            Let Examples(vi) be the subset of examples that
            have the value vi for A
            If Examples(vi) is empty
            below this new branch add a leaf node with
            label = most common target value in the examples
            Else
            below this new branch add the subtree
            ID3 (Examples(vi), Target_Attribute, Attributes - {A})
            EndIf
            EndFor
            EndIf
            Return Root
    */
    
    protected static Node ID3(Set<Example> examples,Set<Attribute> attributes,Attribute target_attribute){
        //Create a root node for the tree
        Node root = new Node(null);
        
        /*If all examples are positive,
            Return the single-node tree Root, with label = +. */
        if(examplesArePositive(examples,target_attribute))
            { root.setLabel("+");return root;}
        
        /*If all examples are negative,
            Return the single-node tree Root, with label = -. */
        if(examplesAreNegative(examples,target_attribute))
            { root.setLabel("-");return root;}
        
        /*If number of predicting attributes is empty, Return the single node tree Root, 
            with label = most common value of the
            target attribute in the examples. */
        
        if(attributes.isEmpty())
            {root.setLabel("most common value of target attribute in the examples");return root;}
        
        else{
            // A = Attribute that best classifies examples
            Attribute A = IMPORTANCE(attributes,examples);
            // Decision Tree attribute for Root = A
            root.setAttribute(A);
            for(Value v: A.getPossibleValues()){
                Branch newBranch = new Branch(v);
                root.addBranch(newBranch);
                Set<Example> subSet_Examples = subSetHavingAttribute(examples,A,v);
                if(subSet_Examples.isEmpty()){
                    //newBranch.
                }
            }
        }
        return root;
    }
    
    
    public static Set<Example> subSetHavingAttribute(Set<Example> examples,Attribute attr,Value v){
        Set<Example> subSet = new HashSet<Example>();
        for(Example sample: examples){
            if(sample.getValue(attr).equals(v)){
                subSet.add(sample);
            }
        }
        return subSet;
    }
    
    
    
    public static boolean examplesArePositive(Set<Example> examples,Attribute target_attribute){
        for(Example ex: examples){
            if(ex.getValue(target_attribute).getContent().equals("no")){
                return false;
            }
        }
        return true;
    }
    
    public static boolean examplesAreNegative(Set<Example> examples,Attribute target_attribute){
        for(Example ex: examples){
            if(ex.getValue(target_attribute).getContent().equals("yes")){
                return false;
            }
        }
        return true;
    }
    
    public static Attribute IMPORTANCE(Set<Attribute> attributes, Set<Example> examples){
        Attribute maximizer = null;
        return maximizer;
    }
    
    protected static void entropy(Attribute attr){
        
    }
}