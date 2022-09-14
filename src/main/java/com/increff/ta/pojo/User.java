package com.increff.ta.pojo;

import com.increff.ta.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "assure_user",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"name", "type"})})
@Getter
@Setter
public class User extends AbstractModel {

  //TODO hiberntae naming steategy
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String name;

  @NotNull
  @Enumerated(EnumType.STRING)
  private UserType type;
}