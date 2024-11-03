// com/capstone/wizshop_admin_webservice/repositories/UserRepository.java

package com.capstone.wizshop_admin_webservice.repositories;

import com.capstone.wizshop_admin_webservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
