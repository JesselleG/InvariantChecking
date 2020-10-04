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
   protected String counterEx;

   public static void main(String[] args)
   {  TransitionSystem graph = new TransitionSystem();
      State s0 = graph.addState("s0","a,b");
      State s1 = graph.addState("s1","a,b");
      State s2 = graph.addState("s2","a,b");
      State s3 = graph.addState("s3","a,b");
      State s4 = graph.addState("s4","a,b");
      
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
      graph.invariantCheck("a and b");
   }
   
   public TransitionSystem(){
       this.setStates = new HashSet<>();
       this.setTransitions = new HashSet<>();
       this.adjacencyList = new HashMap<>();
   }
         
   public void invariantCheck(String pf){
       stackStates = new Stack();
       reachableStates = new HashSet<>();
       b = true;
       counterEx="formula is not satisfied at: ";
       while(b && !reachableStates.contains(initialState))
       {
            visit(initialState, pf);   
       }
                  
       if(b) System.out.println("yes");
       else System.out.println("no. "+counterEx);
   }
   
   public void visit(State initState, String pf){
       stackStates.push(initState);
       reachableStates.add(initState);
       while(!stackStates.empty()&&b){
           State s = stackStates.peek();
           State postS = null;
           
           ArrayList<State> next = s.getConnectingStates();
           for(int i = 0;i<next.size();i++)
           {
               if(!reachableStates.contains(next.get(i)) || next.get(i) != null)
                 postS = next.get(i);
              // System.out.println("YAYAYA POST S ISSSS "+postS);
           }

          
            if(reachableStates.contains(postS) || postS == null)
            {
                State check = stackStates.pop();
                //check propositional form
                checkFormula(check, pf);
//                System.out.println(check);
            }
            
            else
            {   
                State ss = stackStates.peek();
                 ArrayList<State> sss = ss.getConnectingStates();
                for(int i = 0;i<sss.size();i++)
                {
                
                    if(!stackStates.contains(sss.get(i)) && sss.get(i) != null)
                    {
                     stackStates.push(sss.get(i));
                     reachableStates.add(sss.get(i));        //add to reachable states               
                    
                    }
                }
            }
//            System.out.println(stackStates.toString());
       }
   }
   
   private void checkFormula(State s, String pf){
       String correctAP = "";
       String s_ap = s.getAP(); //a
       String low_pf = pf.toLowerCase();

       ArrayList temp = new ArrayList();
       String[] tokens = low_pf.split("\\s|\\(|\\)");
       for (String token : tokens) {
           if (!token.isEmpty()) {
               temp.add(token);
           }
       }
       Stack<String> postFix = new Stack<>();
       for(int i=0;i<temp.size();i++){
           String t = (String) temp.get(i);
           if(t.equals("not")){
               String next = (String) temp.get(i+1);
               postFix.push("!"+next);
               i++;
           }
           else postFix.push(t);

       }

       while(!postFix.empty()){
           String top = postFix.pop();
           switch (top) {
               case "or":
                   {
                       System.out.println("Case OR");
                       
                       String ap1 = postFix.pop();
                       String ap2 = postFix.pop();
                       b = s_ap.equals(ap1)||s_ap.equals(ap2);
                       if(!b) correctAP ="{"+ap1+"}";
                       break;
                   }
               case "and":
                   {
                       System.out.println("Case AND");

                       String ap2 = postFix.pop();
                       String ap1 = postFix.pop();
                       if(ap2.contains("!")&&ap2.contains("!")){
                           
                           String[] ap2_split = ap2.split("\\!");
                           ap2 = ap2_split[1];
                           String[] ap1_split = ap1.split("\\!");
                           ap1 = ap1_split[1];
                           System.out.println(ap2+ap1);
                           b=(!s_ap.equals(ap1)&&!s_ap.equals(ap2));
                           if(!b) correctAP="{"+ap1+"} or {"+ap2+"}";
                       }
                       else{
                           b=(s_ap.equals(ap1+","+ap2))||(s_ap.equals(ap2+","+ap1));
                           if(!b) correctAP="{"+ap1+","+ap2+"}";
                       }       
                       break;
                   }
               case "imp":
                   {
                       System.out.println("Case IMP");

                       String ap2 = postFix.pop();
                       String ap1 = postFix.pop();
                       if(ap2.contains("!")){
                           String[] ap2_split = ap2.split("\\!");
                           ap2 = ap2_split[1];
                           b=((s_ap.contains(ap1)&&!s_ap.contains(ap2))||(!s_ap.contains(ap1)));
                           if(!b) correctAP="{"+ap1+"} or {"+ap2+"}";
                       }
                       else{
                           b=((s_ap.contains(ap1)&&s_ap.contains(ap2))||(!s_ap.contains(ap1)));
                           
                           if(!b) correctAP="{"+ap2+"} or {"+ap1+","+ap2+"}";
                       }       break;
                   }
//               case "not":
//                   {
//                       String ap1 = postFix.pop();
//                       postFix.push("!"+ap1);
//                       break;
//                   }
               default:
                   if(!postFix.empty()){
                       String t = postFix.peek();
                       if(t.equals("or")||t.equals("and")||t.equals("imp")||t.equals("not")){
                           t = postFix.pop();
                           
                           postFix.push(top);
                           postFix.push(t);
                       }
                   }   break;
           }
           if(!b){
               counterEx += s.toString()+" try: "+correctAP;
           }
       }
//       System.out.println(s+" is "+b);
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
