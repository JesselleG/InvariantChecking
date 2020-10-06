package invCheck;


import invCheck.State;
import invCheck.TransitionSystem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dqp6065,jbr9093
 * 
 */
public class tsExample_yes {
   public static void main(String[] args)
   {  TransitionSystem graph = new TransitionSystem();
      State s0 = graph.addState("5,p1","");
      State s1 = graph.addState("4,p2","");
      State s2 = graph.addState("3,p2","");
      State s3 = graph.addState("3,p1","");
      State s4 = graph.addState("2,p1","");
      State s5 = graph.addState("2,p1","");
      State s6 = graph.addState("1,p1","");
      State s7 = graph.addState("2,p2","");
      State s8 = graph.addState("1,p2","");
      State s9 = graph.addState("1,p2","");
      State s10 = graph.addState("0,p2","win1");
      State s11 = graph.addState("1,p2","");
      State s12 = graph.addState("0,p2","win1");
      State s13 = graph.addState("0,p2","win1");
      State s14 = graph.addState("1,p1","");
      State s15 = graph.addState("0,p1","win2");
      State s16 = graph.addState("0,p1","win2");
      State s17 = graph.addState("0,p1","win2");
      State s18 = graph.addState("0,p1","win2");
      State s19 = graph.addState("0,p2","win1");
      
      graph.setInitState(s0);
      //G = gamma, A = alpha, B = beta
      graph.addTransitions(s0, s1, "rm1,na");
      graph.addTransitions(s0, s2, "rm2,na");
      graph.addTransitions(s1, s3, "na,rm1");
      graph.addTransitions(s1, s4, "na,rm2");
      graph.addTransitions(s2, s5, "na,rm1");
      graph.addTransitions(s2, s6, "na,rm2");
      graph.addTransitions(s3, s7, "rm1,na");
      graph.addTransitions(s3, s8, "rm2,na");
      graph.addTransitions(s4, s9, "rm1,na");
      graph.addTransitions(s4, s10, "rm2,na");
      graph.addTransitions(s5, s11, "rm1,na");
      graph.addTransitions(s5, s12, "rm2,na");
      graph.addTransitions(s6, s13, "rm1,na");
      graph.addTransitions(s7, s14, "na,rm1");
      graph.addTransitions(s7, s15, "na,rm2");
      graph.addTransitions(s8, s16, "na,rm1");
      graph.addTransitions(s9, s10, "na,rm1");
      graph.addTransitions(s11, s18, "na,rm1");
      graph.addTransitions(s14, s19, "rm1,na");
      
      System.out.println("Example Graph:\n" + graph);
      System.out.println("Performing depth-first search from D:");
      graph.invariantCheck("win1");
   }
    
}
