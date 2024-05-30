package Dao;

import Controllers.MainApplication;
import Models.Knifes;
import Models.Producers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Класс для доступа к данным о производителях.
 */
public class ProducersDao implements Dao<Producers, Integer> {
    private final Logger logger = LoggerFactory.getLogger(ProducersDao.class);
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

    /**
     * Возвращает объект по его идентификатору.
     * @param integer идентификатор
     * @return объект
     */
    @Override
    public Producers findById(Integer integer) {
        Producers producers = null;
        logger.debug("Поиск по ID");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.selectProducerById"))) {
            statement.setInt(1, integer);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    producers = new Producers();
                    producers.setId( resultSet.getInt("id"));
                    producers.setName(resultSet.getString("name"));
                    producers.setCountry(resultSet.getString("county"));
                }
            }
            logger.debug("Объект успешно получен");
        } catch (SQLException e) {
            logger.error("Ошибка поиска по ID", e);
            throw new RuntimeException(e);
        }
        return producers;
    }

    /**
     * Возвращает объект по его идентификатору.
     * @param id идентификатор
     * @return объект
     */
    public Collection<Knifes> findByProducerId(int id) throws SQLException {
        List<Knifes> list1 = null;
        ResultSet rs = null;
        logger.debug("Поиск по ID производителя");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.findProducerByKnifeId"))) {
            statement.setInt(1, id);
            rs = statement.executeQuery();
            list1 = mapper1(rs);
            logger.debug("Коллекция успешно загружена");
            logger.debug("Объект успешно получен");
        } catch (SQLException e) {
            logger.error("Ошибка поиска по ID производителя", e);
            throw new RuntimeException(e);
        }
        return list1;
    }

    /**
     * Создает список объектов на основе результата запроса к базе данных.
     * @param rs результат запроса к базе данных
     * @return список объектов
     */
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
     * Возвращает коллекцию всех производителях.
     * @return коллекция объектов
     */
    @Override
    public Collection<Producers> finAll() {
        logger.debug("Загрузка коллекции");
        List<Producers> list = null;
        ResultSet rs = null;
        try(PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.findProducer"))){
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
     * Сохраняет объект в базу данных.
     * @param entity объект
     * @return сохраненный объект
     */
    @Override
    public Producers save(Producers entity) {
        logger.debug("Сохранение объекта");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.insertProducer"),new String[] {"id"})) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getCountry());
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
    public Producers update(Producers entity) {
        logger.debug("Обновление объекта");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.updateProducer"))) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getCountry());
            statement.setInt(3, entity.getId());
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
    public Producers delete(Producers entity) {
        logger.debug("Удаление объекта");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.deleteProducer")))
        {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getCountry());
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
        try(PreparedStatement statement = MainApplication.getConnection().prepareStatement("DELETE FROM producers WHERE id = ?")){
            statement.setInt(1, integer);
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
    protected  List<Producers> mapper (ResultSet rs){
        logger.debug("Создание списка объектов");
        List<Producers> list = new ArrayList<>();
        try{
            while (rs.next()) {
                list.add(new Producers(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("county")
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
