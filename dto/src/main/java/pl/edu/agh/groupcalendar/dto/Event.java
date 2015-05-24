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
@Entity(name = "gc_events")
public class Event implements Serializable {

    private static final long serialVersionUID = 235932589L;

    @Id
    private int ev_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gc_us_id")
    private transient Group group;

    private String ev_name;

    private String ev_desc;

    private Date ev_date;

    private String ev_place;

    private Date ev_cdate = new Date();

    private Date ev_mdate;

    public Event() {
        super();
    }

    public int getEv_id() {
        return ev_id;
    }

    public void setEv_id(int ev_id) {
        this.ev_id = ev_id;
    }

    public String getEv_name() {
        return ev_name;
    }

    public void setEv_name(String ev_name) {
        this.ev_name = ev_name;
    }

    public String getEv_desc() {
        return ev_desc;
    }

    public void setEv_desc(String ev_desc) {
        this.ev_desc = ev_desc;
    }

    public Date getEv_date() {
        return ev_date;
    }

    public void setEv_date(Date ev_date) {
        this.ev_date = ev_date;
    }

    public String getEv_place() {
        return ev_place;
    }

    public void setEv_place(String ev_place) {
        this.ev_place = ev_place;
    }

    public Date getEv_cdate() {
        return ev_cdate;
    }

    public void setEv_cdate(Date ev_cdate) {
        this.ev_cdate = ev_cdate;
    }

    public Date getEv_mdate() {
        return ev_mdate;
    }

    public void setEv_mdate(Date ev_mdate) {
        this.ev_mdate = ev_mdate;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
