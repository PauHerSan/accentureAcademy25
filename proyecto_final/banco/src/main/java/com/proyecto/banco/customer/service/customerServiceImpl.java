package com.proyecto.banco.customer.service;


import com.proyecto.banco.customer.model.customerModel;
import com.proyecto.banco.customer.repository.customerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class customerServiceImpl implements customerService {

    private final customerRepo customerRepository;

    @Override
    public customerModel createCustomer(customerModel customer) {
        log.debug("Creating customer with email: {}", customer.getEmail());

        // Validar que el email no exista
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + customer.getEmail());
        }

        // Establecer estado inicial
        customer.setStatus(customerModel.CustomerStatus.ACTIVE);

        customerModel savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with id: {}", savedCustomer.getId());
        return savedCustomer;
    }

    @Override
    @Transactional(readOnly = true)
    public customerModel getCustomerById(Long id) {
        log.debug("Getting customer by id: {}", id);
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<customerModel> getAllCustomers() {
        log.debug("Getting all customers");
        return customerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<customerModel> getCustomersByStatus(customerModel.CustomerStatus status) {
        log.debug("Getting customers by status: {}", status);
        return customerRepository.findByStatus(status);
    }

    @Override
    public customerModel updateCustomer(Long id, customerModel customer) {
        log.debug("Updating customer with id: {}", id);

        customerModel existingCustomer = getCustomerById(id);

        // Validar email único si cambió
        if (!existingCustomer.getEmail().equals(customer.getEmail())
                && customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + customer.getEmail());
        }

        // Actualizar campos
        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhone(customer.getPhone());

        customerModel updatedCustomer = customerRepository.save(existingCustomer);
        log.info("Customer updated successfully with id: {}", id);
        return updatedCustomer;
    }

    @Override
    public void deleteCustomer(Long id) {
        log.debug("Deleting customer with id: {}", id);

        customerModel customer = getCustomerById(id);

        // Soft delete: cambiar estado a INACTIVE
        customer.setStatus(customerModel.CustomerStatus.INACTIVE);
        customerRepository.save(customer);

        log.info("Customer soft deleted (deactivated) with id: {}", id);
    }

    @Override
    public customerModel activateCustomer(Long id) {
        log.debug("Activating customer with id: {}", id);

        customerModel customer = getCustomerById(id);
        customer.setStatus(customerModel.CustomerStatus.ACTIVE);

        customerModel activatedCustomer = customerRepository.save(customer);
        log.info("Customer activated successfully with id: {}", id);
        return activatedCustomer;
    }

    @Override
    public customerModel deactivateCustomer(Long id) {
        log.debug("Deactivating customer with id: {}", id);

        customerModel customer = getCustomerById(id);
        customer.setStatus(customerModel.CustomerStatus.INACTIVE);

        customerModel deactivatedCustomer = customerRepository.save(customer);
        log.info("Customer deactivated successfully with id: {}", id);
        return deactivatedCustomer;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

}
