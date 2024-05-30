package Dao;

import Controllers.MainApplication;
import Models.Categories;
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
 * Класс для доступа к данным о категориях.
 */
public class CategoriesDao implements Dao<Categories, Integer> {
    private final Logger logger = LoggerFactory.getLogger(CategoriesDao.class);
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
     * @param integer идентификатор категории
     * @return объект категории
     */
    @Override
    public Categories findById(Integer integer) {
        Categories categories = null;
        logger.debug("Поиск по ID");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.selectCategoryById"))) {
            statement.setLong(1, integer);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    categories = new Categories();
                    categories.setId(resultSet.getInt("id"));
                    categories.setName(resultSet.getString("name"));
                }
            }
            logger.debug("Объект успешно получен");
        } catch (SQLException e) {
            logger.error("Ошибка поиска по ID", e);
            throw new RuntimeException(e);
        }
        return categories;
    }

    /**
     * Возвращает объект по его идентификатору.
     * @param id идентификатор ножа
     * @return объект ножа
     */
    public Collection<Knifes> findByKnifeId(int id) throws SQLException {
        List<Knifes> list1 = null;
        ResultSet rs = null;
        logger.debug("Поиск по ID ножа");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.findCategoryByKnifeId"))) {
            statement.setInt(1, id);
            rs = statement.executeQuery();
            list1 = mapper1(rs);
            logger.debug("Коллекция успешно загружена");
            logger.debug("Объект ножа успешно получен");
        } catch (SQLException e) {
            logger.error("Ошибка поиска по ID ножа", e);
            throw new RuntimeException(e);
        }
        return list1;
    }

    /**
     * Создает список объектов на основе результата запроса к базе данных.
     * @param rs результат запроса к базе данных
     * @return список объектов категорий
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
     * Возвращает коллекцию всех категориях.
     * @return коллекция объектов категорий
     */
    @Override
    public Collection<Categories> finAll() {
        logger.debug("Загрузка коллекции");
        List<Categories> list = null;
        ResultSet rs = null;
        try(PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.findCategory"))){
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
     * @param entity объект категории
     * @return сохраненный объект
     */
    @Override
    public Categories save(Categories entity) {
        logger.debug("Сохранение объекта");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.insertCategory"),new String[] {"id"})) {
            statement.setString(1, entity.getName());
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
     * Обновляет информацию о категории.
     * @param entity объект категориях
     * @return обновленный объект категорий
     */
    @Override
    public Categories update(Categories entity) {
        logger.debug("Обновление объекта");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.updateCategory"))) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getId());
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
    public Categories delete(Categories entity) {
        logger.debug("Удаление объекта");
        try (PreparedStatement statement = MainApplication.getConnection().prepareStatement(property.getProperty("sql.deleteCategory")))
        {
            statement.setString(1, entity.getName());
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
     * Удаляет объектпо его идентификатору.
     * @param integer идентификатор
     */
    @Override
    public void deleteById(Integer integer) {
        logger.debug("Удаление объекта");
        try(PreparedStatement statement = MainApplication.getConnection().prepareStatement("DELETE FROM categories WHERE id = ?")){
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
    protected  List<Categories> mapper (ResultSet rs){
        logger.debug("Создание списка объектов");
        List<Categories> list = new ArrayList<>();
        try{
            while (rs.next()) {
                list.add(new Categories(rs.getInt("id"),
                        rs.getString("name")
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
