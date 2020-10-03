/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invCheck;

import java.util.*;
import java.util.Set;


/**
 *
 * @author dqp6065,jbr9093
 * using an adjacency list to create a directed, weighted transition system
 */
public class TransitionSystem {
   protected Set<State> setStates;
   protected Set<Transition> setTransitions;
   protected Map<State, Set<Transition>> adjacencyList; 
   protected State initialState;
   
   protected Set<State> reachableStates;
   protected Stack<State> stackStates;  //to be treated as a stack
   protected boolean b; //states in R statisfy φ

   public static void main(String[] args)
   {  TransitionSystem graph = new TransitionSystem();
      State s0 = graph.addState("s0", "a");
      State s1 = graph.addState("s1","a");
      State s2 = graph.addState("s2","a,b");
      State s3 = graph.addState("s3","b");
      State s4 = graph.addState("s4","b");
      
      graph.setInitState(s0);
      //G = gamma, A = alpha, B = beta
      graph.addTransitions(s0, s1, "G");
      graph.addTransitions(s1, s1, "A");
      graph.addTransitions(s1, s3, "A");
      graph.addTransitions(s1, s4, "B");
      graph.addTransitions(s4, s3, "G");
      graph.addTransitions(s4, s2, "A");
      graph.addTransitions(s2, s4, "B");
      graph.addTransitions(s2, s0, "A");
      graph.addTransitions(s0, s2, "A");
      
      System.out.println("Example Graph:\n" + graph);
      System.out.println("Performing depth-first search from D:");
      graph.invariantCheck();
   }
   
   public TransitionSystem(){
       this.setStates = new HashSet<>();
       this.setTransitions = new HashSet<>();
       this.adjacencyList = new HashMap<>();
   }
         
   public String invariantCheck(){
       stackStates = new Stack();
       reachableStates = new HashSet<>();
       b = true;
       do{
           visit(initialState);
       }while(!stackStates.empty()&&b);
       if(b) return "yes";
       else return "no";
   }
   
   public void visit(State initState){
       stackStates.push(initState);
       reachableStates.add(initState);
       do{
           State s = stackStates.peek();

           ArrayList<State> next = s.getConnectingStates();
           for(int i = 0;i<next.size();i++){
               if(!stackStates.contains(next.get(i))){
                    stackStates.push(next.get(i));
               }
           }
           
            if(reachableStates.contains(stackStates.peek())){
                State check = stackStates.pop();
                //check propositional form
                System.out.println(check);
                System.out.println("if");
            }
            else{
                State ss = stackStates.peek();
                 ArrayList<State> sss = ss.getConnectingStates();
                for(int i = 0;i<sss.size();i++){
                   if(!stackStates.contains(sss.get(i))){
                     stackStates.push(sss.get(i));
                     reachableStates.add(sss.get(i));                       
                   }
                }
            }
            System.out.println(reachableStates);
            System.out.println(stackStates);
       }while(!stackStates.empty()||b);
   }
   
   public void setInitState(State s0){
       this.initialState = s0;
   }
   public State getInitState(){
       return this.initialState;
   }
   
   //adds a new state as a string
   public State addState(String s, String AP){
       State state = new tsState(s, AP);
       setStates.add(state);
       adjacencyList.put(state, new HashSet<Transition>());
       return state;
   }
   
   //ending state = s2
   public Transition addTransitions(State s1, State s2, String act){
       Transition t = new tsTransition(s1, s2, act);
       setTransitions.add(t);
       adjacencyList.get(s1).add(t);
       return t;
   }
   
   public String toString()
   {  String output = "Graph:\n";
      for (State s : setStates)
         output += "" + s + " has transitions:"
            + adjacencyList.get(s) + "\n";
      return output;
   }
   
   //inside class to create transition system
   protected class tsState implements State{
       private String stateName;
       private String AP;
       
       //sets state as a string
       public tsState(String s, String AP){
           this.stateName = s;
           this.AP = AP;
       }
       public String getState(){
           return stateName;
       }
       public String getAP(){
           return AP;
       }
       
       public Set<Transition> getTransitions(){
           return adjacencyList.get(this);
       }
       
       public ArrayList<State> getConnectingStates(){
           ArrayList<State> states = new ArrayList<State>();
           for(Transition t : adjacencyList.get(this))
               states.add(t.oppositeStates(this));
           return states;
       }
       
       public boolean isAdjacent(State s){
           return getConnectingStates().contains(s);
       }
       
       public String toString(){
           return stateName;
       }
   }
   
   protected class tsTransition implements Transition{
       //directed graph goes from state1 to state2
       private State state1, state2;
       private String actionName;

       public tsTransition(State s1, State s2, String act){
           this.state1 = s1;
           this.state2 = s2;
           this.actionName = act;
       }
       
       
       //returns states as a array
       public State[] endStates(){
           State[] states = new State[2];
           states[0] = state1;
           states[1] = state2;
           
           return states;
       }
       
       public State oppositeStates(State state){
           if(state1.equals(state))
               return state2;
           else
               return state1;
       }
       
       public String toString(){
           return("("+state1+"-"+state2+":"+actionName+")");
       }
   }
}
