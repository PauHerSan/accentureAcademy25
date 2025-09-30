package com.proyecto.banco.customer.service;


import com.proyecto.banco.customer.model.customerModel;

import java.util.List;

public interface customerService {

    customerModel createCustomer(customerModel customer);
    customerModel getCustomerById(Long id);
    List<customerModel> getAllCustomers();
    List<customerModel> getCustomersByStatus(customerModel.CustomerStatus status);
    customerModel updateCustomer(Long id, customerModel customer);
    void deleteCustomer(Long id);
    customerModel activateCustomer(Long id);
    customerModel deactivateCustomer(Long id);
    boolean existsByEmail(String email);

}
