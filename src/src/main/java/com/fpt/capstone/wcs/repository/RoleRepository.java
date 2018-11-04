package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.Role;
import com.fpt.capstone.wcs.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}
