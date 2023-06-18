package com.example.azure.finalprojectazure.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Atractii {
    @Id
    @GeneratedValue
    private Long id;
    private String imageUrl;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private double latitude;
    private double longitude;
}

