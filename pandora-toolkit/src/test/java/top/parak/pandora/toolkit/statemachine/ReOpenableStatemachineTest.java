package top.parak.pandora.toolkit.statemachine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.parak.pandora.exception.StatemachineException;
import top.parak.pandora.statemachine.AbstractStateMachine;
import top.parak.pandora.statemachine.ReOpenableStatemachine;

public class ReOpenableStatemachineTest {

    private ReOpenableStatemachine statemachine;

    @Before
    public void before() {
        statemachine = new ReOpenableStatemachine();
    }

    @Test
    public void init() {
        Assert.assertEquals(AbstractStateMachine.STATE_INITIAL, statemachine.state());
    }

    @Test
    public void open() {
        statemachine.transferToOpened();
        Assert.assertEquals(ReOpenableStatemachine.STATE_OPENED, statemachine.state());
    }

    @Test
    public void openTwice() {
        statemachine.transferToOpened();
        Assert.assertEquals(ReOpenableStatemachine.STATE_OPENED, statemachine.state());
        statemachine.transferToOpened();
        Assert.assertEquals(ReOpenableStatemachine.STATE_OPENED, statemachine.state());
    }

    @Test
    public void close() {
        statemachine.transferToOpened();
        Assert.assertEquals(ReOpenableStatemachine.STATE_OPENED, statemachine.state());
        statemachine.transferToClosed();
        Assert.assertEquals(ReOpenableStatemachine.STATE_CLOSED, statemachine.state());
    }

    @Test(expected = StatemachineException.class)
    public void closeBeforeOpen() {
        statemachine.transferToClosed();
    }

    @Test
    public void isOpened() {
        Assert.assertFalse(statemachine.isOpened());
        statemachine.transferToOpened();
        Assert.assertTrue(statemachine.isOpened());
    }

}