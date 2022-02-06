package sample.model.account.user;

import sample.model.media.Media;

import java.sql.Date;

/**
 * Saves all information about a borrowed item, including the item and who borrowed it, as well as additional
 * information.
 *
 * Note: The functionality for this is currently not implemented.
 */
public class Borrow {

    private int id;
    private Media item;
    private User user;
    private Date borrowedSince;
    private Date due;
    private boolean overdue;

    /**
     * A borrow constructor for newly created borrows.
     *
     * This constructor should be used when adding new borrows to the database, as database-based values such as
     * the id, borrow date and the due date based on it have not been assigned by the database yet. These values will
     * be created upon database insertion. To retrieve entries from the database, the advanced constructor should be
     * used instead.
     *
     * @param item The borrowed item.
     * @param user The borrowing user.
     */
    public Borrow(Media item, User user) {

        this.id = -1;
        this.item = item;
        this.user = user;
        this.borrowedSince = null;
        this.due = null;
        this.overdue = false;

    }

    /**
     * The full constructor of a borrow.
     *
     * This constructor should be used when retrieving data from the database, as database-dependent values have
     * been assigned and exist within the database and can be set properly.
     *
     * @param id The borrow id.
     * @param item The borrowed item.
     * @param user The borrowing user.
     * @param borrowedSince The date since the borrow has been active.
     * @param due The due date of the borrow.
     * @param overdue The state of an item being borrowed longer than initially set.
     */
    public Borrow(int id, Media item, User user, Date borrowedSince, Date due, boolean overdue) {

        this.id = id;
        this.item = item;
        this.user = user;
        this.borrowedSince = borrowedSince;
        this.due = due;
        this.overdue = overdue;

    }

    // Getter and setter methods for object attributes

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Media getItem() {
        return item;
    }

    public void setItem(Media item) {
        this.item = item;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getBorrowedSince() {
        return borrowedSince;
    }

    public void setBorrowedSince(Date borrowedSince) {
        this.borrowedSince = borrowedSince;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

}
