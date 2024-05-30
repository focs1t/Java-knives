package Dao;

import Controllers.MainApplication;
import Models.Knifes;
import Models.Orders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Класс для доступа к данным о заказах.
 */
public class OrdersDao implements Dao<Orders, Integer> {
    private final Logger logger = LoggerFactory.getLogger(OrdersDao.class);
    static Properties property = new Properties();
    {
        try (InputStream input = this.getClass().getResourceAsStream("/statements.properties")) {
            property.load(input);
            logger.debug("Данные из файла statements.properties получены");
        } catch (IOException e) {
            logger.error("Ошибка получения данных из файла statements.properties", e);
            throw new RuntimeException(e);
        }
    }

    public Collection<Knifes> findAvaliableKnifes() {
        List<Knifes> list1 = null;
        ResultSet rs = null;
        logger.debug("Поиск по ID ножей");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement("SELECT * FROM knifes WHERE avaliability = 'в наличии'")) {
            rs = statement.executeQuery();
            list1 = mapper1(rs);
            logger.debug("Коллекция успешно загружена");
            logger.debug("Объект ножей успешно получен");
        } catch (SQLException e) {
            logger.error("Ошибка поиска по ID ножей", e);
            System.out.println(e.getMessage());
        }
        return list1;
    }

    protected  List<Knifes> mapper1 (ResultSet rs){
        logger.debug("Создание списка объектов");
        List<Knifes> list = new ArrayList<>();
        try{
            while (rs.next()) {
                list.add(new Knifes(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("avaliability"),
                        rs.getInt("quantityInStock"),
                        rs.getInt("categoriesId"),
                        rs.getInt("producersId")
                ));
            }
            logger.debug("Список объектов успешно создан");
        }
        catch (SQLException e){
            logger.error("Ошибка создания списка объектов", e);
            System.out.println(e.getMessage());
        }
        return list;
    }

    /**
     * Возвращает объект  по его идентификатору.
     * @param integer идентификатор
     * @return объект
     */
    @Override
    public Orders findById(Integer integer) {
        Orders orders = null;
        logger.debug("Поиск по ID");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.selectOrderById"))) {
            statement.setLong(1, integer);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    orders = new Orders();
                    orders.setId(resultSet.getInt("id"));
                    orders.setKnifeId(resultSet.getInt("knifeId"));
                    orders.setDate(resultSet.getDate("date"));
                    orders.setQuantity(resultSet.getInt("quantity"));
                }
            }
            logger.debug("Объект успешно получен");
        } catch (SQLException e) {
            logger.error("Ошибка поиска по ID", e);
            throw new RuntimeException(e);
        }
        return orders;
    }

    /**
     * Возвращает коллекцию всех заказов.
     * @return коллекция объектов
     */
    @Override
    public Collection<Orders> finAll() {
        logger.debug("Загрузка коллекции");
        List<Orders> list = null;
        ResultSet rs = null;
        try(PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.findOrders"))){
            rs = statement.executeQuery();
            list = mapper(rs);
            logger.debug("Коллекция успешно загружена");
        }
        catch (SQLException e){
            logger.error("Ошибка загрузки коллекции", e);
            System.out.println(e.getMessage());
        }
        return list;
    }

    /**
     * Сохраняет объект  в базу данных.
     * @param entity объект
     * @return сохраненный объект
     */
    @Override
    public Orders save(Orders entity) {
        logger.debug("Сохранение объекта");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.insertOrder"),new String[] {"id"})) {
            statement.setInt(1, entity.getKnifeId());
            statement.setDate(2, (Date) entity.getDate());
            statement.setInt(3, entity.getQuantity());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted == 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getInt(1));
                        return entity;
                    } else {
                        throw new SQLException("Unable to create, id was not found.");
                    }
                }
            }
            logger.debug("Объект успешно сохранен");
        } catch (SQLException e) {
            logger.error("Ошибка сохранения объекта", e);
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Обновляет информацию.
     * @param entity объект
     * @return обновленный объект
     */
    @Override
    public Orders update(Orders entity) {
        logger.debug("Обновление объекта");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.updateOrder"))) {
            statement.setInt(2, entity.getKnifeId());
            statement.setDate(3, (Date) entity.getDate());
            statement.setInt(4, entity.getQuantity());
            statement.setInt(5, entity.getId());
            statement.executeUpdate();
            logger.debug("Объект успешно обновлен");
        } catch (SQLException e) {
            logger.error("Ошибка обновления объекта", e);
            System.out.println(e.getMessage());
        }
        return entity;
    }

    /**
     * Удаляет объект.
     * @param entity объект
     * @return удаленный объект
     */
    @Override
    public Orders delete(Orders entity) {
        logger.debug("Удаление объекта");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.deleteOrder")))
        {
            statement.setInt(2, entity.getKnifeId());
            statement.setDate(3, (Date) entity.getDate());
            statement.setInt(4, entity.getQuantity());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Unable to delete.");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getInt(1));
            }
            logger.debug("Объект успешно удален");
            return entity;
        } catch (SQLException e) {
            logger.error("Ошибка удаления объекта", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Удаляет объект по его идентификатору.
     * @param integer идентификатор
     */
    @Override
    public void deleteById(Integer integer) {
        logger.debug("Удаление объекта");
        try(PreparedStatement statement = MainApplication.getConnection().prepareStatement("DELETE FROM orders WHERE id = ?")){
            statement.setLong(1, integer);
            statement.executeUpdate();
            logger.debug("Объект успешно удален");
        }catch (SQLException e){
            logger.error("Ошибка удаления объекта", e);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Создает список объектов на основе результата запроса к базе данных.
     * @param rs результат запроса к базе данных
     * @return список объектов
     */
    protected  List<Orders> mapper (ResultSet rs){
        logger.debug("Создание списка объектов");
        List<Orders> list = new ArrayList<>();
        try{
            while (rs.next()) {
                list.add(new Orders(rs.getInt("id"),
                        rs.getInt("knifeId"),
                        rs.getDate("date"),
                        rs.getInt("quantity")
                ));
            }
            logger.debug("Список объектов успешно создан");
        }
        catch (SQLException e){
            logger.error("Ошибка создания списка объектов", e);
            System.out.println(e.getMessage());
        }
        return list;
    }
}
