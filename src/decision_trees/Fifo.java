package decision_trees;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import decision_trees.Node;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author user
 */
public class Fifo<T> extends MyQueue<T>{
    LinkedList list;
    public Fifo(){
        super();
        this.list = new LinkedList();
        super.type = "fifo";
        super.size = 0;
        super.maxSize = 0;
    }
    
    @Override
    public Fifo add(MyQueue<Node> queue, Set<Node> nodes) {
        Fifo fifo = (Fifo)queue;
        if(nodes==null){return fifo;}
        fifo.list.addAll(nodes);
        super.size=fifo.list.size();
        if(super.size>super.maxSize){
            super.maxSize=super.size;
        }
        return fifo;
    }
    
    @Override
    public Fifo add(MyQueue<Node> queue,Node node){
        Fifo fifo = (Fifo)queue;
        if(node==null){return fifo;}
        fifo.list.add(node);
        super.size=fifo.list.size();
        if(super.size>super.maxSize){
            super.maxSize=super.size;
        }
        return fifo;
    }
    
//    @Override
//    public String toString(){
//        System.out.println("printing queue\n");
//        String result ="";
//        for(Object obj:this.list){
//            Node node = (Node)obj;
//            result+= Node.result(node);
//            result+="\n\n";
//        }
//        return result;
//    }
}
