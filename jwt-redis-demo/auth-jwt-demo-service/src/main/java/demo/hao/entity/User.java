package demo.hao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {
    @GeneratedValue @Id
    private Long id;
    private String username;
    private String password;
}
