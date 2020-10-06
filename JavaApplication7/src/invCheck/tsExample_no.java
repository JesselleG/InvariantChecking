/*          
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invCheck;

/**
 *
 * @author bernie
 */
public class tsExample_no {
    public static void main(String[] args) {
       
      TransitionSystem graph = new TransitionSystem();
      State s0 = graph.addState("s0","beta,gamma");
      State s1 = graph.addState("s1","");
      State s2 = graph.addState("s2","beta,gamma");
      State s3 = graph.addState("s3","beta,gamma");
      State s4 = graph.addState("s4","gamma");
      State s5 = graph.addState("s5","gamma");
      State s6 = graph.addState("s6","beta,gamma");
      State s7 = graph.addState("s7","gamma");
      State s8 = graph.addState("s8","beta,gamma");
      State s9 = graph.addState("s9","gamma");
      State s10 = graph.addState("s10","gamma");
      State s11 = graph.addState("s11","gamma");
      State s12 = graph.addState("s12","gamma");
      State s13 = graph.addState("s13","gamma");
      State s14 = graph.addState("s14","gamma");
      
      graph.setInitState(s0);
      
      System.out.println("Example Graph:\n" + graph);
      System.out.println("Performing depth-first search from D:");
      graph.invariantCheck("beta imp !gamma");
    }
     
}                 
