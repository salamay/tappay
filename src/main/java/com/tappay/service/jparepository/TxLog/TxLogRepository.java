package com.tappay.service.jparepository.TxLog;

import com.tappay.service.webservice.TxLog.TxLog;
import org.springframework.data.repository.CrudRepository;

public interface TxLogRepository extends CrudRepository<TxLog,String> {
}
