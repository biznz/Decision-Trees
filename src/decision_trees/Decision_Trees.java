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
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author user
 */


public class Decision_Trees {

    /**
     * @param args the command line arguments
     */
    
    private static Attribute goal = null;
    private static int treeDepth = 0;
    private static int level = 0;
    private static HashSet<Example> examples;
    private static ArrayList<Attribute> attributes;
    
    public static void main(String[] args) {
        // TODO code application logic here
        // reads the file with a file reader into a buffer
        
        Scanner in = new Scanner(System.in);
        String line;
        System.out.print("Give a training file path: ");
        while((line = in.nextLine()).equals("")){
            System.out.println("Training data file path: ");
        }
        System.out.println(line);
        
        readTrainingData(args[0]);
        Node Tree_root = ID3(examples,goal,attributes);
        //GENERAL_SEARCH(Tree_root,new Fifo());
        TreeTraversal(Tree_root);
        
        
    }
    
    private static void readTrainingData(String filePath) throws IOException{
            BufferedReader in = new BufferedReader(new FileReader(new File(filePath))); //restaurant.csv,weather.csv,
            //first line from the file contains the attribute description
            String[] Attributes = in.readLine().split(",");
            //arraylist containing the attributes
            attributes = new ArrayList<Attribute>();
            for(int i=1;i<Attributes.length;i++){
                attributes.add(new Attribute(Attributes[i]));
            }
            // goal attribute records the classifier
            goal = attributes.get(attributes.size()-1);
            // the set of examples on the file 
            examples = new HashSet<Example>();
            String l;
            while(( l = in.readLine())!=null){
                String[] line = l.split(",");
                int counter = 0;
                Example example = new Example();
                example.setId(line[0]);
                for(int i=1;i<line.length;i++){
                    Value v = new Value(line[i]);
                    //System.out.println(attributes.get(i-1));
//                    System.out.println(v);
                    if(!attributes.get(i-1).possibleValues.contains(v)){
                        attributes.get(i-1).possibleValues.add(v);
                    }
                    example.add(attributes.get(i-1), v);
                }
                //System.out.println(example);
                examples.add(example);
                //System.out.println(example.getKeys()+"\n");
            }    
        }
    
    
    
    private static void classifyTestData(String filePath){
        
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
        Node root = new Node();
//        root.SetDepth(treeDepth);
//        treeDepth+=1;
        /*
            If all examples have the same classifier,
            return the single-node tree Root, with label classifier;
        */
        if(examplesHaveSameClassifier(examples,goal))
            {
                Value v = ((Example)examples.toArray()[0]).getValue(target_attribute);
//                root.setLabel(v.getContent());
                root.setValue(v);
                return root;
            }
        
        if(attributes.isEmpty())
            { 
                Value v = mostCommonValueOnTargetAttribute(examples,target_attribute);
                root.setAttribute(target_attribute);
//                root.setLabel(v.getContent());
                root.setValue(v);
                return root;
            }
        
        else{
            Attribute A = IMPORTANCE(attributes,examples);
            root.setAttribute(A);
            for(Value v: A.getPossibleValues()){
                //Add a new tree branch below Root,
                //corresponding to the test A = vi.
                Branch newBranch = new Branch(v);
//                System.out.println(newBranch);
                //System.out.println(root.getDepth()+1);
                //newBranch.setDepth(root.getDepth()+1);
                root.addBranch(newBranch);
                //Let Examples(vi) be the subset of examples that
                // have the value vi for A
                HashSet<Example> subSet_Examples = subSetHavingAttribute(examples,A,v);
                //If Examples(vi) is empty
                if(subSet_Examples.isEmpty()){
                    //below this new branch add a leaf node
                    Node leafNode = new Node();
                    Value val = mostCommonValueOnTargetAttribute(examples,target_attribute);
                    leafNode.setValue(val);
//                    System.out.println(leafNode.getLabel());
//                    System.out.println(newBranch.getDepth()+1);
//                    leafNode.SetDepth(newBranch.getDepth()+1);
                    newBranch.addLeaf(leafNode);
                }
                else{
                    ArrayList<Attribute> newAttributes = Attributes_removedA(attributes,A);
                    Node subTree = ID3(subSet_Examples, target_attribute, newAttributes);
//                    subTree.SetDepth(newBranch.getDepth()+1);
                    newBranch.addLeaf(subTree);
                }
            }
        }
        //root.SetDepth(treeDepth+1);
        return root;
    }
    
