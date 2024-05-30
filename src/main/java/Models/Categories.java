package Models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий специализации врачей.
 */
@Data
@NoArgsConstructor
@Getter
@Setter
public class Categories {
    /** Уникальный идентификатор специализации врача. */
    private int id;

    /** Наименование специализации врача. */
    private String name;

    /**
     * Конструктор класса Categories.
     * @param id Уникальный идентификатор специализации врача.
     * @param name Наименование специализации врача.
     */
    public Categories(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Переопределение метода toString() для возвращения наименования специализации врача.
     * @return Наименование специализации врача.
     */
    @Override
    public String toString() {
        return name;
    }
}
