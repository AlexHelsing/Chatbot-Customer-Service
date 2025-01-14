package com.CustomerChatbot.Chatbot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Arrays;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    @Getter
    private UUID orderId;

    @Setter
    @Getter
    private UUID customerId;

    @Setter
    @Getter
    private String customerName;

    @Setter
    @Getter
    private String orderStatus;

    @Setter
    @Getter
    private java.sql.Date orderDate;

    @Setter
    @Getter
    private java.sql.Time orderTime;

    @Column(columnDefinition = "vector(768)")
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 768)
    @Getter
    private float[] embedding;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @Setter
    @Getter
    private User user;

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderDate=" + orderDate +
                ", orderTime=" + orderTime +
                ", embedding=" + Arrays.toString(embedding) +
                ", user=" + (user != null ? user.getId() : "null") +
                '}';
    }
}
