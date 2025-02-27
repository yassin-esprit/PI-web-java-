package Services;

import java.sql.SQLException;
import java.util.List;

public interface IServices<T> {
    void ajouter(T p) throws SQLException;
    void supprimer(int id) throws SQLException;
    public void edit(T p);
    public List<T> getAll();

}