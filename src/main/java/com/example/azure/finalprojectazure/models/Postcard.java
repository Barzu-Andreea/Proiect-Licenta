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
public class Postcard {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String imageUrl;
    @Column(columnDefinition = "TEXT")
    private String message;

}