package com.coderintuition.CoderIntuition.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "test_run")
@Getter
@Setter
public class TestRun {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column(name = "token")
    private String token;

    @Column(name = "language")
    private String language;

    @Column(name = "code", columnDefinition = "TEXT")
    private String code;

    @Column(name = "input", columnDefinition = "TEXT")
    private String input;

    @Column(name = "status")
    private String status;

    @Column(name = "expected_output", columnDefinition = "TEXT")
    private String expectedOutput;

    @Column(name = "output", columnDefinition = "TEXT")
    private String output;

    @Column(name = "stdout", columnDefinition = "TEXT")
    private String stdout;

    @Column(name = "stderr", columnDefinition = "TEXT")
    private String stderr;

}
