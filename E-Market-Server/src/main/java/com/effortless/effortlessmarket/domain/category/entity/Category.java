package com.effortless.effortlessmarket.domain.category.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","name"})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long id;

    @Column(name = "c_name")
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_p_id")
    private Category parent;

    @Column(name = "c_depth")
    private Long depth;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();


    public Category(String name){
        this.name = name;
        this.depth = 0L;
    }

    public Category(String name, Long depth){
        this.name = name;
        this.depth = depth;
    }

    public void addParent(Category category){
        this.parent = category;
        category.child.add(this);
    }


}
