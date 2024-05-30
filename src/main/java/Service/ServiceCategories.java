package Service;


import Dao.CategoriesDao;
import Models.Categories;
import Models.Knifes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Класс, предоставляющий сервисы для работы с данными о категориях.
 */
public class ServiceCategories {
    private final Logger logger = LoggerFactory.getLogger(ServiceCategories.class);
    private  final CategoriesDao dao;

    /**
     * Конструктор класса ServiceCategories.
     * @param dao Объект класса CategoriesDao для доступа к данным о категориях.
     */
    public ServiceCategories(CategoriesDao dao) {
        this.dao = dao;
    }

    /**
     * Метод для обновления информации о категории.
     * @param categories Объект класса Categories с информацией о категории.
     * @param name Наименование категории.
     */
    public void UpdateCategory(Categories categories, String name){
        categories.setName(name);
        dao.update(categories);
        logger.info("Обновление");
    }

    /**
     * Метод для удаления информации о категории.
     * @param categories Объект класса Categories с информацией о категории.
     */
    public void DeleteCategory(Categories categories){
        dao.deleteById(categories.getId());
        logger.info("Удаление");
    }

    /**
     * Метод для просмотра информации о всех категориях.
     */
    public Collection<Categories> ViewField(){
        return dao.finAll();
    }

    /**
     * Метод для создания новой категорией.
     * @param name Наименование новой категорией.
     * @return Объект класса Categories с информацией о новой категорией.
     * @throws IllegalArgumentException если переданы некорректные параметры.
     */
    public Categories CreateCategory(String name){
        if(name == null){
            throw  new IllegalArgumentException("Wrong parameters");
        }
        Categories categories = new Categories(0, name);
        dao.save(categories);
        logger.info("Добавление");
        return categories;
    }

    /**
     * Метод для поиска врачей по идентификатору категории.
     * @param categoryId Уникальный идентификатор категории.
     * @return Объект класса Knifes соответствующего категории.
     * @throws SQLException если возникает ошибка при работе с базой данных.
     */
    public Collection<Knifes> FindByCategoryId(int categoryId) throws SQLException {
        logger.info("Поиск");
        return dao.findByKnifeId(categoryId);
    }
}
