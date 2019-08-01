package nl.han.oose.dea.rest.services;

import nl.han.oose.dea.rest.services.dto.ItemDTO;
import nl.han.oose.dea.rest.services.exceptions.IdAlreadyInUseException;
import nl.han.oose.dea.rest.services.exceptions.ItemNotAvailableException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The {@code ItemService} can be used for accessing a {@link List} of {@link ItemDTO} instances, but also
 * for adding instances to and deleting from the {@link List}.
 */
public class ItemService {

    private List<ItemDTO> items = new ArrayList<>();

    public ItemService() {
        items.add(new ItemDTO(1, "Bread", new String[]{"Breakfast, Lunch"}, "Delicious!"));
        items.add(new ItemDTO(2, "Butter", new String[]{"Breakfast, Lunch"}, "Use it with bread"));
        items.add(new ItemDTO(3, "Honey", new String[]{"Breakfast, Lunch"}, "Use it with bread"));
    }

    /**
     * Return the full {@link List} of {@link ItemDTO} instances.
     *
     * @return The full {@link List} of {@link ItemDTO} instances.
     */
    public List<ItemDTO> getAll() {
        return items;
    }

    /**
     * Add an item to the {@link List} of items.
     * <p>
     * Note that the newly added item should have an unique Id.
     *
     * @param itemDTO The {@link ItemDTO} to be added
     * @throws IdAlreadyInUseException Thrown if the Id is not unique
     */
    public void addItem(ItemDTO itemDTO) {
        if (items.stream().anyMatch(item -> item.getId() == itemDTO.getId())) {
            throw new IdAlreadyInUseException();
        }

        items.add(itemDTO);
    }

    /**
     * Return a specific {@link ItemDTO} with the given Id.
     *
     * @param id The Id of the {@link ItemDTO} to be returned
     * @throws ItemNotAvailableException Thrown if there is no {@link ItemDTO} for the given Id
     */
    public ItemDTO getItem(int id) {
        Optional<ItemDTO> requestedItem = items.stream().filter(item -> item.getId() == id).findFirst();

        if (requestedItem.isPresent()) {
            return requestedItem.get();
        } else {
            throw new ItemNotAvailableException();
        }
    }

    /**
     * Delete a specific {@link ItemDTO} with the given Id.
     *
     * @throws ItemNotAvailableException Thrown if there is no {@link ItemDTO} for the given Id
     */
    public void deleteItem(int id) {
        Optional<ItemDTO> itemForName = items.stream().filter(item -> item.getId() == id).findFirst();

        List<ItemDTO> filteredItems = items.stream().filter(item -> item.getId() != id).collect(Collectors.toList());

        if (filteredItems.size() == items.size()) {
            throw new ItemNotAvailableException();
        }

        items = filteredItems;
    }
}
