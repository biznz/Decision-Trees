/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decision_trees;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    
    public static Attribute goal = null;
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            BufferedReader in = new BufferedReader(new FileReader(new File(args[0]))); //restaurant.csv,weather.csv,
            String[] Attributes = in.readLine().split(",");
            ArrayList<Attribute> attributes = new ArrayList<Attribute>();
            for(String s:Attributes){
                attributes.add(new Attribute(s));
            }
            goal = attributes.get(attributes.size()-1);
            HashSet<Example> examples = new HashSet<Example>();
            String l;
            while(( l = in.readLine())!=null){
                String[] line = l.split(",");
                int counter = 0;
                Example example = new Example();
                for(String a:line){
                    Value v = new Value(line[counter]);
                    if(!attributes.get(counter).possibleValues.contains(v)){
                        attributes.get(counter).possibleValues.add(v);
                    }
                    example.add(attributes.get(counter), v);
                    counter++;
                }
                examples.add(example);
                System.out.println(example);
            }
            Node Tree_root = ID3(examples,null,attributes);
        }
        catch(IOException FnF){
            FnF.printStackTrace();
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
    
    protected static Node ID3(HashSet<Example> examples,Attribute target_attribute,ArrayList<Attribute> attributes){
        //Create a root node for the tree
        Node root = new Node(null);
        
        /*
            If all examples have the same classifier,
            return the single-node tree Root, with label classifier;
        */
        if(examplesHaveSameClassifier(examples,goal))
            {   Value v = ((Example)examples.toArray()[0]).getValue(target_attribute); 
                root.setLabel(v.getContent());return root;}
        
        /*If all examples are positive,
            Return the single-node tree Root, with label = +. */
//        if(examplesArePositive(examples,target_attribute))
//            {   Value v = ((Example)examples.toArray()[0]).getValue(target_attribute);
//                root.setLabel(v.getContent());return root;}
//        
//        /*If all examples are negative,
//            Return the single-node tree Root, with label = -. */
//        if(examplesAreNegative(examples,target_attribute))
//            {   Value v = ((Example)examples.toArray()[0]).getValue(target_attribute); 
//                root.setLabel(v.getContent());return root;}
        
        /*If number of predicting attributes is empty, Return the single node tree Root, 
            with label = most common value of the
            target attribute in the examples. */
        
        if(attributes.isEmpty())
            { 
                Value v = mostCommonValueOnTargetAttribute(examples,target_attribute);
                root.setAttribute(target_attribute);
                root.setLabel(v.getContent());
                //
                //root.setLabel(l);
//                root.setLabel("most common value of target attribute in the examples");return root;
            }
        
        else{
            // A = Attribute that best classifies examples
            Attribute A = IMPORTANCE(attributes,examples);
            // Decision Tree attribute for Root = A
            root.setAttribute(A);
            //For each possible value, vi, of A,
            for(Value v: A.getPossibleValues()){
                //Add a new tree branch below Root,
                //corresponding to the test A = vi.
                Branch newBranch = new Branch(v);
                root.addBranch(newBranch);
                //Let Examples(vi) be the subset of examples that
                // have the value vi for A
                HashSet<Example> subSet_Examples = subSetHavingAttribute(examples,A,v);
                //If Examples(vi) is empty
                if(subSet_Examples.isEmpty()){
                    //below this new branch add a leaf node
                    Node leafNode = new Node();
                    //newBranch.addLeaf(leafNode);
                    //label = most common target value in the examples
                    String label = "most common target value in the examples";
                    leafNode.setLabel(label);
                    newBranch.addLeaf(leafNode);
                }
                else{
                    //below this new branch add the subtree
                    //using removing selected attribute
                    ArrayList<Attribute> newAttributes = Attributes_removedA(attributes,A);
                    Node subTree = ID3(subSet_Examples, root.getAttribute(), newAttributes);
                    newBranch.addLeaf(subTree);
                }
            }
        }
        return root;
    }
    
    /*
        counts occurrence of each value for an attribute in the data set
    */
    public static HashMap<Value,Integer> valuesCount(HashSet<Example> examples, Attribute target_attribute){
        HashMap<Value,Integer> valueCounter = new HashMap<Value,Integer>();
        for(Value v:target_attribute.getPossibleValues()){
            int counter = 0;
            for(Example ex:examples){
                if(valueCounter.containsKey(ex.content.get(target_attribute))){
                    counter+=1;
                    valueCounter.replace(v, counter);
                }
                else{
                    valueCounter.put(v, new Integer(1));
                }
            }
        }
        return valueCounter;
    }
    
    /*
        function returns the most common value for target_attribute from the dataset
    */
    public static Value mostCommonValueOnTargetAttribute(HashSet<Example> examples, Attribute target_attribute){
        HashMap<Value,Integer> valueCounter = valuesCount(examples,target_attribute);
        Value result = null;
        int ocurrence=0;
        for(Value v:valueCounter.keySet()){
            if(valueCounter.get(v)>ocurrence){
                result = v;
            }
        }
        return result;
    }
    
    /*
        Removes attribute A from set of attributes
    */
    
    public static ArrayList<Attribute> Attributes_removedA(ArrayList<Attribute> attributes,Attribute toRemove){
        ArrayList<Attribute> att = new ArrayList<Attribute>();
        for(Attribute attr:attributes){
            att.add(attr);
        }
        att.remove(toRemove);
        return att;
    }
    
    /*
        returns a subset of examples that have Value v on Attribute attr
    */
    
    public static HashSet<Example> subSetHavingAttribute(Set<Example> examples,Attribute attr,Value v){
        HashSet<Example> subSet = new HashSet<Example>();
        for(Example sample: examples){
            if(sample.getValue(attr).equals(v)){
                subSet.add(sample);
            }
        }
        return subSet;
    }
    
    /*
        checks if all examples yield the same classification
    */
    
    public static boolean examplesHaveSameClassifier(Set<Example> examples,Attribute target_attribute){
        String classification = null;
        for(Example ex: examples){
            if(classification == null){
               classification = ex.getValue(target_attribute).getContent();
            }
            else if(!ex.getValue(target_attribute).getContent().equals(classification)){
                return false;
            }
        }
        return true;
    }
    
    public static boolean examplesArePositive(Set<Example> examples,Attribute target_attribute){
        String classification = null;
        for(Example ex: examples){
            if(classification == null){
               classification = ex.getValue(target_attribute).getContent();
            }
            else if(ex.getValue(target_attribute).getContent().equals(classification)){
                return false;
            }
        }
        return true;
    }
    
    /*
       
    */
    
    public static boolean examplesAreNegative(Set<Example> examples,Attribute target_attribute){
        String classification = null;
        for(Example ex: examples){
            if(classification == null){
               classification = ex.getValue(target_attribute).getContent();
            }
            else if(ex.getValue(target_attribute).getContent().equals(classification)){
                return false;
            }
        }
        return true;
    }
    
    /*
        
    */
    
    public static Attribute IMPORTANCE(ArrayList<Attribute> attributes, HashSet<Example> examples){
        Attribute maximizer = null;
        double entropy_value = 0;
        for(Attribute attr:attributes){
            double attribute_entropy = ENTROPY(attr,examples);
            System.out.println(attribute_entropy);
        }
        return maximizer;
    }
    
    /*
        returns an 
            Entropy (in information theory) a logarithmic measure of the rate of transfer of information in a particular message or language.
            value on given attribute
    */
    
    public static double ENTROPY(Attribute attr,HashSet<Example> examples){
        int total_examples = examples.size();
        // ocurrence of value in examples
        HashMap<Value,Integer> valueCounter = valuesCount(examples,attr);
        HashMap<Value,HashMap<Value,Integer>> goalCounter = new HashMap<Value,HashMap<Value,Integer>>();
        for(Value v:valueCounter.keySet()){
            HashMap<Value,Integer> possibleValues = new HashMap<Value,Integer>();
            for(Value possible:goal.possibleValues){
                possibleValues.put(possible, 0);
            }
            goalCounter.put(v, possibleValues);
        }
        for(Example ex:examples){
            Value s = ex.content.get(attr);
            Value goalVal = ex.content.get(goal);
            goalCounter.get(s).replace(goalVal, goalCounter.get(s).get(goalVal)+1);
        }
        double result=0;
        System.out.println(valueCounter);
        for(Value v:valueCounter.keySet()){
            double div = valueCounter.get(v)/total_examples;
            //System.out.println(valueCounter.get(v)+"/"+total_examples);
            double total=0;
            for(Value possibleGoal:goalCounter.get(v).keySet()){
                double count = goalCounter.get(v).get(possibleGoal);
                //System.out.println("count :"+count);
                total = -1*(count/total_examples)*log2((count/total_examples));
            }
            div = div*total;
            result+=div;
        }
        return result;
    }
    
    public static double log2(double num){
        return Math.log(num)/Math.log(2);
    }
}
