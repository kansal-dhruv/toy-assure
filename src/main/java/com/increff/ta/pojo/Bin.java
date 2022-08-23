package com.increff.ta.pojo;

import javax.persistence.*;

@Entity
@Table(name = "assure_bin")
public class Bin extends AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long binId;

    public Long getBinId() {
        return binId;
    }

    public void setBinId(Long binId) {
        this.binId = binId;
    }
}
