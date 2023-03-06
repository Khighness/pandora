package top.parak.pandora.exception;

import top.parak.pandora.statemachine.StateMachine;

/**
 * Thrown when {@link StateMachine} transfer from a invalid state to the target state.
 *
 * @author Khighness
 * @since 2023-02-24
 */
public class StatemachineException extends RuntimeException {

    public StatemachineException(String message) {
        super(message);
    }

    public StatemachineException(String message, Throwable cause) {
        super(message, cause);
    }

}
