package pl.edu.agh.groupcalendar.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents user in backend system.
 *
 * @author Bartosz
 *         Created on 2015-05-23.
 */
@Entity(name = "gc_users")
public class User implements Serializable {

    /** serialVersionUID. */
    private final static long serialVersionUID = 1435232L;

    /** Get user for given username. */
    public static final String GET_USER_BY_USERNAME =
            "SELECT u FROM gc_users u WHERE u.us_username = :username";

    /** Get user for given username or email. */
    public static final String GET_USER_BY_USERNAME_OR_EMAIL =
            "SELECT u FROM gc_users u WHERE u.us_username = :username OR u.us_email = :email";

    @Id
    private int us_id;

    @OneToMany(mappedBy = "gc_admin", fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private transient List<Group> administeredGroups = new ArrayList<>();

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private transient List<Group> groups = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private transient List<Session> sessions = new ArrayList<>();

    private String us_username;

    private String us_name;

    private String us_surname;

    private String us_password;

    private String us_salt = "";

    private String us_email;

    private boolean us_email_notif = true;

    private boolean us_active = true;

    private int us_login_fails = 0;

    private Date us_cdate = new Date();

    private Date us_mdate;

    public User() {
        super();
    }

    public boolean addSession(final Session session) {
        return sessions.add(session);
    }

    public boolean removeSession(final Object o) {
        return sessions.remove(o);
    }

    public boolean addAdministeredGroup(final Group group) {
        return administeredGroups.add(group);
    }

    public boolean removeAdministeredGroup(final Group o) {
        return administeredGroups.remove(o);
    }

    public boolean addGroup(final Group group) {
        return groups.add(group);
    }

    public boolean removeGroup(final Group o) {
        return groups.remove(o);
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(final List<Session> sessions) {
        this.sessions = sessions;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getAdministeredGroups() {
        return administeredGroups;
    }

    public void setAdministeredGroups(List<Group> administeredGroups) {
        this.administeredGroups = administeredGroups;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUs_username() {
        return us_username;
    }

    public void setUs_username(String us_username) {
        this.us_username = us_username;
    }

    public int getUs_id() {
        return us_id;
    }

    public void setUs_id(int us_id) {
        this.us_id = us_id;
    }

    public String getUs_name() {
        return us_name;
    }

    public void setUs_name(String us_name) {
        this.us_name = us_name;
    }

    public String getUs_surname() {
        return us_surname;
    }

    public void setUs_surname(String us_surname) {
        this.us_surname = us_surname;
    }

    public String getUs_password() {
        return us_password;
    }

    public void setUs_password(String us_password) {
        this.us_password = us_password;
    }

    public String getUs_salt() {
        return us_salt;
    }

    public void setUs_salt(String us_salt) {
        this.us_salt = us_salt;
    }

    public String getUs_email() {
        return us_email;
    }

    public void setUs_email(String us_email) {
        this.us_email = us_email;
    }

    public boolean isUs_email_notif() {
        return us_email_notif;
    }

    public void setUs_email_notif(boolean us_email_notif) {
        this.us_email_notif = us_email_notif;
    }

    public boolean isUs_active() {
        return us_active;
    }

    public void setUs_active(boolean us_active) {
        this.us_active = us_active;
    }

    public int getUs_login_fails() {
        return us_login_fails;
    }

    public void setUs_login_fails(int us_login_fails) {
        this.us_login_fails = us_login_fails;
    }

    public Date getUs_cdate() {
        return us_cdate;
    }

    public void setUs_cdate(Date us_cdate) {
        this.us_cdate = us_cdate;
    }

    public Date getUs_mdate() {
        return us_mdate;
    }

    public void setUs_mdate(Date us_mdate) {
        this.us_mdate = us_mdate;
    }

    @Override
    public String toString() {
        return "Id: " + us_id + " Name: " + us_name + " Username: " + us_username + " Email: " + us_email;
    }
}
