package com.namduong.viettel.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {
    @Id
    private String id;
    @Column(name = "username")
    private String username;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "email")
    private String email;
    @Column(name = "first_name")
    private String firstname;
    @Column(name = "last_name")
    private String lastname;
    @Column(name = "created_at")
    @JsonIgnore
    private LocalDateTime createdAt;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id")
    @JsonIgnore
    private Ticket ticket;
    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE}
    )
    @JoinTable(name = "conversation",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "agent_id") })
    List<Agent> agents;

    public User(String id, String username, boolean enabled, String email, String firstname, String lastname, LocalDateTime createdAt, String avatarUrl, Ticket ticket) {
        this.id = id;
        this.username = username;
        this.enabled = enabled;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.createdAt = createdAt;
        this.avatarUrl = avatarUrl;
        this.ticket = ticket;
    }

    public User(String id, String username, boolean enabled, String email, String firstname, String lastname, LocalDateTime createdAt, String avatarUrl) {
        this.id = id;
        this.username = username;
        this.enabled = enabled;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.createdAt = createdAt;
        this.avatarUrl = avatarUrl;
    }
    public void addAgent(Agent agent)
    {
        if(agents == null) agents = new ArrayList<>();
        Integer idx = agents.indexOf(agent);
        if(idx == -1) {
            agents.add(agent);
            agent.getUsers().add(this);
        }
    }
    public void removeAgent(String agentId)
    {
        Agent agent = agents.stream().filter(t -> t.getId().equals(agentId)).findFirst().orElse(null);
        if(agent != null)
        {
            agents.remove(agent);
            agent.getUsers().remove(this);
        }
    }

}
