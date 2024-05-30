package Models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Класс, представляющий информацию о заказах на операции.
 */
@Data
@NoArgsConstructor
@Getter
@Setter
public class Orders {
    /** Уникальный идентификатор заказа. */
    private int id;

    /** Уникальный идентификатор врача, осуществляющего операцию. */
    private int knifeId;

    /** Дата операции. */
    private Date date;

    /** Количество операций. */
    private int quantity;

    /**
     * Конструктор класса Orders.
     * @param id Уникальный идентификатор заказа.
     * @param knifeId Уникальный идентификатор врача.
     * @param date Дата операции.
     * @param quantity Количество операций.
     */
    public Orders(int id, int knifeId, Date date, int quantity) {
        this.id = id;
        this.knifeId = knifeId;
        this.date = date;
        this.quantity = quantity;
    }
}
