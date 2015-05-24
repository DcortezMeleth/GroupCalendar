package pl.edu.agh.groupcalendar.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Bartosz
 *         Created on 2015-05-24.
 */
@Entity(name = "gc_sessions")
public class Session implements Serializable {

    private static final long serialVersionUID = 8790432890L;

    @Id
    private int ss_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ss_us_id")
    private User user;

    private Date ss_last_action;

    public Session() {
        super();
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
