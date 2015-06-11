package pl.edu.agh.groupcalendar.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents group in backend system.
 *
 * @author Bartosz
 *         Created on 2015-05-24.
 */
@Entity(name = "gc_groups")
public class Group implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 32452535L;

    /** Get group for given name. */
    public static final String GET_GROUP_BY_NAME =
            "SELECT g FROM gc_groups g WHERE g.gr_name = :name";

    @Id
    private int gr_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gr_admin")
    private User gr_admin;

    @OneToMany(mappedBy = "group")
    private transient List<Event> events;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "gc_us_gr",
            joinColumns = {@JoinColumn(name = "ug_gr_id", referencedColumnName = "gr_id")},
            inverseJoinColumns = {@JoinColumn(name = "ug_us_id", referencedColumnName = "us_id")}
    )
    private transient List<User> users = new ArrayList<>();

    private String gr_name;

    private String gr_desc;

    private Date gr_cdate = new Date();

    private Date gr_mdate;

    public Group() {
        super();
    }

    public List<Event> getEvents() {
        return events;
    }

    public boolean addUser(final User user) {
        return users.add(user);
    }

    public boolean removeUser(final User user) {
        return users.remove(user);
    }

    public boolean addEvent(final Event event) {
        return events.add(event);
    }

    public boolean removeEvent(final Event event) {
        return events.remove(event);
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getGr_id() {
        return gr_id;
    }

    public void setGr_id(int gr_id) {
        this.gr_id = gr_id;
    }

    public String getGr_name() {
        return gr_name;
    }

    public void setGr_name(String gr_name) {
        this.gr_name = gr_name;
    }

    public String getGr_desc() {
        return gr_desc;
    }

    public void setGr_desc(final String gr_desc) {
        this.gr_desc = gr_desc;
    }

    public User getGr_admin() {
        return gr_admin;
    }

    public void setGr_admin(User gc_admin) {
        this.gr_admin = gc_admin;
    }

    public Date getGr_cdate() {
        return gr_cdate;
    }

    public void setGr_cdate(Date gc_cdate) {
        this.gr_cdate = gc_cdate;
    }

    public Date getGr_mdate() {
        return gr_mdate;
    }

    public void setGr_mdate(Date gc_mdate) {
        this.gr_mdate = gc_mdate;
    }
}
