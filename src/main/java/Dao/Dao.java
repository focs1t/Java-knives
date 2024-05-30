package Dao;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Интерфейс, предоставляющий базовые методы для работы с объектами в базе данных.
 * @param <ID> Тип объекта, с которым работает DAO.
 * @param <T> Тип идентификатора объекта.
 */
public interface Dao <T,ID> {

    /**
     * Найти объект по идентификатору.
     *
     * @param id Идентификатор объекта
     * @return Найденный объект
     */
    T findById(ID id);

    /**
     * Найти все объекты данного типа.
     * @return Коллекция всех объектов данного типа
     */
    Collection<T> finAll();

    /**
     * Сохранить объект в базе данных.
     *
     * @param entity Объект для сохранения
     * @return Сохраненный объект
     */
    T save(T entity);

    /**
     * Обновить существующий объект в базе данных.
     *
     * @param entity Обновляемый объект
     * @return Обновленный объект
     * @throws SQLException Исключение, если произошла ошибка при обновлении
     */
    T update(T entity) throws SQLException;

    /**
     * Обновить существующий объект в базе данных.
     *
     * @param entity Обновляемый объект
     * @return Обновленный объект
     * @throws SQLException Исключение, если произошла ошибка при обновлении
     */
    T delete(T entity) throws SQLException;

    /**
     * Удалить объект по идентификатору.
     *
     * @param id Идентификатор объекта для удаления
     * @throws SQLException Исключение, если произошла ошибка при удалении
     */
    void deleteById(ID id) throws SQLException;
}
