package com.example.azure.finalprojectazure.repositories;


import com.example.azure.finalprojectazure.models.Postcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostcardRepository extends JpaRepository<Postcard, Long> {
}
