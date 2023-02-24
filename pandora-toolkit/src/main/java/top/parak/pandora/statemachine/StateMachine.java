package top.parak.pandora.statemachine;

import top.parak.pandora.exception.StatemachineException;

import java.util.List;

/**
 * Thread-safe stateMachine.
 *
 * @author Khighness
 * @since 2023-02-24
 */
public interface StateMachine {

    /**
     * Get the state of statemachine.
     *
     * @return the state of statemachine.
     */
    int state();

    /**
     * Transfer statemachine's state to the target state.
     *
     * @param allowedStates the allowed state list
     * @param targetState   the target state
     */
    void transfer(List<Integer> allowedStates, int targetState) throws StatemachineException;

}
