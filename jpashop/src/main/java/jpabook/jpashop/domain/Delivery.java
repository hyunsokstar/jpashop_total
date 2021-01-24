package jpabook.jpashop.domain;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.DeliveryStatus;
import jpabook.jpashop.domain.Order;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;


@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //ENUM [READY(준비), COMP(배송)]

}
