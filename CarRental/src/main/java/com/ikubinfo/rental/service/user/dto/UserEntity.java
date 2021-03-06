package com.ikubinfo.rental.service.user.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ikubinfo.rental.service.role.dto.RoleEntity;
import lombok.Data;

@Entity
@Table(name = "user", schema = "rental")
@Data
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	private LocalDateTime birthdate;

	@Column(name = "address")
	private String address;

	@Column(name = "contact_number")
	private String phone;

	@Column(name = "email")
	private String email;

	@Column(name = "role_id")
	private int roleId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
	private RoleEntity role;

	@Column(name = "active")
	private boolean active;
}
