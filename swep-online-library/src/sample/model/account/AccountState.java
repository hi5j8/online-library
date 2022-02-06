package sample.model.account;

/**
 * Represents the state of an account with an integer and vice versa.
 *
 */
public enum AccountState {

    ACTIVE(5), MARKED_FOR_DELETION(6), FROZEN(7);

    private int stateInt;

    public int getStateID() {
        return stateInt;
    }

    AccountState(int stateInt) {
        this.stateInt = stateInt;
    }

}