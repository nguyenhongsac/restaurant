package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	/*
	 * User: @Entity
	 * :user_name: query parameter
	 */
//	@Query("SELECT u FROM User u WHERE u.user_name = :username")
	Optional<User> findByName(String username);

	boolean existsByName(String name);
	List<User> findAllByRole(String role);
}
