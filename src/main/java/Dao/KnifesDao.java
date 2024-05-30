package Dao;

import Controllers.MainApplication;
import Models.Knifes;
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
 * Класс для доступа к данным о ножах.
 */
public class KnifesDao implements Dao<Knifes, Integer> {
    private final Logger logger = LoggerFactory.getLogger(KnifesDao.class);
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
    public Knifes findById(Integer integer) {
        Knifes knifes = null;
        logger.debug("Поиск по ID");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.selectKnifeById"))) {
            statement.setLong(1, integer);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    knifes = new Knifes();
                    knifes.setId(resultSet.getInt("id"));
                    knifes.setName(resultSet.getString("name"));
                    knifes.setPrice(resultSet.getDouble("price"));
                    knifes.setDescription(resultSet.getString("description"));
                    knifes.setAvaliability(resultSet.getString("avaliability"));
                    knifes.setQuantityInStock(resultSet.getInt("quantityInStock"));
                    knifes.setCategoriesId(resultSet.getInt("categoriesId"));
                    knifes.setProducersId(resultSet.getInt("producersId"));
                }
            }
            logger.debug("Объект успешно получен");
        } catch (SQLException e) {
            logger.error("Ошибка поиска по ID", e);
            throw new RuntimeException(e);
        }
        return knifes;
    }

    /**
     * Возвращает коллекцию всех ножей.
     * @return коллекция объектов
     */
    @Override
    public Collection<Knifes> finAll() {
        logger.debug("Загрузка коллекции");
        List<Knifes> list = null;
        ResultSet rs = null;
        try(PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.findKnife"))){
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
    public Knifes save(Knifes entity) {
        logger.debug("Сохранение объекта");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.insertKnife"),new String[] {"id"})) {
            statement.setString(1, entity.getName());
            statement.setDouble(2, entity.getPrice());
            statement.setString(3, entity.getDescription());
            statement.setString(4, entity.getAvaliability());
            statement.setInt(5, entity.getQuantityInStock());
            statement.setInt(6, entity.getCategoriesId());
            statement.setInt(7, entity.getProducersId());
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
    public Knifes update(Knifes entity) {
        logger.debug("Обновление объекта");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.updateKnife"))) {
            statement.setString(1, entity.getName());
            statement.setDouble(2, entity.getPrice());
            statement.setString(3, entity.getDescription());
            statement.setString(4, entity.getAvaliability());
            statement.setInt(5, entity.getQuantityInStock());
            statement.setInt(6, entity.getCategoriesId());
            statement.setInt(7, entity.getProducersId());
            statement.setInt(8, entity.getId());
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
    public Knifes delete(Knifes entity) {
        logger.debug("Удаление объекта");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.deleteKnife")))
        {
            statement.setString(1, entity.getName());
            statement.setDouble(2, entity.getPrice());
            statement.setString(3, entity.getDescription());
            statement.setString(4, entity.getAvaliability());
            statement.setInt(5, entity.getQuantityInStock());
            statement.setInt(6, entity.getCategoriesId());
            statement.setInt(7, entity.getProducersId());
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
        try(PreparedStatement statement = MainApplication.getConnection().prepareStatement("DELETE FROM knifes WHERE id = ?")){
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
    protected  List<Knifes> mapper (ResultSet rs){
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
}
