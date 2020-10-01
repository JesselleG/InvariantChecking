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

public interface State {
       public String getState();
       public Set<Transition> getTransitions();
       public Set<State> getConnectingStates();
       public boolean isAdjacent(State s);
       public String toString();
}
