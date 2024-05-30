package Service;

import Dao.OrdersDao;
import Models.Orders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;

/**
 * Класс, предоставляющий сервисы для работы с заказами.
 */
public class ServiceOrders {
    private final Logger logger = LoggerFactory.getLogger(ServiceOrders.class);
    private  final OrdersDao dao;

    /**
     * Конструктор класса ServiceOrders.
     * @param dao Объект класса OrdersDao для доступа к данным о заказе.
     */
    public ServiceOrders(OrdersDao dao) {
        this.dao = dao;
    }

    /**
     * Метод для обновления информации о заказе.
     * @param orders Объект класса Orders с информацией о заказе.
     * @param knifeId Уникальный идентификатор ножа.
     * @param quantity кол-во.
     */
    public void UpdateOrder(Orders orders, int knifeId, Date date, int quantity){
        orders.setKnifeId(knifeId);
        orders.setDate(date);
        orders.setQuantity(quantity);
        dao.update(orders);
        logger.info("Обновление");
    }

    /**
     * Метод для удаления информации о заказе.
     * @param orders Объект класса Orders с информацией о заказе.
     */
    public void DeleteOrder(Orders orders){
        dao.deleteById(orders.getId());
        logger.info("Удаление");
    }

    /**
     * Метод для просмотра информации о всех заказах.
     */
    public Collection<Orders> ViewField(){
        return dao.finAll();
    }

    /**
     * Метод для создания нового заказа.
     * @param knifeId Уникальный идентификатор ножа.
     * @param quantity кол-во.
     * @return Объект класса Orders с информацией о новом ноже.
     * @throws IllegalArgumentException если переданы некорректные параметры.
     */
    public Orders CreateOrder(int knifeId, Date date, int quantity){
        if(knifeId <= 0 || date == null || quantity <= 0){
            throw  new IllegalArgumentException("Wrong parameters");
        }
        Orders orders = new Orders(0, knifeId, date, quantity);
        dao.save(orders);
        logger.info("Добавление");
        return orders;
    }
}

