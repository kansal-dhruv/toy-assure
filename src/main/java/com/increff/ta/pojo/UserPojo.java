package com.increff.ta.pojo;

import com.increff.ta.commons.model.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "type"})})
@Getter
@Setter
public class UserPojo extends AbstractPojo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String name;

  @NotNull
  @Enumerated(EnumType.STRING)
  private UserType type;
}