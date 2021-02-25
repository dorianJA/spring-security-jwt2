package ru.max.securitytask.springsecjwttask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.max.securitytask.springsecjwttask.model.entity.Payment;

/**
 * Created by maxxii on 23.02.2021.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
