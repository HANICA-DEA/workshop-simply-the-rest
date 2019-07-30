package nl.han.oose.dea.rest.services;

import nl.han.oose.dea.rest.services.dto.ItemDTO;
import nl.han.oose.dea.rest.services.exceptions.ItemNotAvailableException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemService {

    private List<ItemDTO> items = new ArrayList<>();

    public ItemService() {
        items.add(new ItemDTO(1, "Bread", new String[]{"Breakfast, Lunch"}, "Delicious!"));
        items.add(new ItemDTO(2, "Butter", new String[]{"Breakfast, Lunch"}, "Use it with bread"));
        items.add(new ItemDTO(3, "Honey", new String[]{"Breakfast, Lunch"}, "Use it with bread"));
    }

    public List<ItemDTO> getAll() {
        return items;
    }

    public ItemDTO getItem(int id) {
        Optional<ItemDTO> itemForName = items.stream().filter(item -> item.getId() == id).findFirst();

        if (itemForName.isPresent()) {
            return itemForName.get();
        } else {
            throw new ItemNotAvailableException();
        }
    }
}
