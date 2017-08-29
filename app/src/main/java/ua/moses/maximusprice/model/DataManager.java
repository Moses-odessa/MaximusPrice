package ua.moses.maximusprice.model;

import java.util.List;

public interface DataManager {
    String[] getGroups(String parentGroup);

    List<String> getSubGroups(String group);

    List<Good> getGoods(String group, String subGroup);

    void updatePrice(List<Good> goods);
}
