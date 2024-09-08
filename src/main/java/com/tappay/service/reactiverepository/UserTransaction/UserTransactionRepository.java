package com.tappay.service.reactiverepository.UserTransaction;

import com.tappay.service.reactiverepository.Balance.ReactiveBalanceRepository;
import com.tappay.service.webservice.Transaction.model.UserTransaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserTransactionRepository extends ReactiveCrudRepository<UserTransaction, String> {

}
