package Service;

import Dao.KnifesDao;
import Models.Knifes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Класс, предоставляющий сервисы для работы с данными о  ножах.
 */
public class ServiceKnifes {
    private final Logger logger = LoggerFactory.getLogger(ServiceKnifes.class);
    private  final KnifesDao dao;

    /**
     * Конструктор класса ServiceKnifes.
     * @param dao Объект класса KnifesDao для доступа к данным о ножах.
     */
    public ServiceKnifes(KnifesDao dao) {
        this.dao = dao;
    }

    /**
     * Метод для обновления информации о ножах.
     * @param knifes Объект класса Knifes с информацией о ножах.
     * @param name название.
     * @param description описание.
     * @param avaliability доступность.
     * @param producersId Уникальный идентификатор произовиделя.
     * @param categoriesId Уникальный идентификатор категорий.
     */
    public void UpdateKnife(Knifes knifes, String name, double price, String description, String avaliability, int quantityInStock, int producersId, int categoriesId){
        knifes.setName(name);
        knifes.setPrice(price);
        knifes.setDescription(description);
        knifes.setAvaliability(avaliability);
        knifes.setQuantityInStock(quantityInStock);
        knifes.setCategoriesId(producersId);
        knifes.setProducersId(categoriesId);
        dao.update(knifes);
        logger.info("Обновление");
    }

    /**
     * Метод для удаления информации о ноже.
     * @param knifes Объект класса Knifes с информацией о ноже.
     */
    public void DeleteKnife(Knifes knifes){
        dao.deleteById(knifes.getId());
        logger.info("Удаление");
    }

    /**
     * Метод для просмотра информации о всех ножах.
     */
    public Collection<Knifes> ViewField(){
        return dao.finAll();
    }

    /**
     * Метод для создания нового ножа.
     * @param name название.
     * @param description описание.
     * @param avaliability доступность.
     * @param producersId Уникальный идентификатор произовидетеля.
     * @param categoriesId Уникальный идентификатор категории.
     * @return Объект класса Knifes с информацией о новом ноже.
     * @throws IllegalArgumentException если переданы некорректные параметры.
     */
    public Knifes CreateKnife(String name, double price, String description, String avaliability, int quantityInStock, int producersId, int categoriesId){
        if(name == null || price <= 0 || description == null || avaliability == null || quantityInStock < 0 || producersId <= 0 || categoriesId <= 0){
            throw  new IllegalArgumentException("Wrong parameters");
        }
        Knifes knifes = new Knifes(0, name, price, description, avaliability, quantityInStock, producersId, categoriesId);
        dao.save(knifes);
        logger.info("Добавление");
        return knifes;
    }
}
