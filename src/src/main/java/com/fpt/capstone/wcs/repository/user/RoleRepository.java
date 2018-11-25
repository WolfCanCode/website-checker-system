package com.fpt.capstone.wcs.repository.user;

import com.fpt.capstone.wcs.model.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findAllByName(String name);
}
