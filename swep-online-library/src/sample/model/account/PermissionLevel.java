package sample.model.account;

/**
 * Represents the permission level of a user.
 */
public enum PermissionLevel {

    GUEST(0), USER(1), MODERATOR(2), ADMINISTRATOR(3);

    private int permissionID;

    public int getPermissionID() {
        return permissionID;
    }

    PermissionLevel(int permissionID) {
        this.permissionID = permissionID;
    }

}