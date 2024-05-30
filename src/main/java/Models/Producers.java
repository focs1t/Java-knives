package Models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий производителей.
 */
@Data
@NoArgsConstructor
@Getter
@Setter
public class Producers {
    /** Уникальный идентификатор производителя. */
    private int id;

    /** Наименование производителя. */
    private String name;

    /** Страна производства. */
    private String country;

    /**
     * Конструктор класса Producers.
     * @param id Уникальный идентификатор производителя.
     * @param name Наименование производителя.
     * @param country Страна производства.
     */
    public Producers(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    /**
     * Переопределение метода toString() для возвращения наименования и страны производителя.
     * @return Наименование и страна производителя.
     */
    @Override
    public String toString() {
        return name + " (" + country + ")";
    }
}
