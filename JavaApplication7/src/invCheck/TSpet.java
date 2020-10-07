/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invCheck;

/**
 *
 * @author dqp6065,jbr9093
 */
public class TSpet {
    int noProcess;
    private String wait = "WAIT";
    private String crit = "CRIT";
    private String noncrit = "NC";
    public static void main(String[] args){
        TSpet tsPet = new TSpet();
        TransitionSystem ts = tsPet.createTSpet(2);
        System.out.println(ts);
        ts.invariantCheck("not crit1 and not crit2");
    }
    
    public TSpet(){    }
    
    public TransitionSystem createTSpet(int noProcess){
        this.noProcess =  noProcess;
        TransitionSystem tsPet = new TransitionSystem();
        
        //make all the states
      State s0 = tsPet.addState("nc1nc2","");
      State s1 = tsPet.addState("wait1nc2","");
      State s2 = tsPet.addState("nc1wait2","");
      State s3 = tsPet.addState("crit1nc2","");
      State s4 = tsPet.addState("wait1wait2","");
      State s5 = tsPet.addState("nc1crti2","CRIT2");
      State s6 = tsPet.addState("crit1wait2","CRIT1");
      State s7 = tsPet.addState("wait1crit2","CRIT2");
      State s8 = tsPet.addState("crit1crit2","CRIT1,CRIT2");
      
      tsPet.setInitState(s0);
      
        //G = gamma, A = alpha, B = beta
      tsPet.addTransitions(s0, s1, "");
      tsPet.addTransitions(s0, s2, "");
      tsPet.addTransitions(s1, s3, "");
      tsPet.addTransitions(s1, s4, "");
      tsPet.addTransitions(s2, s4, "");
      tsPet.addTransitions(s2, s5, "");
      tsPet.addTransitions(s3, s6, "");
      tsPet.addTransitions(s4, s6, "");
      tsPet.addTransitions(s4, s7, "");
      tsPet.addTransitions(s5, s7, "");
      tsPet.addTransitions(s6, s8, "");
      tsPet.addTransitions(s7, s8, "");
      
        return tsPet;
    }
}
