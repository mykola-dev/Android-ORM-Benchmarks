package ds.database;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class DBGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(9, args[0]);

        addUser(schema);
        schema.enableKeepSectionsByDefault();
        new DaoGenerator().generateAll(schema, args[1]);
    }


    private static void addUser(Schema schema) {
        Entity user = schema.addEntity("UserGreenDao");
        user.implementsSerializable();
        user.implementsInterface("ds.dbtests.db.User");
        user.addImport("java.io.*");
        user.addImport("java.util.*");
        user.addImport("ds.dbtests.db.Order");
        user.addIdProperty().autoincrement();
        user.addIntProperty("age").notNull();
        user.addDoubleProperty("height").notNull();
        user.addStringProperty("name");
        user.addStringProperty("login");
        user.addStringProperty("password");
        user.addStringProperty("sex");
        user.addStringProperty("description");
        user.addStringProperty("phone");
        //user.addByteArrayProperty("rawOrders");
        user.addByteArrayProperty("rawOrders")
            .customType("java.util.List<OrderGreenDao>", "ds.dbtests.db.greendao.OrderSerializer");

        Entity order = schema.addEntity("OrderGreenDao");
        order.implementsSerializable();
        order.implementsInterface("ds.dbtests.db.Order");
        order.addImport("ds.dbtests.db.User");
        order.addIdProperty().autoincrement();
        order.addStringProperty("title");
        order.addDoubleProperty("price").notNull();
        order.addIntProperty("count").notNull();
        order.addDateProperty("created").notNull();
        order.addDateProperty("expiration").notNull();
        order.addStringProperty("description").notNull();
        final Property userId = order.addLongProperty("userId").notNull().getProperty();
        order.addToOne(user, userId, "user");
        user.addToMany(order, userId);


    }
}
