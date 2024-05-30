package Controllers;

import Dao.*;
import Models.*;
import Service.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class MainController {
    //region Объявления FMXL
    public TableView<Knifes> tableKnifes;
        public TableColumn<Knifes, String> NameKnifeColumn;
        public TableColumn<Knifes, Double> PriceColumn;
        public TableColumn<Knifes, String> DescriptionColumn;
        public TableColumn<Knifes, String> KnifeAvaliabilityColumn;
        public TableColumn<Knifes, Integer> KnifeQuantityStockColumn;
        public TableColumn<Knifes, String> KnifeCategoryColumn;
        public TableColumn<Knifes, String> KnifeProducerColumn;
    public TableView<Orders> tableOrders;
        public TableColumn<Orders, String> KnifeNameColumn;
        public TableColumn<Orders, Date> DateColumn;
        public TableColumn<Orders, Integer> QuantityColumn;
    public TableView<Producers> tableProducers;
        public TableColumn<Producers, String> producerNameColumn;
        public TableColumn<Producers, String> producerCountryColumn;
    public TableView<Categories> tableCategories;
        public TableColumn<Categories, String> categoryNameColumn;
    public DatePicker dataPicker;
    public TableView<Knifes> tableKnife2;
        public TableColumn<Knifes, String> KnifeNameColumn2;
        public TableColumn<Knifes, String> KnifeCategoryColumn2;
        public TableColumn<Knifes, Double> KnifePriceColumn2;
    public TableView<Knifes> tableKnife;
        public TableColumn<Knifes, String> knifeNameColumn1;
        public TableColumn<Knifes, String> knifeProducerColumn1;
        public TableColumn<Knifes, Double> KnifePriceColumn1;
        public TableColumn<Knifes, String> knifeCountryColumn1;
    public ComboBox<Knifes> KnifeNameComboBox;
    public TextField QuantityText;
    public TextField producerText;
    public TextField countryText;
    public TextField categoryText;
    public TextField KnifeNameText;
    public TextField KnifePriceText;
    public TextField KnifeDescriptionText;
    public ComboBox<Categories> CategoryCombo;
    public ComboBox<Producers> ProducerCombo;
    public ComboBox<String> KnifeAvaliabilityText;
    public TextField KnifeQuantityInStock;
    //endregion

    //region Объявления сервисов, листов и дао
    private ObservableList<Knifes> knifes = FXCollections.observableArrayList();
    private ObservableList<Knifes> knifes1 = FXCollections.observableArrayList();
    private ObservableList<Knifes> knifes2 = FXCollections.observableArrayList();
    private ServiceKnifes serviceKnifes;
    private ObservableList<Orders> orders = FXCollections.observableArrayList();
    private ServiceOrders serviceOrders;
    private ObservableList<Producers> producers = FXCollections.observableArrayList();
    private ServiceProducers serviceProducers;
    private ObservableList<Categories> categories = FXCollections.observableArrayList();
    private ServiceCategories serviceCategories;
    private ProducersDao producersDao;
    private CategoriesDao categoriesDao;
    private KnifesDao knifesDao;
    private OrdersDao ordersDao;
    //endregion

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    public MainController() {
        producersDao = new ProducersDao();
        categoriesDao = new CategoriesDao();
        knifesDao = new KnifesDao();
        ordersDao = new OrdersDao();
        serviceKnifes = new ServiceKnifes(new KnifesDao());
        serviceOrders = new ServiceOrders(new OrdersDao());
        serviceProducers = new ServiceProducers(new ProducersDao());
        serviceCategories = new ServiceCategories(new CategoriesDao());
    }

    @FXML
    protected void initialize() {
        tableKnifes.getItems().clear();
        knifes.addAll(serviceKnifes.ViewField());
        NameKnifeColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getName()));
        PriceColumn.setCellValueFactory(item -> new SimpleObjectProperty<>(item.getValue().getPrice()));
        DescriptionColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDescription()));
        KnifeAvaliabilityColumn.setCellValueFactory(item -> new SimpleObjectProperty<>(item.getValue().getAvaliability()));
        KnifeQuantityStockColumn.setCellValueFactory(item -> new SimpleObjectProperty<>(item.getValue().getQuantityInStock()));
        KnifeCategoryColumn.setCellValueFactory(item -> new SimpleObjectProperty<>(
                categoriesDao.findById(item.getValue().getCategoriesId()).getName()
        ));
        KnifeProducerColumn.setCellValueFactory(item -> new SimpleObjectProperty<>(
                producersDao.findById(item.getValue().getProducersId()).getName()
        ));
        tableKnifes.setItems(knifes);
        tableOrders.getItems().clear();
        orders.addAll(serviceOrders.ViewField());
        KnifeNameColumn.setCellValueFactory(item -> new SimpleStringProperty(
                knifesDao.findById(item.getValue().getKnifeId()).getName()
        ));
        DateColumn.setCellValueFactory(item -> new SimpleObjectProperty(item.getValue().getDate()));
        QuantityColumn.setCellValueFactory(item -> new SimpleObjectProperty<>(item.getValue().getQuantity()));
        tableOrders.setItems(orders);
        tableProducers.getItems().clear();
        producers.addAll(serviceProducers.ViewField());
        producerNameColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getName()));
        producerCountryColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getCountry()));
        tableProducers.setItems(producers);
        tableCategories.getItems().clear();
        categories.addAll(serviceCategories.ViewField());
        categoryNameColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getName()));
        tableCategories.setItems(categories);


        Collection<Knifes> availableKnives = ordersDao.findAvaliableKnifes();
        ObservableList<Knifes> knivesObservableList = FXCollections.observableArrayList(availableKnives);
        KnifeNameComboBox.setItems(knivesObservableList);
        CategoryCombo.setItems(categories);
        ProducerCombo.setItems(producers);
        //KnifeNameComboBox.setItems(knifes);
        KnifeAvaliabilityText.getItems().addAll("в наличии", "нет в наличии");

        LocalDate currentDate = LocalDate.now();
        dataPicker.setValue(currentDate);

    }

    //region Работа с Tab панелью
    public void LoadOrdersByProducer(int producerId) {
        try {
            tableKnife2.getItems().clear();
            knifes1.addAll(serviceProducers.FindByProducerId(producerId));
            if(knifes1 != null) {
                KnifeNameColumn2.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().toString()));
                KnifeCategoryColumn2.setCellValueFactory(item -> new SimpleStringProperty(
                        categoriesDao.findById(item.getValue().getCategoriesId()).getName()
                ));
                KnifePriceColumn2.setCellValueFactory(item -> new SimpleObjectProperty<>(item.getValue().getPrice()));
                tableKnife2.setItems(knifes1);
            } else {
                tableKnife2.getItems().clear();
            }
            logger.debug("Данные успешно получены");
        } catch (SQLException e) {
            logger.error("Ошибка получения данных", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось загрузить данные ножа");
            alert.setContentText("Пожалуйста, попробуйте снова.");
            alert.showAndWait();
        }
    }

    @FXML
    void ClickProducer(MouseEvent mouseEvent) {
        logger.info("Выбрана таблица 'Производители'");
        tableKnife2.getItems().clear();
        Producers selectedProducer = tableProducers.getSelectionModel().getSelectedItem();
        if (selectedProducer != null) {
            logger.debug("Строка выбрана");
            int orderId = selectedProducer.getId();
            LoadOrdersByProducer(orderId);
        }
    }

    public void LoadOrdersByCategory(int categoryId) {
        try {
            tableKnife.getItems().clear();
            knifes2.addAll(serviceCategories.FindByCategoryId(categoryId));
            if(knifes2 != null) {
                knifeNameColumn1.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().toString()));
                knifeProducerColumn1.setCellValueFactory(item -> new SimpleStringProperty(
                        producersDao.findById(item.getValue().getProducersId()).getName()
                ));
                knifeCountryColumn1.setCellValueFactory(item -> new SimpleObjectProperty<>(
                        producersDao.findById(item.getValue().getProducersId()).getCountry()
                ));
                KnifePriceColumn1.setCellValueFactory(item -> new SimpleObjectProperty<>(item.getValue().getPrice()));
                tableKnife.setItems(knifes2);
            } else {
                tableKnife.getItems().clear();
            }
            logger.debug("Данные успешно получены");
        } catch (SQLException e) {
            logger.error("Ошибка получения данных", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось загрузить данные ножа");
            alert.setContentText("Пожалуйста, попробуйте снова.");
            alert.showAndWait();
        }
    }

    @FXML
    void ClickCategory(MouseEvent mouseEvent) {
        logger.info("Выбрана таблица 'Категории'");
        tableKnife.getItems().clear();
        Categories selectedCategory = tableCategories.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            logger.debug("Строка выбрана");
            int categoryId = selectedCategory.getId();
            LoadOrdersByCategory(categoryId);
        }
    }

    @FXML
    void ClickKnife(MouseEvent mouseEvent) {
        logger.info("Выбрана таблица 'Ножи'");
        Knifes selectedKnife = tableKnifes.getSelectionModel().getSelectedItem();
        if (selectedKnife != null) {
            logger.debug("Строка выбрана");
            KnifeNameText.setText(selectedKnife.getName());
            KnifePriceText.setText(String.valueOf(selectedKnife.getPrice()));
            KnifeDescriptionText.setText(selectedKnife.getDescription());
            KnifeQuantityInStock.setText(String.valueOf(selectedKnife.getQuantityInStock()));
        }
    }

    @FXML
    void ClickOrder(MouseEvent mouseEvent) {
        logger.info("Выбрана таблица 'Заказы'");
        Orders selectedOrder = tableOrders.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            logger.debug("Строка выбрана");
            String dateString = String.valueOf(selectedOrder.getDate());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateString, formatter);
            dataPicker.setValue(date);
            QuantityText.setText(String.valueOf(selectedOrder.getQuantity()));
        }
    }

    @FXML
    void knifesTab(Event event) {
        logger.info("Выбрана вкладка 'Ножи'");
        tableKnifes.getItems().clear();
        knifes.addAll(serviceKnifes.ViewField());
        NameKnifeColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getName()));
        PriceColumn.setCellValueFactory(item -> new SimpleObjectProperty<>(item.getValue().getPrice()));
        DescriptionColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDescription()));
        KnifeAvaliabilityColumn.setCellValueFactory(item -> new SimpleObjectProperty<>(item.getValue().getAvaliability()));
        KnifeQuantityStockColumn.setCellValueFactory(item -> new SimpleObjectProperty<>(item.getValue().getQuantityInStock()));
        KnifeCategoryColumn.setCellValueFactory(item -> new SimpleObjectProperty<>(
                categoriesDao.findById(item.getValue().getCategoriesId()).getName()
        ));
        KnifeProducerColumn.setCellValueFactory(item -> new SimpleObjectProperty<>(
                producersDao.findById(item.getValue().getProducersId()).getName()
        ));
        tableKnifes.setItems(knifes);
    }

    @FXML
    void ordersTab(Event event) {
        logger.info("Выбрана вкладка 'Заказы'");
        tableOrders.getItems().clear();
        orders.addAll(serviceOrders.ViewField());
        KnifeNameColumn.setCellValueFactory(item -> new SimpleStringProperty(
                knifesDao.findById(item.getValue().getKnifeId()).getName()
        ));
        DateColumn.setCellValueFactory(item -> new SimpleObjectProperty(item.getValue().getDate()));
        QuantityColumn.setCellValueFactory(item -> new SimpleObjectProperty<>(item.getValue().getQuantity()));
        tableOrders.setItems(orders);
        initialize();
    }

    @FXML
    void producersTab(Event event) {
        logger.info("Выбрана вкладка 'Производители'");
        tableProducers.getItems().clear();
        producers.addAll(serviceProducers.ViewField());
        producerNameColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getName()));
        producerCountryColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getCountry()));
        tableProducers.setItems(producers);
        initialize();
    }

    @FXML
    void categoriesTab(Event event) {
        logger.info("Выбрана вкладка 'Категории'");
        tableCategories.getItems().clear();
        categories.addAll(serviceCategories.ViewField());
        categoryNameColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getName()));
        tableCategories.setItems(categories);
        initialize();
    }
    //endregion

    //region Работа с Buttons
    @FXML
    void AddKnife(ActionEvent actionEvent) {
        logger.info("Нажатие на кнопку 'Добавить'");
        if (KnifeNameText.getText().isEmpty() || KnifePriceText.getText().isEmpty() || KnifeDescriptionText.getText().isEmpty() ||
                CategoryCombo.getItems().isEmpty() || ProducerCombo.getItems().isEmpty()) {
            logger.debug("Имеются пустые поля");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Fields are empty:");
            alert.setContentText("Please, fill all fields.");
            alert.showAndWait();
            return;
        }
        try {
            String name = KnifeNameText.getText();
            Double price = Double.valueOf(KnifePriceText.getText());
            String description = KnifeDescriptionText.getText();
            //String avaliability = KnifeAvaliabilityText.getSelectionModel().getSelectedItem().toString();
            int quantityInStock = Integer.parseInt(KnifeQuantityInStock.getText());
            String avaliability = null;
            if(quantityInStock > 0) {
                avaliability = "в наличии";
            } else if (quantityInStock == 0) {
                avaliability = "нет в наличии";
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");
                alert.setHeaderText("Ошибка записи - неверный формат данных:");
                alert.setContentText("Please, fill all fields.");
                alert.showAndWait();
                return;
            }
            int categoriesId = CategoryCombo.getSelectionModel().getSelectedItem().getId();
            int producersId = ProducerCombo.getSelectionModel().getSelectedItem().getId();
            Knifes knifes1 = serviceKnifes.CreateKnife(name, price, description, avaliability, quantityInStock, categoriesId, producersId);
            tableKnifes.getItems().add(knifes1);
            tableKnifes.getItems().clear();
            knifes.addAll(serviceKnifes.ViewField());
            logger.debug("Успешное добавление записи");
        } catch (NumberFormatException e) {
            logger.error("Ошибка записи - неверный формат данных", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Incorrect data format:");
            alert.setContentText("Please, Check all data and try again.");
            alert.showAndWait();
        }
    }

    @FXML
    void DeleteKnife(ActionEvent actionEvent) {
        logger.info("Нажатие на кнопку 'Удалить'");
        Knifes knifes1 = tableKnifes.getSelectionModel().getSelectedItem();
        if (knifes1 != null) {
            serviceKnifes.DeleteKnife(knifes1);
            tableKnifes.getItems().remove(knifes1);
            tableKnifes.getItems().clear();
            knifes.addAll(serviceKnifes.ViewField());
            logger.debug("Объект успешно удален");
        } else {
            logger.debug("Ошибка получения объекта");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alert");
            alert.setHeaderText("Row is not selected:");
            alert.setContentText("Please, choose row for deleting.");
            alert.showAndWait();
        }
    }

    @FXML
    void UpdateKnife(ActionEvent actionEvent) {
        logger.info("Нажатие на кнопку 'Изменить'");
        Knifes selectedKnife = tableKnifes.getSelectionModel().getSelectedItem();
        if (selectedKnife == null) {
            logger.debug("Объект не выбран");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alert");
            alert.setHeaderText("Row is not selected:");
            alert.setContentText("Please, choose row for editing.");
            alert.showAndWait();
            return;
        }
        try {
            String name = KnifeNameText.getText();
            Double price = Double.valueOf(KnifePriceText.getText());
            String description = KnifeDescriptionText.getText();
            String avaliability = KnifeAvaliabilityText.getSelectionModel().getSelectedItem().toString();
            int quantityInStock = Integer.parseInt(KnifeQuantityInStock.getText());
            int categoriesId = CategoryCombo.getSelectionModel().getSelectedItem().getId();
            int producersId = ProducerCombo.getSelectionModel().getSelectedItem().getId();
            selectedKnife.setName(name);
            selectedKnife.setPrice(price);
            selectedKnife.setDescription(description);
            selectedKnife.setAvaliability(avaliability);
            selectedKnife.setQuantityInStock(quantityInStock);
            selectedKnife.setCategoriesId(categoriesId);
            selectedKnife.setProducersId(producersId);
            serviceKnifes.UpdateKnife(selectedKnife, name, price, description, avaliability, quantityInStock, categoriesId, producersId);
            tableKnifes.getItems().clear();
            knifes.addAll(serviceKnifes.ViewField());
            logger.debug("Успешное обновление записи");
        } catch (NumberFormatException e) {
            logger.error("Ошибка обновления записи - неверный формат данных", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Incorrect data format:");
            alert.setContentText("Please, Check all data and try again.");
            alert.showAndWait();
        }
    }

    @FXML
    void AddOrder(ActionEvent actionEvent) {
        logger.info("Нажатие на кнопку 'Добавить'");
        tableOrders.getItems().clear();
        if (KnifeNameComboBox.getItems().isEmpty() || QuantityText.getText().isEmpty()) {
            logger.debug("Имеются пустые поля");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Fields are empty:");
            alert.setContentText("Please, fill all fields.");
            alert.showAndWait();
            return;
        }
        try {
            int knifeId = KnifeNameComboBox.getSelectionModel().getSelectedItem().getId();
            Date date = Date.valueOf(dataPicker.getValue());
            int quantity = Integer.parseInt((QuantityText.getText()));
            Orders orders1 = serviceOrders.CreateOrder(knifeId, date, quantity);
            tableOrders.getItems().add(orders1);
            tableOrders.getItems().clear();
            orders.addAll(serviceOrders.ViewField());
            logger.debug("Успешное добавление записи");
        } catch (NumberFormatException e) {
            logger.error("Ошибка записи - неверный формат данных", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Incorrect data format:");
            alert.setContentText("Please, Check all data and try again.");
            alert.showAndWait();
        }
    }

    @FXML
    void DeleteOrder(ActionEvent actionEvent) {
        logger.info("Нажатие на кнопку 'Удалить'");
        Orders orders1 = tableOrders.getSelectionModel().getSelectedItem();
        if (orders1 != null) {
            serviceOrders.DeleteOrder(orders1);
            tableOrders.getItems().remove(orders1);
            tableOrders.getItems().clear();
            orders.addAll(serviceOrders.ViewField());
            logger.debug("Объект успешно удален");
        } else {
            logger.debug("Ошибка получения объекта");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alert");
            alert.setHeaderText("Row is not selected:");
            alert.setContentText("Please, choose row for deleting.");
            alert.showAndWait();
        }
    }

    @FXML
    void UpdateOrder(ActionEvent actionEvent) {
        logger.info("Нажатие на кнопку 'Изменить'");
        Orders selectedOrder = tableOrders.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            logger.debug("Объект не выбран");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alert");
            alert.setHeaderText("Row is not selected:");
            alert.setContentText("Please, choose row for editing.");
            alert.showAndWait();
            return;
        }
        try {
            int knifeId = KnifeNameComboBox.getSelectionModel().getSelectedItem().getId();
            Date date = Date.valueOf(dataPicker.getValue());
            int quantity = Integer.parseInt((QuantityText.getText()));
            Orders orders1 = serviceOrders.CreateOrder(knifeId, date, quantity);
            selectedOrder.setKnifeId(knifeId);
            selectedOrder.setDate(date);
            selectedOrder.setQuantity(quantity);
            serviceOrders.UpdateOrder(selectedOrder, knifeId, date, quantity);
            tableOrders.getItems().clear();
            orders.addAll(serviceOrders.ViewField());
            logger.debug("Успешное обновление записи");
        } catch (NumberFormatException e) {
            logger.error("Ошибка обновления записи - неверный формат данных", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Incorrect data format:");
            alert.setContentText("Please, Check all data and try again.");
            alert.showAndWait();
        }
    }

    @FXML
    void AddProducer(ActionEvent actionEvent) {
        logger.info("Нажатие на кнопку 'Добавить'");
        tableProducers.getItems().clear();
        if (producerText.getText().isEmpty()) {
            logger.debug("Имеются пустые поля");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Fields are empty:");
            alert.setContentText("Please, fill all fields.");
            alert.showAndWait();
            return;
        }
        try {
            String name = producerText.getText();
            String county = countryText.getText();
            Producers producers1 = serviceProducers.CreateProducer(name, county);
            tableProducers.getItems().add(producers1);
            tableProducers.getItems().clear();
            producers.addAll(serviceProducers.ViewField());
            logger.debug("Успешное добавление записи");
        } catch (NumberFormatException e) {
            logger.error("Ошибка записи - неверный формат данных", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Incorrect data format:");
            alert.setContentText("Please, Check all data and try again.");
            alert.showAndWait();
        }
    }

    @FXML
    void DeleteProducer(ActionEvent actionEvent) {
        logger.info("Нажатие на кнопку 'Удалить'");
        Producers producers1 = tableProducers.getSelectionModel().getSelectedItem();
        if (producers1 != null) {
            serviceProducers.DeleteProducer(producers1);
            tableProducers.getItems().remove(producers1);
            tableProducers.getItems().clear();
            producers.addAll(serviceProducers.ViewField());
            tableKnife2.getItems().clear();
            logger.debug("Объект успешно удален");
        } else {
            logger.debug("Ошибка получения объекта");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alert");
            alert.setHeaderText("Row is not selected:");
            alert.setContentText("Please, choose row for deleting.");
            alert.showAndWait();
        }
    }

    @FXML
    void UpdateProducer(ActionEvent actionEvent) {
        logger.info("Нажатие на кнопку 'Изменить'");
        Producers selectedProducer = tableProducers.getSelectionModel().getSelectedItem();
        if (selectedProducer == null) {
            logger.debug("Объект не выбран");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Контракт не выбран");
            alert.setContentText("Пожалуйста, выберите контракт для обновления.");
            alert.showAndWait();
            return;
        }
        try {
            String name = producerText.getText();
            String country = countryText.getText();
            selectedProducer.setName(name);
            selectedProducer.setCountry(country);
            serviceProducers.UpdateProducer(selectedProducer, name, country);
            tableProducers.getItems().clear();
            producers.addAll(serviceProducers.ViewField());
            logger.debug("Успешное обновление записи");
        } catch (NumberFormatException e) {
            logger.error("Ошибка обновления записи - неверный формат данных", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Incorrect data format:");
            alert.setContentText("Please, Check all data and try again.");
            alert.showAndWait();
        }
    }

    @FXML
    void AddCategory(ActionEvent actionEvent) {
        logger.info("Нажатие на кнопку 'Добавить'");
        tableCategories.getItems().clear();
        if (categoryText.getText().isEmpty()) {
            logger.debug("Имеются пустые поля");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Fields are empty:");
            alert.setContentText("Please, fill all fields.");
            alert.showAndWait();
            return;
        }
        try {
            String name = categoryText.getText();
            Categories categories1 = serviceCategories.CreateCategory(name);
            tableCategories.getItems().add(categories1);
            tableCategories.getItems().clear();
            categories.addAll(serviceCategories.ViewField());
            logger.debug("Успешное добавление записи");
        } catch (NumberFormatException e) {
            logger.error("Ошибка записи - неверный формат данных", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Incorrect data format:");
            alert.setContentText("Please, Check all data and try again.");
            alert.showAndWait();
        }
    }

    @FXML
    void DeleteCategory(ActionEvent actionEvent) {
        logger.info("Нажатие на кнопку 'Удалить'");
        Categories categories1 = tableCategories.getSelectionModel().getSelectedItem();
        if (categories1 != null) {
            serviceCategories.DeleteCategory(categories1);
            tableCategories.getItems().remove(categories1);
            tableCategories.getItems().clear();
            categories.addAll(serviceCategories.ViewField());
            tableKnife.getItems().clear();
            logger.debug("Объект успешно удален");
        } else {
            logger.debug("Ошибка получения объекта");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alert");
            alert.setHeaderText("Row is not selected:");
            alert.setContentText("Please, choose row for deleting.");
            alert.showAndWait();
        }
    }

    @FXML
    void UpdateCategory(ActionEvent actionEvent) {
        logger.info("Нажатие на кнопку 'Изменить'");
        Categories selectedCategory = tableCategories.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            logger.debug("Объект не выбран");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alert");
            alert.setHeaderText("Row is not selected:");
            alert.setContentText("Please, choose row for editing.");
            alert.showAndWait();
            return;
        }
        try {
            String name = categoryText.getText();
            selectedCategory.setName(name);
            serviceCategories.UpdateCategory(selectedCategory, name);
            tableCategories.getItems().clear();
            categories.addAll(serviceCategories.ViewField());
            logger.debug("Успешное обновление записи");
        } catch (NumberFormatException e) {
            logger.error("Ошибка обновления записи - неверный формат данных", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Incorrect data format:");
            alert.setContentText("Please, Check all data and try again.");
            alert.showAndWait();
        }
    }
    //endregion
}