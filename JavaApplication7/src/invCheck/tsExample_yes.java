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
      State s0 = graph.addState("s0","A");
      State s1 = graph.addState("s1","A");
      State s2 = graph.addState("s2","B");
      State s3 = graph.addState("s3","A");
      State s4 = graph.addState("s4","A,B");
      State s5 = graph.addState("s5","B");
      State s6 = graph.addState("s6","B");
      State s7 = graph.addState("s7","A,B");
      State s8 = graph.addState("s8","A,B");
      State s9 = graph.addState("s9","A,B");
      State s10 = graph.addState("s10","A,B");
      State s11 = graph.addState("s11","B");
      State s12 = graph.addState("s12","B");
      State s13 = graph.addState("s13","A");
      State s14 = graph.addState("s14","B");
      
      graph.setInitState(s0);
      //G = gamma, A = alpha, B = beta
      graph.addTransitions(s0, s1, "");
      graph.addTransitions(s0, s2, "");
      graph.addTransitions(s1, s3, "");
      graph.addTransitions(s1, s5, "");
      graph.addTransitions(s2, s4, "");
      graph.addTransitions(s2, s6, "");
      graph.addTransitions(s3, s7, "");
      graph.addTransitions(s5, s7, "");
      graph.addTransitions(s4, s8, "");
      graph.addTransitions(s6, s8, "");
      graph.addTransitions(s7, s9, "");
      graph.addTransitions(s8, s10, "");
      graph.addTransitions(s8, s11, "");
      graph.addTransitions(s9, s13, "");
      graph.addTransitions(s9, s12, "");
      graph.addTransitions(s12, s14, "");
      graph.addTransitions(s11, s14, "");
      graph.addTransitions(s10, s14, "");
      
      System.out.println("Example Graph:\n" + graph);
      System.out.println("Performing depth-first search from D:");
      graph.invariantCheck("a or b");
   }
    
}
