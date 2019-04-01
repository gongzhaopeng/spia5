package com.atzu68.spia5.tacocloud.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Taco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @NotNull
    @Size(min = 5,
            message = "Name must be at least 5 characters long")
    private String name;

    private Date createdAt;

    @Size(min = 1,
            message = "You must choose at least 1 ingredient")
    @ManyToMany(targetEntity = Ingredient.class)
    private List<Ingredient> ingredients = new ArrayList<>();

    @PrePersist
    void createdAt() {
        createdAt = new Date();
    }
}
