package jpabook.jpashop.service;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);
        // 배송 정보 생성 하기
        Delivery delivery = new Delivery();

        System.out.println("delivery :::::::::::::::::::::::::::::" + delivery);
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 주문 상품 정보 생성 하기
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        // 주문 정보 생성 하기
        Order order = Order.createOrder(member, delivery, orderItem);
        // 주문 정보 영속화 하기
        orderRepository.save(order);
        return order.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 정보 찾기
        Order order = orderRepository.findOne(orderId);
        System.out.println("cancelOrder ::::::::::::::::::::::::::::::::: "+ order);
        // 주문 취소
        order.cancel();
    }

    // 주문 조회
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }

    // 주문 취소 하기
    @Transactional
    public void cancleOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        System.out.println("cancel order ::::::::::::::::" + order);
        // 주문 취소
        order.cancel();
    }


}
