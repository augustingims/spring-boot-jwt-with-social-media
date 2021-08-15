package cm.skysoft.app.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "product_visit_note")
public class ProductVisitNote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Products products;

    @ManyToOne(optional = false)
    private VisitNote visitNote;

    public ProductVisitNote() {
    }

    public ProductVisitNote(Products products, VisitNote visitNote) {
        this.products = products;
        this.visitNote = visitNote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public VisitNote getVisitNote() {
        return visitNote;
    }

    public void setVisitNote(VisitNote visitNote) {
        this.visitNote = visitNote;
    }
}
