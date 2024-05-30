package Models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий медицинские инструменты.
 */
@Data
@NoArgsConstructor
@Getter
@Setter
public class Knifes {
    /** Уникальный идентификатор медицинского инструмента. */
    private int id;

    /** Наименование медицинского инструмента. */
    private String name;

    /** Цена медицинского инструмента. */
    private double price;

    /** Описание медицинского инструмента. */
    private String description;

    /** Доступность медицинского инструмента. */
    private String avaliability;

    /** Количество в наличии медицинского инструмента. */
    private int quantityInStock;

    /** Уникальный идентификатор специализации медицинского инструмента. */
    private int categoriesId;

    /** Уникальный идентификатор производителя медицинского инструмента. */
    private int producersId;

    /**
     * Конструктор класса Knifes.
     * @param id Уникальный идентификатор медицинского инструмента.
     * @param name Наименование медицинского инструмента.
     * @param price Цена медицинского инструмента.
     * @param description Описание медицинского инструмента.
     * @param avaliability Доступность медицинского инструмента.
     * @param quantityInStock Количество в наличии медицинского инструмента.
     * @param categoryId Уникальный идентификатор специализации медицинского инструмента.
     * @param producerId Уникальный идентификатор производителя медицинского инструмента.
     */
    public Knifes(int id, String name, double price, String description, String avaliability, int quantityInStock, int categoryId, int producerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.avaliability = avaliability;
        this.quantityInStock = quantityInStock;
        this.categoriesId = categoryId;
        this.producersId = producerId;
    }

    /**
     * Переопределение метода toString() для возвращения наименования медицинского инструмента и его доступности.
     * @return Наименование медицинского инструмента и его доступность.
     */
    @Override
    public String toString() {
        return name + " в наличии: " + quantityInStock;
    }
}
