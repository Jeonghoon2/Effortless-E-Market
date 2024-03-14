package com.effortless.effortlessmarket.domain.category.entity;

import com.effortless.effortlessmarket.global.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name",length = 30, nullable = false)
    private String name;

    @Column(name = "category_depth", nullable = false)
    private Integer depth = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Category> children = new ArrayList<>();

    public Category() {
    }

    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
        this.depth = (parent==null) ? 0 : parent.getDepth() + 1;
    }
}