    /*
        counts occurrence of each value for an attribute in the data set
    */
    public static HashMap<Value,Integer> valuesCount(HashSet<Example> examples, Attribute target_attribute){
        HashMap<Value,Integer> valueCounter = new HashMap<Value,Integer>();
//        System.out.println("attribute possible values:"+target_attribute.getPossibleValues());
        for(Value v:target_attribute.getPossibleValues()){
            if(!valueCounter.containsKey(v)){valueCounter.put(v, 0);}
        }
        for(Example ex:examples){
            Value v = ex.getValue(target_attribute);
//            System.out.println("Found value v:"+v);
//            System.out.println(ex.getContent());
            valueCounter.replace(v, valueCounter.get(v)+1);
        }
//        System.out.println("the searched attribute: "+target_attribute);
//        System.out.println(valueCounter);
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
        //System.out.println(target_attribute);
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
        Attribute minimizer = null;
        double entropy_value = Short.MAX_VALUE;
        for(Attribute attr:attributes){
            if(!attr.text.equals(goal.getText())){
                double attribute_entropy = ENTROPY(attr,examples);
                if(attribute_entropy<entropy_value){
                    minimizer = attr;
                    entropy_value=attribute_entropy;
                }
            }
        }
        //System.out.println("\n\n\nThe minimizer:"+minimizer+"\n\n");
        return minimizer;
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
        //System.out.println(attr);
        //System.out.println(valueCounter);
        HashMap<Value,HashMap<Value,Integer>> goalCounter = new HashMap<Value,HashMap<Value,Integer>>();
        for(Value v:valueCounter.keySet()){
            HashMap<Value,Integer> possibleValues = new HashMap<Value,Integer>();
            for(Value possible:goal.possibleValues){
                possibleValues.put(possible, 0);
            }
            goalCounter.put(v, possibleValues);
//            System.out.println(possibleValues);
        }
//        double result = 0;
//        return result;
        for(Example ex:examples){
            Value s = ex.content.get(attr);
            Value goalVal = ex.content.get(goal);
            //Class classifier = new Classifier(goalVal.getContent());
            goalCounter.get(s).replace(goalVal, goalCounter.get(s).get(goalVal)+1);
        }
        for(Value s:attr.getPossibleValues()){
            Class classifier = new Class();
            Value v = null;
            int count=0;
//            System.out.println("VALUE s:"+s);
            for(Value a:goalCounter.get(s).keySet()){
                if(goalCounter.get(s).get(a)>=count){
                    v = a;
                    count = goalCounter.get(s).get(a);
//                    System.out.println("count "+count);
                }
            }
//            classifier.setValue(v);
//            classifier.setCounter(goalCounter.get(s).get(v));
//            System.out.println("the value:"+v+" with value:"+goalCounter.get(s).get(v));
//            s.setClassifier(classifier);
        }
        
        double result=0;
        for(Value v:valueCounter.keySet()){
            double div = valueCounter.get(v)/(total_examples*1.0);
            double total=0;
            double currentSum=0;
            for(Value possibleGoal:goalCounter.get(v).keySet()){
                double count = goalCounter.get(v).get(possibleGoal);
                //System.out.println("count :"+count);
                if(count==0.0){
//                    System.out.println(count+"/"+total_examples+"log2"+count+"/"+total_examples);
                    total = 0.0;
//                    System.out.println("current total is"+total);
                }
                else{
//                    System.out.println(count+"/"+total_examples+"log2"+count+"/"+total_examples);
                    total = -1*(count/total_examples)*log2((count/total_examples));
//                    System.out.println("current total is"+total);
                }
                currentSum+=total;
                
            }
            //div = div*total;
            div = div * currentSum;
            //total= total*div;
//            System.out.println("the div: "+div);
            result+=div;
//            System.out.println("count sum: "+result);
//            System.out.println("-----------");
        }
//        System.out.println("Returning result:  \n\n\nresult :"+result);
        return result;
    }
    
