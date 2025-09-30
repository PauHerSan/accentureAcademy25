package com.proyecto.banco.customer.repository;

import com.proyecto.banco.customer.model.customerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface customerRepo extends JpaRepository<customerModel, Long>  {

    // Buscar cliente por email
    Optional<customerModel> findByEmail(String email);

    // Verificar si existe un email
    boolean existsByEmail(String email);

    // Buscar clientes por estado
    List<customerModel> findByStatus(customerModel.CustomerStatus status);

    // Buscar clientes por nombre (búsqueda parcial, case-insensitive)
    List<customerModel> findByNameContainingIgnoreCase(String name);

}
