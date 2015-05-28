package pl.edu.agh.groupcalendar.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents active user session.
 *
 * @author Bartosz
 *         Created on 2015-05-24.
 */
@Entity(name = "gc_sessions")
public class Session implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 8790432890L;

    /** Get session for given user and sessionKey. */
    public static final String GET_SESSION_BY_USER_ID_AND_SESSION_KEY =
            "SELECT s FROM gc_sessions s WHERE s.user = :user and s.ss_key = :sessionKey";

    /** Get session for given ss_key. */
    public static final String GET_SESSION_BY_SESSION_KEY = "SELECT s FROM gc_sessions s WHERE s.ss_key = :ss_key";

    /** Deletes session for given user_id. */
    public static final String DELETE_USER_SESSION = "DELETE s FROM gc_sessions s WHERE s.ss_us_id = :us_id";

    @Id
    private int ss_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ss_us_id")
    private User user;

    private String ss_key;

    private Date ss_last_action;

    public Session() {
        super();
    }

    public String getSs_key() {
        return ss_key;
    }

    public void setSs_key(String ss_key) {
        this.ss_key = ss_key;
    }

    public int getSs_id() {
        return ss_id;
    }

    public void setSs_id(int ss_id) {
        this.ss_id = ss_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getSs_last_action() {
        return ss_last_action;
    }

    public void setSs_last_action(Date ss_last_action) {
        this.ss_last_action = ss_last_action;
    }
}
