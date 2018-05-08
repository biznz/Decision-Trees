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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
    private static Set<Example> examples;
    private static Set<Example> testSamples;
    private static ArrayList<Attribute> attributes;
    private static Node Tree_root;
    private static int chosenOption=-1;
    private static boolean continuous_values;
    
    public static void main(String[] args) {
        continuous_values=false;
        while(printInterface()){
            if(chosenOption==1){
                readTrainingDataFile();
                //if sample has continuous values
//                if(continuous_values==true){
//                    //sort sample set
//                    //this sorting needs to occur at any given node in a tree where relevant
////                    examples=sortSamples(examples);
////                    printExamples(examples);
//                    //find splitting points
//                }
                TreeTraversal(Tree_root);
            }
            if(chosenOption==2){
                readTestDataFile();
                classifyTestData();
            }
        }
        //TreeTraversal(Tree_root);   
    }
    
    /**
     * prints examples data set
     * 
     * @param data 
     */
    
    private static void printExamples(Set<Example> data){
        for(Example ex:data){
            System.out.println(ex);
        }
    }
    
    /**
     * method creates an ordered set from the read data
     * @param input class variable with the training data to be read
     */
    
    private static TreeSet<Example> sortSamples(Set<Example> input){
        TreeSet<Example> treeSet = new TreeSet<Example>();
        for(Example ex:input){
            treeSet.add(ex);
        }
        return treeSet;
    }
    
    /**
     * method calls a classification method on each example on testSamples set
     */
    
    private static void classifyTestData(){
        for(Example ex:testSamples){
//            System.out.println(ex);
            String classification = classifyExample(ex,Tree_root);
            ex.replace(goal, new Value(classification));
            System.out.println("\nExample:"+ex);
        }
    }
    
    /**
     * method runs the decision tree on the example providing it with a classification
     * 
     * @param ex the example to be classified
     * @param node the root of the decision tree
     * @return a string with classification value
     */
    
    private static String classifyExample(Example ex,Node node){
        Node current = node;
        String result="";
        Value v;
        while(node.getAttribute()!=null){
            //System.out.println(node.getAttribute()+" fawf");
            v = ex.getValue(node.getAttribute());
            for(int a = 0; a<node.getValues().size();a++){
                //System.out.println(node.getValues().get(a));
                if(node.getValues().get(a).getVal().equals(v)){
//                    System.out.println("node value:"+node.getValues().get(a).getVal()+"equals:"+" v :"+v);
//                    System.out.println("attribute :"+node.getValues().get(a).getLeaf().getAttribute());
                    if(node.getValues().get(a).getLeaf().getAttribute()==null){
                        result = node.getValues().get(a).getLeaf().getValue().getContent(); //.getVal().getContent();
                        return result;
                    }
                    node = node.getValues().get(a).getLeaf();
                    //classifyExample(ex,node.getValues().get(a).getLeaf());
                }
            }
        }
        return result;
    }
    
    /**
     * reads a training data file to set examples and runs ID3 on read data
     */
    
    private static void readTrainingDataFile(){
        Scanner in = new Scanner(System.in);
        String line="";
        System.out.print("Give a training file path: ");
        while((line = in.nextLine()).equals("")){
            System.out.println("Training data file path: ");
            System.out.println("read line:"+line);
        }
        try{
                examples = readData(line,examples);
                Tree_root = ID3(examples,goal,attributes);
            }
            catch(IOException ex){
                System.out.println("Couldn't read file");
            }
    }
    
    /**
     * reads a test data file to testSamples Set
     */
    
    private static void readTestDataFile(){
        Scanner in = new Scanner(System.in);
        String line="";
        System.out.print("Give a test file path: ");
        while((line=in.nextLine()).equals("")){
            System.out.println("Test date file path: ");
            System.out.println("read line:"+line);
        }
        try{
            testSamples = readData(line,testSamples);
        }
        catch(IOException ex){
            System.out.println("Couldn't read file");
        }
    }
    

    /**
     * method loops an a console interface on input
     * with the options "1.read Training data from file"
     * with the options "2.read test data and classify it"
     * with the options "3.exit program"
     * 
     * @return a boolean on console menu interaction with input
     */
    
    private static boolean printInterface(){
        Scanner in = new Scanner(System.in);
        chosenOption = -1;
        int input;
        while(chosenOption==-1){
            System.out.println("\nChoose option");
            System.out.println("1.read Training data from file");
            System.out.println("2.read test data and classify it");
            System.out.println("3.exit program");
            if(in.hasNext()){
                input = in.nextInt();
                if(input==1){
                    chosenOption=input;
                }
                if(input==2){
                    chosenOption=input;
                }
                if(input==3){
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * function tests if a value can be a part of a continuous set of values
     * sets class variable continuous_values to true if so
     * 
     * @param examples set of examples to test
     * @param at the attribute to check
     */
    
    private static void testDiscreteness(Set<Example> examples,Attribute at){
        Value value = null;
        float g;
        for(Example sample:examples){
            value = sample.getValue(at);
            try{
                g=Float.parseFloat(value.getContent());
                continuous_values=true;
                return;
            }
            catch(NumberFormatException ex){
                return;
            }
        }
    }
    
    
    /**
     * method finds split points on the range of continuous dataSet values
     * 
     * @param samples the set of samples to test
     * @param attr the attribute to test the samples for
     */
    
    private static void findSplittingPoints(Set<Example> samples,Attribute attr){
        Example.setSortAttribute(attr);
        Example previous=null;
        Example current=null;
        int count = 0;
        HashMap<Example,Integer> maps= new HashMap<Example,Integer>();
        samples = sortSamples(samples);
        for(Example a:samples){
            if(previous==null){
                previous=a;
                count+=1;
            }
            else if(previous!=null && current==null){
                current=a;
                count+=1;
            }
            else if(previous!=null && current!=null){
                previous=current;
                current=a;
                count+=1;
            }
            if(previous!=null && current!=null){
                if(!equalClassifiers(previous,current)){
                    maps.put(a,count);
                    count=0;
                }
                else{
                    count+=1;
                }
            }
        }
        printExamples(samples);
        System.out.print("\n\nSPLIT");
        System.out.println("\nspliting attr:"+attr);
        System.out.println("BEGIN MAP");
        Iterator it = maps.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        System.out.println("END MAP");
    }
    
    private static boolean equalClassifiers(Example a,Example b){
        if(!a.getValue(goal).equals(b.getValue(goal))){
            System.out.println("testing a and b");
            System.out.println("testing :"+a+" testing :"+b);
            return true;
        }
        return false;
    }
    
    /**
     * generic data reading method for both training data ( examples are classified ) and,
     * test data ( examples are to be classified according to decision tree on the data
     * 
     * @param filePath file relative or absolute path
     * @param data set to where read examples will be loaded to
     * @return data set with the read data
     * @throws IOException 
     */
    
    private static Set<Example> readData(String filePath,Set<Example> data) throws IOException{
            BufferedReader in = new BufferedReader(new FileReader(new File(filePath))); //restaurant.csv,weather.csv,
            //first line from the file contains the attribute description
            String[] Attributes = in.readLine().split(",");
            //arraylist containing the attributes
            attributes = new ArrayList<Attribute>();
            for(int i=1;i<Attributes.length;i++){
                attributes.add(new Attribute(Attributes[i]));
            }
            // goal attribute records the classifier
            if(goal==null){
                goal = attributes.get(attributes.size()-1);
            }
            // the set of examples on the file 
            data = new HashSet<Example>();
            String l;
            while(( l = in.readLine())!=null){
                String[] line = l.split(",");
                int counter = 0;
                Example example = new Example();
                example.setId(line[0]);
                for(int i=1;i<line.length;i++){
                    Value v = new Value(line[i]);
                    //testDiscreteness(line[i]);
                    if(!attributes.get(i-1).possibleValues.contains(v)){
                        attributes.get(i-1).possibleValues.add(v);
                    }
                    example.add(attributes.get(i-1), v);
                }
                data.add(example);
            }   
            return data;
        }
    
    
    /**
     * implementation of a decision tree learning algorithm ID3
     * 
     * @param examples the dataset of examples to learn from
     * @param target_attribute the classification attribute
     * @param attributes an arraylist of attributes that describe remaining attributes to be tested
     * @return a root node on a decision tree of the data
     */
    
    protected static Node ID3(Set<Example> examples,Attribute target_attribute,ArrayList<Attribute> attributes){
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
    
    /**
     * method builds a map of values and the number of occurrences of the value in 
     * set data
     * 
     * @param examples set of examples 
     * @param target_attribute the target_attribute to get the values from
     * @return an HashMap of values on number of occurrences
     */
    
    public static HashMap<Value,Integer> valuesCount(Set<Example> examples, Attribute target_attribute){
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
    
    /**
     * method verifies which value is the most common on target attribute
     * it maps a number of occurrences to a value by calling valuesCount
     * 
     * @param examples set of examples to check a value from
     * @param target_attribute a target attribute from which values are counted
     * @return 
     */
    
    public static Value mostCommonValueOnTargetAttribute(Set<Example> examples, Attribute target_attribute){
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
    
    /**
     * removes an attribute from an arraylist
     * 
     * @param attributes the arraylist of attributes
     * @param toRemove the attribute to remove from the attribute list
     * @return the resulting arraylist ( without toRemove attribute )
     */
    
    public static ArrayList<Attribute> Attributes_removedA(ArrayList<Attribute> attributes,Attribute toRemove){
        ArrayList<Attribute> att = new ArrayList<Attribute>();
        for(Attribute attr:attributes){
            att.add(attr);
        }
        att.remove(toRemove);
        return att;
    }
    
    
    
    /**
     * builds a subset of examples that have Value v on Attribute attr
     * 
     * @param examples the full example set
     * @param attr the attribute to check
     * @param v the value on the attribute to check
     * @return subset of examples
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
    
    
    /**
     * method checks if all the input set examples have same classification
     * 
     * @param examples the set examples to be tested ( always a subset of the training data )
     * @param target_attribute the classifier attribute
     * @return true if all have the same classification, false otherwise
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
    
    /**
     * 
     * method calculates the most important attribute by determining the entropy of each
     * and returning the one with less entropy
     * 
     * @param attributes an arraylist of attributes that describes attributes to be tested
     * @param examples set of data examples 
     * @return most important attribute for the decision tree
     */
    
    public static Attribute IMPORTANCE(ArrayList<Attribute> attributes, Set<Example> examples){
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
        testDiscreteness(examples,minimizer);
        findSplittingPoints(examples,minimizer);
        return minimizer;
    }
    
    /**
     * 
     * Entropy (in information theory) a logarithmic measure of the rate of transfer of information in a particular message or language.
     *       value on given attribute
     * 
     * @param attr attribute to calculate the information gain on
     * @param examples set of data examples 
     * @return an entropy value for a given attribute
     */
    
    public static double ENTROPY(Attribute attr,Set<Example> examples){
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
    
    /**
     * method calculates de logarithm base 2 of x
     * 
     * @param num x value to use in log b (x)
     * @return a log base 2 value of x
     */
    
    public static double log2(double num){
        return Math.log(num)/Math.log(2);
    }
    
    /**
     * method a prints a product of depth of the node and # of spaces
     * 
     * @param node a node from current decision tree
     */
    
    protected static void printSpaces(Node node){
        for(int a=0;a<node.getDepth();a++){
            System.out.print("  ");
        }
    }
    
    /**
     * method a prints a product of depth of the branch and # of spaces
     * 
     * @param branch a branch from current decision tree
     */
    
    protected static void printSpaces(Branch branch){
        for(int a=0;a<branch.getDepth();a++){
            System.out.print("  ");
        }
    }
    
    /**
     * method counts the occurrence of different classifications
     * for different values in different branches and sets the countDecision
     * value on leaf Nodes
     * 
     * @param node root of a decision tree
     */
    
    
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
    
    /**
     * method counts the number of attributes with a expected classification
     * 
     * @param attr attribute to evaluate
     * @param branch where the current classification is retrieved from
     * @return a number of occurrences
     */
    
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
    
    /**
     * method is a recursive tree traversal on a decision tree
     * printing to screen its components
     * @param root the root node of the decision tree
     */
    
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
}
