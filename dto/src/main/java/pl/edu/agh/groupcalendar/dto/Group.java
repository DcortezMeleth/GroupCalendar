package pl.edu.agh.groupcalendar.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Bartosz
 *         Created on 2015-05-24.
 */
@Entity(name = "gc_groups")
public class Group implements Serializable {

    private static final long serialVersionUID = 32452535L;

    @Id
    private int gr_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gc_admin")
    private User gc_admin;

    @OneToMany(mappedBy = "group")
    private transient List<Event> events;

    @ManyToMany
    @JoinTable(
            name = "gc_us_gr",
            joinColumns = {@JoinColumn(name = "ug_gr_id", referencedColumnName = "gr_id")},
            inverseJoinColumns = {@JoinColumn(name = "ug_us_id", referencedColumnName = "us_id")}
    )
    private transient List<User> users;

    private String gr_name;

    private Date gc_cdate = new Date();

    private Date gc_mdate;

    public Group() {
        super();
    }

    public List<Event> getEvents() {
        return events;
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

    public User getGc_admin() {
        return gc_admin;
    }

    public void setGc_admin(User gc_admin) {
        this.gc_admin = gc_admin;
    }

    public Date getGc_cdate() {
        return gc_cdate;
    }

    public void setGc_cdate(Date gc_cdate) {
        this.gc_cdate = gc_cdate;
    }

    public Date getGc_mdate() {
        return gc_mdate;
    }

    public void setGc_mdate(Date gc_mdate) {
        this.gc_mdate = gc_mdate;
    }
}
