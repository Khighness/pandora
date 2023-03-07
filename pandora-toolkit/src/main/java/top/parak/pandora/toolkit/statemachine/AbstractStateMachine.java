package top.parak.pandora.toolkit.statemachine;

import top.parak.pandora.toolkit.exception.StatemachineException;

import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Abstract statemachine.
 *
 * @author Khighness
 * @since 2023-02-24
 */
public abstract class AbstractStateMachine implements StateMachine {

    public static final int STATE_INITIAL = 0;

    private volatile int state = STATE_INITIAL;
    private static final AtomicIntegerFieldUpdater<AbstractStateMachine> STATE_UPDATER
            = AtomicIntegerFieldUpdater.newUpdater(AbstractStateMachine.class, "state");

    @Override
    public int state() {
        return state;
    }

    @Override
    public void transfer(List<Integer> allowedStates, int targetState) throws StatemachineException {
        if (!allowedStates.contains(state)) {
            throw new StatemachineException("Invalid state [" + state +
                    "] to transfer to target state: " + targetState);
        }
        STATE_UPDATER.compareAndSet(this, state, targetState);
    }

}
