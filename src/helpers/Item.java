package helpers;

public class Item<T> {
    private T id;
    private String descricao;

    public Item(T id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public T getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
