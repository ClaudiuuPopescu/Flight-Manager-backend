package converter;

public interface IConverter<E, D> {
	
    D convertToDTO(E entity);

    E convertToEntity(D dto);
}
