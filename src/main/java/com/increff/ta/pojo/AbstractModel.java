package com.increff.ta.pojo;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.Date;

@MappedSuperclass
public class AbstractModel {

    //TODO indexes on non unique constraints like createdOn

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    protected Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    protected Date updatedOn;

    @Version
    protected Long version;

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}