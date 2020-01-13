package ch.zhaw.jv19.loganalyzer.view;

public interface UIPanelController {
    /** Determines, if panel is only visible to admin users.
     * Note: feature not implemented in MVP because of missing user login (out of scope)
     * @return false, if all users can see panel.
     */
    boolean isAdminPanel();
}
