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
import java.util.Set;

public interface Transition {
       //returns states as a array
       public State[] endStates();
       public State oppositeStates(State state);
}
