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
      State s0 = graph.addState("s0","a");
      State s1 = graph.addState("s1","a");
      State s2 = graph.addState("s2","a,b");
      State s3 = graph.addState("s3","");
      State s4 = graph.addState("s4","a");
      
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
      graph.invariantCheck("a IMP !b");
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
   
   //checks invariant if the specific state satisfies the condition of pf (phi)
   private void checkFormula(State s, String pf){
       String correctAP = ""; //the label that would be required in order to satisfy phi
       String[] stateLabels = s.getAP().split(",|\\s|-"); //get the current state's labels (each index is an atomic proposition) 
       String low_pf = pf.toLowerCase();

       ArrayList<String> temp = new ArrayList<>();
       String[] tokens = low_pf.split("\\s|\\(|\\)");
       for (String token : tokens) {
           if (!token.isEmpty()) {
               temp.add(token);
           }
       }
       Stack<String> postFix = new Stack<>();
       for(int i=0;i<temp.size();i++){
           String t = temp.get(i);
           
           if(t.equals("not"))  //convert "not" operator to its symbol "!"
           {    
               String next = temp.get(i+1);
               postFix.push("!"+next);
               i++;
           }
           else postFix.push(t);

       }

       while(!postFix.empty())
       {
           String top = postFix.pop(); //stack (leftmost is top){b, OR, a}
           switch (top) {
               case "or":
                   {

                       System.out.println("Case OR");
                       boolean ap1_match = false;
                       boolean ap2_match = false;
                       String ap2 = postFix.pop();            
                       String ap1 = postFix.pop();  
                       if(stateLabels[0].equalsIgnoreCase("null"))
                       {
                           b = false;
                           correctAP ="{"+ap1+"} or {"+ap2+"}";
                           break;
                       }

                       for (String ap : stateLabels)                                 
                            {
                                if (ap.equalsIgnoreCase(ap1)) 
                                {                         
                                   ap1_match = true;
                                }
                                if (ap.equalsIgnoreCase(ap2)) 
                                {                             
                                   ap2_match = true;
                                }
                            }  
                       
                       if(ap1.contains("!") || ap2.contains("!"))
                       {                         
                           b = ((ap1_match && !ap2_match) || (!ap1_match && ap2_match));
                       }
                       else 
                       {
                           b = ap1_match || ap2_match;
                       }


                       if(!b) correctAP ="{"+ap1+"} or {"+ap2+"}";
                       break;
                   }
               case "and":
                   {

                       System.out.println("Case AND");
                       boolean ap1_match = false; 
                       boolean ap2_match = false;
                       String ap2 = postFix.pop();
                       String ap1 = postFix.pop();
                       if(stateLabels[0].equalsIgnoreCase("null"))
                       {
                           b = false;
                           correctAP ="{"+ap1+"} or {"+ap2+"}";
                           break;
                       }
                       
                       String correct = "";
                       int cases = -1; // 0 = both operators have NOT, 1 = ap1 has NOT, 2 = ap2 has NOT
                       if(ap1.contains("!")|| ap2.contains("!"))
                       {           
                           if(ap1.contains("!") && ap2.contains("!"))
                           {    
                               cases = 0;
                               String[] ap2_split = ap2.split("!");
                                ap2 = ap2_split[1];

                                String[] ap1_split = ap1.split("!");
                                ap1 = ap1_split[1];
                           }
                               
                           else if(ap1.contains("!"))
                           {                               
                                cases = 1;
                                String[] ap1_split = ap1.split("!");
                                ap1 = ap1_split[1];
                           }
                               
                           else
                           {
                                cases = 2;
                                String[] ap2_split = ap2.split("!");
                                ap2 = ap2_split[1];
                           }
                                                                               
                              
                            for (String ap : stateLabels)                                 
                            {
                                if (ap.equalsIgnoreCase(ap1)) 
                                {
                                   ap1_match = true;
                                }
                                if (ap.equalsIgnoreCase(ap2)) 
                                {
                                   ap2_match = true;
                                }
                            }
                            
                            switch(cases)
                            {
                                case 0:
                                {
                                     b = !ap1_match && !ap2_match;
                                     correct = "{"+ap1+","+ap2+"}";
                                     break;
                                }
                                case 1:
                                    b = !ap1_match && ap2_match;
                                    correct = "{!"+ap1+","+ap2+"}";
                                    break;
                                case 2:
                                    b = ap1_match && !ap2_match;
                                    correct = "{"+ap1+",!"+ap2+"}";
                                    break;
                            }
                          
                          
                           if(!b) correctAP= correct;
                       }                                                                                                 
                      
                       else //both atomic propositions do not have the "NOT operator"
                       {
                      
                          for(String ap : stateLabels)
                           {    
                               if(ap.equalsIgnoreCase(ap1))
                                   ap1_match = true;
                               if(ap.equalsIgnoreCase(ap2))
                                   ap2_match = true;
                           }                 
                            b = ap1_match && ap2_match;
                          
                           if(!b) correctAP="{"+ap1+","+ap2+"}";
                       }       
                       
                       break;
                   }
               case "imp": //if current expression is the "IMPLIES ->" operator
                   {
                        System.out.println("Case IMP");
                        Arrays.sort(stateLabels);
                        String correctLabel = "";
                        String ap2 = postFix.pop();
                        String ap1 = postFix.pop();
                        if(!ap1.contains("!") && !ap2.contains("!"))// then a IMP b
                        {
                            if(stateLabels[0].equalsIgnoreCase("null"))// then !a and !b, where F & F = TRUE (state label is empty {} )
                            {
                                b = true;
                            }
                            else if(stateLabels.length == 1 || stateLabels.length == 0) //then check which label matches which AP
                            {
                                if(stateLabels[0].equalsIgnoreCase(ap2)) //{b}
                                {   // F & T = TRUE
                                    b = true;
                                }
                                else if (stateLabels[0].equalsIgnoreCase(ap1)) //{a}
                                {   // T & F = FALSE
                                    b = false;
                                    correctLabel = "{"+ap1+","+ap2+"}";
                                }
                            }
                            else if(stateLabels.length == 2) //{a,b}
                            {
                                //T & T = TRUE
                                b = true;
                                
                            }
                        }
                        else if(!ap1.contains("!") && ap2.contains("!"))// a imp !b
                        {
                            if(stateLabels[0].equalsIgnoreCase("null"))// then !a and !b, where F & T = TRUE
                            {
                                b = true;
                            }
                             
                            else if(stateLabels.length == 1 || stateLabels.length == 0) //then check which label matches which AP
                            {
                                if(stateLabels[0].equalsIgnoreCase(ap2))  //{b}
                                {   // F & F = TRUE
                                    b = true;
                                }
                                else if (stateLabels[0].equalsIgnoreCase(ap1))//{a}
                                {   // T & T = TRUE
                                    b = true;
                                }                                                            
                            }
                            
                            else if(stateLabels.length == 2) //{a,b}
                            {
                                //T & F = FALSE;
                                b = false;           
                                correctLabel = "{"+ap1+","+ap2+"}";
                           }
                        }
                        else if(ap1.contains("!") && !ap2.contains("!"))// !a imp b
                        {
                            if(stateLabels[0].equalsIgnoreCase("null")) //{}
                            {   // T & F = FALSE
                                b = false;    
                                correctLabel = "{"+ap1+","+ap2+"}";
                            }
                            else if(stateLabels.length == 1)
                            {
                               if(stateLabels[0].equalsIgnoreCase(ap2))  //{b}
                                {   // T & T = TRUE
                                    b = true;
                                }
                                else if (stateLabels[0].equalsIgnoreCase(ap1)) // {a}
                                {   // F & F = TRUE
                                    b = true;
                                }                                                                                           
                            }
                             
                            if(stateLabels[0].equalsIgnoreCase(ap2)) //{a,b}
                            {   //F & T = TRUE
                                    b = true;
                            }
                        }
                        
                        else //!a IMP !b
                        {
                            if(stateLabels[0].equalsIgnoreCase("null")) // {}
                            {   //T & T = TRUE
                                b = true;
                            }
                            
                            else if(stateLabels.length == 1)
                            {
                               if(stateLabels[0].equalsIgnoreCase(ap2))// {b} 
                                {   // T & F = FALSE
                                    b = false;
                                    correctLabel = "{"+ap1+","+ap2+"}";
                                }
                                else if (stateLabels[0].equalsIgnoreCase(ap1)) //{a}
                                {   // F & T = TRUE
                                    b = true;
                                }                                                            
                                
                            }
                            else if(stateLabels.length == 2) //{a,b}
                            {   //F & F = TRUE
                                b = true;
                            }
                        }
                        
                
                        if(!b) correctAP= correctLabel;
                    }

//               case "not":
//                   {
//                       String ap1 = postFix.pop();
//                       postFix.push("!"+ap1);
//                       break;
//                   }
               default: //if expression is an atomic proposition
                   if(!postFix.empty()){
                       String t = postFix.peek();
                       if(t.equals("or")||t.equals("and")||t.equals("imp")||t.equals("not")){
                           t = postFix.pop();
                           
                           postFix.push(top);
                           postFix.push(t);//push the operator on top of the stack 
                       }
                   }   break;
           }
           if(!b)
           {
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
       if(AP == "")
           AP = "null"; //indicates that state is empty
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
