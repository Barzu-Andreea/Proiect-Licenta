package com.example.azure.finalprojectazure.repositories;


import com.example.azure.finalprojectazure.models.Atractii;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttractionRepository extends JpaRepository<Atractii, Long> {
}
