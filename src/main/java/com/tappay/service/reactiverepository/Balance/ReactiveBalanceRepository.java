package com.tappay.service.reactiverepository.Balance;

import com.tappay.service.webservice.Balance.Model.UserBalance;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ReactiveBalanceRepository extends ReactiveCrudRepository<UserBalance, String> {

    @Query("SELECT * FROM user_balance WHERE uid = :uid")
    Mono<UserBalance> findBalance(int uid);

    @Query("UPDATE user_balance SET balance=balance + :amount WHERE uid = :uid")
    Mono<UserBalance> updateBalance(int uid,BigDecimal amount);

    @Query("UPDATE user_balance SET balance=balance - :amount WHERE uid = :uid")
    Mono<UserBalance> deductBalance(int uid,BigDecimal amount);


}
