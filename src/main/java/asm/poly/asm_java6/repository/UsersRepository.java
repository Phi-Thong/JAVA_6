package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.enity.users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<users, Long> {
    users findByEmail(String email);
}