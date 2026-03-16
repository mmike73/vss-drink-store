package drinkshop.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public abstract class AbstractRepository<ID, E>
        implements Repository<ID, E> {

    protected Map<ID, E> entities = new HashMap<>();

    @Override
    public E findOne(ID id) {
        return entities.get(id);
    }

    @Override
    public List<E> findAll() {
        return (List<E>)StreamSupport.stream(entities.values().spliterator(), false).toList();
//                    .collect(Collectors.toList());
        // return (List<E>) entities.values();
    }

    @Override
    public E save(E entity) {
        ID id = getId(entity);

        if (entities.containsKey(id)) {
            return null; // already exists
        }

        entities.put(id, entity);
        return entity;
    }

    @Override
    public E delete(ID id) {
        return entities.remove(id);
    }

    @Override
    public E update(E entity) {
        ID id = getId(entity);

        if (!entities.containsKey(id)) {
            return null; // does nit exist
        }

        entities.put(id, entity);
        return entity;
    }

    protected abstract ID getId(E entity);
}
