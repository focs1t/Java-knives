package Service;

import Dao.ProducersDao;
import Models.Knifes;
import Models.Producers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Класс, предоставляющий сервисы для работы с данными о производителях.
 */
public class ServiceProducers {
    private final Logger logger = LoggerFactory.getLogger(ServiceProducers.class);
    private  final ProducersDao dao;

    /**
     * Конструктор класса ServiceProducers.
     * @param dao Объект класса ProducersDao для доступа к данным о производителях.
     */
    public ServiceProducers(ProducersDao dao) {
        this.dao = dao;
    }

    /**
     * Метод для обновления информации о категории врача.
     * @param producers Объект класса Producers с информацией о производителях.
     * @param name Наименование производителя.
     */
    public void UpdateProducer(Producers producers, String name, String county){
        producers.setName(name);
        producers.setCountry(county);
        dao.update(producers);
        logger.info("Обновление");
    }

    /**
     * Метод для удаления информации о производителях.
     * @param producers Объект класса Producers с информацией о производителе.
     */
    public void DeleteProducer(Producers producers){
        dao.deleteById(producers.getId());
        logger.info("Удаление");
    }

    /**
     * Метод для просмотра информации о всех произвоидетелях.
     */
    public Collection<Producers> ViewField(){
        return dao.finAll();
    }

    /**
     * Метод для создания нового производителя.
     * @param name Наименование нового производителя.
     * @return Объект класса Producers с информацией о новом производителе.
     * @throws IllegalArgumentException если переданы некорректные параметры.
     */
    public Producers CreateProducer(String name, String county){
        if(name == null || county == null){
            throw  new IllegalArgumentException("Wrong parameters");
        }
        Producers producers = new Producers(0, name, county);
        dao.save(producers);
        logger.info("Добавление");
        return producers;
    }

    /**
     * Метод для поиска врачей по идентификатору производителя.
     * @param producerId Уникальный идентификатор производителя.
     * @return Объект класса Knifes соответствующего производителя.
     * @throws SQLException если возникает ошибка при работе с базой данных.
     */
    public Collection<Knifes> FindByProducerId(int producerId) throws SQLException {
        logger.info("Поиск");
        return dao.findByProducerId(producerId);
    }
}
