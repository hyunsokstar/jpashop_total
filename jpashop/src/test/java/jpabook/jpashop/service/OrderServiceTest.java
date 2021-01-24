package jpabook.jpashop.service;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    // 주문 테스트
    @Test
    public void 상품주문() throws Exception {

        // 주문 함수에 필요한 멤버, 상품, 주문 수량 정보 초기화
        Member member = createMember();
        Item item = createBook("잭과 콩나물",1000, 10);
        int orderCount = 2;

        // 주문 함수 호출
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        System.out.println("orderId ::::::::::::::::::::" + orderId);

        // 생성된 주문 정보 다시 조회
        Order getOrder = orderRepository.findOne(orderId);
        System.out.println("getOrder ::::::::::::::::::" + getOrder);

        // 생성된 주문 정보 다시 얻어 온뒤 주문 상태, 주문 상품 종류수, 주문 가격 합계, 주문 아이템의 재고 수량 확인
        assertEquals("상품 주문시 상태 check ORDER 이어야 하 ", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("상품 주문 종류 check ", 1, getOrder.getOrderItems().size());
        assertEquals("상품 주문 가격 합계 check ", 1000 * 2, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다." , 8, item.getStockQuantity());

    }

    @Test
    public void 주문취소() throws Exception {
        // 주문에 필요한 회원 정보, 아이템 정보, 수량 정보 초기화
        Member member = createMember();
        Item item = createBook("잭과 콩나물",1000, 10);
        int orderCount = 2;

        // 주문 하기 함수 호출
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // 주문 취소 함수 호출
        orderService.cancelOrder(orderId);

        // 주문 정보 다시 불러 오기
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소후 주문 상태는 CANCEL 이다.", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가 해야 한다.", 10, item.getStockQuantity());

    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }



}

