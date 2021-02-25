package ru.max.securitytask.springsecjwttask.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.max.securitytask.springsecjwttask.model.entity.Payment;
import ru.max.securitytask.springsecjwttask.model.entity.UserEntity;
import ru.max.securitytask.springsecjwttask.model.exception.NoMoneyForPaymentException;
import ru.max.securitytask.springsecjwttask.repository.PaymentRepository;
import ru.max.securitytask.springsecjwttask.repository.UserRepository;

import java.time.LocalDateTime;

/**
 * Created by maxxii on 22.02.2021.
 */
@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PaymentRepository paymentRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.paymentRepository = paymentRepository;

    }

    @Override
    public UserEntity getUserByLoginAndPass(String login, String password) {
        UserEntity userEntity = userRepository.findByLogin(login);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        throw new UsernameNotFoundException("Invalid name or password");
    }

    @Override
    public UserEntity getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setBalance(8.0);
        return userRepository.save(user);
    }

    @Override
    public boolean isExistLogin(String login) {
        UserEntity user = userRepository.findByLogin(login);
        return (user != null && user.getLogin().equals(login));
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void pay(String login, Double payment) {
        UserEntity user = userRepository.findByLogin(login);
        if (user!=null && user.getBalance().compareTo(payment) > 0){
            user.setBalance(user.getBalance()-payment);
            userRepository.save(user);
            Payment payObj = new Payment();
            payObj.setAmount(payment);
            payObj.setUser(user);
            payObj.setOperationDate(LocalDateTime.now());
            System.out.println(LocalDateTime.now());
            paymentRepository.save(payObj);
        }else {
            throw new NoMoneyForPaymentException("Not enough cash to pay");
        }
    }
}
