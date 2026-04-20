package org.e2e.labe2e01.passenger.infrastructure;

import jakarta.transaction.Transactional;
import org.e2e.labe2e01.user.infrastructure.BaseUserRepository;

@Transactional
public interface PassengerRepository extends BaseUserRepository<> {
}