package com.openclassrooms.poseidon.domain;


import com.openclassrooms.poseidon.configuration.Generated;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Generated
@Entity
@Table(name="curves")
public class CurvePoint {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "must not be null")
    @Column(name="curveId")
    private Integer curveId;

    @UpdateTimestamp
    @Column(name="asOfDate")
    private Timestamp asOfDate;

    @NotNull(message = "must not be null")
    private Double term;

    @NotNull(message = "must not be null")
    private Double value;

    @CreationTimestamp
    @Column(name="creationDate")
    private Timestamp creationDate;


    public CurvePoint() {
    }

    public CurvePoint(Integer curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCurveId() {
        return curveId;
    }

    public void setCurveId(Integer curveId) {
        this.curveId = curveId;
    }

    public Timestamp getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Timestamp asOfDate) {
        this.asOfDate = asOfDate;
    }

    public Double getTerm() {
        return term;
    }

    public void setTerm(Double term) {
        this.term = term;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