    public static double log2(double num){
        return Math.log(num)/Math.log(2);
    }
    
    
//    protected static void printSpaces(int spaces){
//        for(int a=0;a<spaces;a++){
//            System.out.print("  ");
//        }
//    }
    
    protected static void printSpaces(Node node){
        for(int a=0;a<node.getDepth();a++){
            System.out.print("  ");
        }
    }
    
    protected static void printSpaces(Branch branch){
        for(int a=0;a<branch.getDepth();a++){
            System.out.print("  ");
        }
    }
    
    static void countInExamples(Node node){
        int d=0;
        Attribute attr=null;
        if(node.getAttribute()!=null){
            attr=node.getAttribute();
        }
        ArrayList<Branch> arraylist = node.getValues();
        for(int s = 0;s<node.getValues().size();s++){
            d = searchExamples(attr,arraylist.get(s));
            arraylist.get(s).getLeaf().setCountDecision(d);
        }
    }
    
    static int searchExamples(Attribute attr,Branch branch){
        int count = 0;
        for(Example ex:examples){
            if(ex.getValue(attr).equals(branch.getVal())){
                if(ex.getValue(goal).equals(branch.getLeaf().getValue())){
                    count++;
                }
            }
        }
        return count;
    }
    
    static void TreeTraversal(Node root){
        if(root.getAttribute()!=null){
            System.out.println();
            printSpaces(root);
            countInExamples(root);
            System.out.print(root+"\n");
        }
        else{
            System.out.print(root.getValue()+" "+root.getCountDecision()+"\n");
        }
        ArrayList<Branch> arraylist = root.getValues();
        if(arraylist!=null){
           for(int s = 0;s<arraylist.size();s++){
//               printSpaces(arraylist.get(s).getDepth());
               
               arraylist.get(s).setDepth(root.getDepth()+1);
               printSpaces(arraylist.get(s));
               System.out.print(arraylist.get(s).getVal()+": ");
               arraylist.get(s).getLeaf().SetDepth(arraylist.get(s).getDepth()+1);
               TreeTraversal(arraylist.get(s).getLeaf());
                //TreeTraversal(arraylist.get(s).getVal());
           }
       }
    }
    
    protected static String GENERAL_SEARCH(Node root,MyQueue<Node> QUEUEING_FN){
        MyQueue<Node> nodes = MAKE_QUEUE(QUEUEING_FN,root);
        while(!EMPTY(nodes)){
            Node node = REMOVE_FRONT(nodes);
            String tab = "\t";
            for(int i=0;i<node.getDepth();i++){
                System.out.print(tab);
            }
            System.out.println(node);
            Set<Node> new_nodes = EXPAND(node);
            nodes = QUEUEING_FN.add(nodes,new_nodes);
        }
        return "solution not found";
    }
    
    private static MyQueue<Node> MAKE_QUEUE(MyQueue<Node> queue,Node node){
        queue.add(queue, node);
        return queue;
    }
    
    private static Set<Node> EXPAND(Node node){
        Set<Node> childNodes = new HashSet<Node>();
        for(Branch b:node.getValues()){
            childNodes.add(b.getLeaf());
        }
        return childNodes;
    }
    
    
    protected static boolean EMPTY(MyQueue<Node> myQueue){
        if(myQueue.size==0)return true;
        return false;
    }
    
    private static Node REMOVE_FRONT(MyQueue<Node> nodes){
        Node node = null;
        //Algorithm.currentDepth+=1;
        if(nodes.size==0){return node;}
        //System.out.println(nodes.type+"\n");
        Fifo fifo = (Fifo)nodes;
                //System.out.println("Removing the following node");
        try{
            node = (Node)fifo.list.remove();
            fifo.size--;
            }
        catch(NoSuchElementException ex){
            return null;
        }
        return node;
    }
}
