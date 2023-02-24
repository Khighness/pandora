package top.parak.pandora.statemachine;

import top.parak.pandora.exception.StatemachineException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Re-openable statemachine.
 *
 * @author Khighness
 * @since 2023-02-24
 */
public class ReOpenableStatemachine extends AbstractStateMachine {

    public static final int STATE_OPENED  = 1;
    public static final int STATE_CLOSED  = 2;

    private static final List<Integer> ALLOWED_OPENED_STATES = Arrays.asList(STATE_INITIAL, STATE_OPENED, STATE_CLOSED);
    private static final List<Integer> ALLOWED_CLOSED_STATES = Collections.singletonList(STATE_OPENED);

    protected void transferToOpened() {
        transfer(ALLOWED_OPENED_STATES, STATE_OPENED);
    }

    protected void transferToClosed() {
        transfer(ALLOWED_CLOSED_STATES, STATE_CLOSED);
    }

    protected boolean isOpened() {
        return state() == STATE_OPENED;
    }

}
