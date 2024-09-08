package com.tappay.service.reactiverepository.Transaction;

import com.tappay.service.webservice.Transaction.model.UserTransaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReactiveTxRepository extends ReactiveCrudRepository<UserTransaction, String> {
}
