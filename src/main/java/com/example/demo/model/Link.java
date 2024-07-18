package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "links")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2000)
    private String bigUrl;
    private String url;
    private String password;
    private LocalDate timeDelete = LocalDate.now()
                                            .plusDays(1);

    @Override
    public String toString() {
        return "\nLink{" + "id=" + id + ", bigUrl='" + bigUrl + '\'' + ", url='" + url + '\'' + ", password='" + password + '\'' + ", timeDelete=" + timeDelete + '}';
    }
}
